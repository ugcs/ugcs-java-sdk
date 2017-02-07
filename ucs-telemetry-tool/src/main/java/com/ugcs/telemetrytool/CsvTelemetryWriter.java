package com.ugcs.telemetrytool;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ugcs.common.util.Preconditions;
import com.ugcs.common.util.value.AbstractValue;

public class CsvTelemetryWriter implements TelemetryWriter {

	private static final Set<String> ANGLE_SEMANTICS = new HashSet<>(
			Arrays.asList(new String[] {"LATITUDE", "LONGITUDE", "HEADING", "ROLL", "PITCH"})
	);

	@Override
	public void write(OutputStream out, FlightTelemetry telemetry, List<TelemetryKey> telemetryKeys) throws IOException{
		Preconditions.checkNotNull(out);
		Preconditions.checkNotNull(telemetry);
		Preconditions.checkNotNull(telemetryKeys);

		try(CsvWriter writer = new CsvWriter(out)) {
			List<String> header = new ArrayList<>(telemetryKeys.size() + 1);
			header.add("Time");
			for (TelemetryKey key : telemetryKeys) {
				// format
				String cell = key.format();
				header.add(cell);
			}

			writer.writeNext(header.toArray(new String[header.size()]));

			Map<TelemetryKey, AbstractValue> mostRecent = new HashMap<>();
			Date lastTime = null;

			Map<TelemetryKey, List<AbstractValue>> timeRows = new LinkedHashMap<>();

			for (TelemetryRecord telemetryRecord : telemetry.getRecords()) {
				Date currentTime = telemetryRecord.getValue().getTime();
				if (lastTime == null) {
					lastTime = currentTime;
				}

				if (!currentTime.equals(lastTime)) {
					// flush
					flush(timeRows, telemetryKeys, mostRecent, writer, lastTime);
					timeRows.clear();
				}

				// Collect without duplicates
				collect(timeRows, telemetryRecord);

				lastTime = currentTime;
			}

			if (!timeRows.isEmpty()) {
				// flush last rows
				flush(timeRows, telemetryKeys, mostRecent, writer, lastTime);
				timeRows.clear();
			}
		}
	}

	private void collect(Map<TelemetryKey, List<AbstractValue>> timeRows, TelemetryRecord telemetryRecord) {
		List<AbstractValue> timeRowsList = timeRows.get(telemetryRecord.getKey());
		if (timeRowsList == null) {
			timeRowsList = new ArrayList<>();
			timeRows.put(telemetryRecord.getKey(), timeRowsList);
		}
		if (timeRowsList.isEmpty() ||
				!timeRowsList.get(timeRowsList.size() - 1).equals(telemetryRecord.getValue().getValue())) {
			timeRowsList.add(telemetryRecord.getValue().getValue());
		}
	}

	private void flush(Map<TelemetryKey, List<AbstractValue>> timeRows,
									 List<TelemetryKey> telemetryKeys,
									 Map<TelemetryKey, AbstractValue> mostRecent,
									 CsvWriter writer,
					   				 Date time) throws IOException {
		int numRows = getMaxNumRows(timeRows);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SXXX");

		for (int i = 0; i < numRows; ++i) {
			List<String> row = new ArrayList<>(telemetryKeys.size() + 1);
			row.add(simpleDateFormat.format(time));

			for (TelemetryKey telemetryKey : telemetryKeys) {
				List<AbstractValue> values = timeRows.get(telemetryKey);
				if (values != null && values.size() > i) {
					mostRecent.put(telemetryKey, values.get(i));
				}
				AbstractValue value = mostRecent.get(telemetryKey);
				row.add(
						value != null && value.isAvailable()
								? ANGLE_SEMANTICS.contains(telemetryKey.getSemantic())
										? String.valueOf(Math.toDegrees(value.doubleValue()))
										: value.stringValue()
								: ""
				);
			}
			writer.writeNext(row.toArray(new String[row.size()]));
		}
	}

	private int getMaxNumRows(Map<TelemetryKey, List<AbstractValue>> timRowsMap) {
		int maxNumRows = 0;

		for (List<AbstractValue> abstractValueList : timRowsMap.values()) {
			int size = abstractValueList.size();
			if (size > maxNumRows)
				maxNumRows = size;
		}
		return maxNumRows;
	}
}