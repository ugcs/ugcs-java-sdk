package com.ugcs.telemetrytool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ugcs.common.util.codec.CodecInputStream;

public abstract class TlmConverter {
	private int tolerance = 60;
	private String sourceFile = null;
	private String destinationDirectory = new File("").getAbsolutePath();
	private String fieldsFile = null;

	public TlmConverter(String[] args) {
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

	abstract List<String> getWritableFields(String fileName) throws IOException;

	abstract String getApplicationHelp();

	public final void writing(TelemetryWriter writer) throws IOException {
		try (CodecInputStream in = new CodecInputStream(
				new BufferedInputStream(Files.newInputStream(Paths.get(sourceFile))))) {
			TelemetryModel telemetryModel = TelemetryModel.loadFromTlm(in, tolerance);

			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_hhmmss");

			for (FlightKey flightKey : telemetryModel.getFlightKeys()) {
				FlightTelemetry telemetry = telemetryModel.getFlightTelemetry(flightKey);

				String fileName =
						flightKey.getVehicleName() +
						"-" +
						format.format(new Date(flightKey.getFlightStartTime())) +
						getExtension(writer);

				fileName = Paths.get(destinationDirectory, fileName).toString();
				FileWriter fileWriter = new FileWriter(fileName);

				writer.write(fileWriter, telemetry, getWritableFields(fieldsFile));
				fileWriter.close();
			}
		}
	}

	private String getExtension(Object object) {
		switch (object.getClass().getSimpleName()) {
			case "KmlTelemetryWriter":
				return ".kml";
			case "CsvTelemetryWriter":
				return ".csv";
			default:
				return null;
		}
	}
}