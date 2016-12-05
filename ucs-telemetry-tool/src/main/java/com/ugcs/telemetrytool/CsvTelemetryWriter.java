package com.ugcs.telemetrytool;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ugcs.common.util.Preconditions;
import com.ugcs.common.util.value.AbstractValue;

public class CsvTelemetryWriter implements TelemetryWriter {

	@Override
	public void write(FileWriter out, FlightTelemetry telemetry, List<String> fields) throws IOException{
		Preconditions.checkNotNull(out);
		Preconditions.checkNotNull(telemetry);

		try(CsvWriter writer = new CsvWriter(out)) {
			List<TelemetryKey> telemetryKeys = telemetry.getTelemetryKeys(fields);

			int n = telemetryKeys.size() + 1;

			List<String> header = new ArrayList<>(n);
			header.add("Time");
			for (TelemetryKey key: telemetryKeys) {
				StringBuilder cell = new StringBuilder()
						.append(TelemetryKey.getSubsystemAlias(key.getSubsystem()))
						.append(":")
						.append(key.getCode())
						.append(key.getSubsystemId() > 0 ? key.getSubsystemId() : "");
				header.add(cell.toString());
			}

			writer.writeNext(header.toArray(new String[header.size()]));

			Map<TelemetryKey, AbstractValue> mostRecent = new HashMap<>();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:Ss.sss");

			for (TelemetryRecord telemetryRecord : telemetry.getRecords()) {
				mostRecent.put(
						telemetryRecord.getKey(),
						telemetryRecord.getValue().getValue());

				List<String> row = new ArrayList<>(n);
				row.add(simpleDateFormat.format(telemetryRecord.getValue().getTime()));

				for (TelemetryKey telemetryKey : telemetryKeys) {
					AbstractValue value = mostRecent.get(telemetryKey);
					row.add(
							value != null
							? (telemetryKey.getCode().equals("longitude")
							|| telemetryKey.getCode().equals("latitude")
							|| telemetryKey.getCode().equals("pitch")
							|| telemetryKey.getCode().equals("roll")
							|| telemetryKey.getCode().equals("heading")
							|| telemetryKey.getCode().equals("course")
							? String.valueOf(value.doubleValue() * 180 / Math.PI)
							: value.stringValue())
							: ""
					);
				}
				writer.writeNext(row.toArray(new String[row.size()]));
			}
		}
	}
}