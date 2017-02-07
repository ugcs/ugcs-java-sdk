package com.ugcs.telemetrytool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.ugcs.common.util.codec.CodecInputStream;

public abstract class TlmConverter {
	private static final SimpleDateFormat FLIGHT_INTERVAL_FORMAT =
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	private Date intervalStartTime = null;
	private Date intervalStopTime = null;
	private int tolerance = 60;
	private String sourceFile = null;
	private String destinationDirectory = new File("").getAbsolutePath();
	private String fieldsFile = null;

	public TlmConverter(String[] args) throws ParseException {
		if (args.length < 1) {
			usage();
		}

		for (int i = 0; i < args.length; ++i) {
			switch (args[i])
			{
				case "-t" :
				case "--tolerance" :
					tolerance = Integer.parseInt(args[i + 1]);
					break;
				case "-f" :
					sourceFile = args[i + 1];
					break;
				case "-d" :
					destinationDirectory = args[i + 1];
					new File(destinationDirectory).mkdir();
					break;
				case "-h" :
				case "--help" :
					usage();
					break;
				case "-l" :
				case "--fields" :
					fieldsFile = args[i + 1];
					break;
				case "-s" :
				case "--start" :
					intervalStartTime = FLIGHT_INTERVAL_FORMAT.parse(args[i + 1]);
					break;
				case "-e" :
				case "--end" :
					intervalStopTime = FLIGHT_INTERVAL_FORMAT.parse(args[i + 1]);
					break;
				default :
					usage();
					break;
			}
			i++;
		}

		if (sourceFile == null || tolerance < 0) {
			usage();
		}
	}

	abstract void usage();

	public List<TelemetryKey> getWritableFields(String fileName, FlightTelemetry telemetry) throws IOException {
		Set<TelemetryKey> set = new HashSet<>();
		for (TelemetryRecord record : telemetry.getRecords())
			set.add(record.getKey());
		List<TelemetryKey> filterList = new LinkedList<>();
		if (fileName != null) {
			if ((new File(fileName)).exists()) {
				List<String> fields = new ArrayList<>(Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8));
				if (!fields.isEmpty()) {
					for (String field : fields) {
						// trim comment
						String tempField = (field.contains("#")
								? field.substring(field.indexOf("#"))
								: field
						).trim();
						if (tempField.equals("")) {
							continue;
						}
						TelemetryKey fieldKey = TelemetryKey.parse(tempField);
						for (TelemetryKey key : set) {
							if (fieldKey.equalsByCode(key)) {
								filterList.add(key);
								break;
							}
						}
					}
					return filterList;
				}
			} else {
				throw new IllegalArgumentException("Fields file not found!");
			}
		}

		filterList.addAll(set);
		Collections.sort(filterList, TelemetryKey.codeComparator());
		return filterList;
	}

	abstract String getApplicationHelp();

	public final void write(TelemetryWriter writer) throws IOException {
		try (CodecInputStream in = new CodecInputStream(
				new BufferedInputStream(Files.newInputStream(Paths.get(sourceFile))))) {
			TelemetryModel telemetryModel =
					TelemetryModel.loadFromTlm(in, tolerance, intervalStartTime, intervalStopTime);

			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");

			for (FlightKey flightKey : telemetryModel.getFlightKeys()) {
				FlightTelemetry telemetry = telemetryModel.getFlightTelemetry(flightKey);

				String fileName =
						flightKey.getVehicleName() +
								"-" +
								format.format(new Date(flightKey.getFlightStartTime())) +
								"." +
								getExtension();

				try (OutputStream out = Files.newOutputStream(Paths.get(destinationDirectory, fileName),
						StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {

					writer.write(
							out,
							telemetry, getWritableFields(fieldsFile, telemetry));
				}
			}
		}
	}

	abstract String getExtension();
}