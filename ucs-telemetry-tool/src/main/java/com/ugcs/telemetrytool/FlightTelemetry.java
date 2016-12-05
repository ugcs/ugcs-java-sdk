package com.ugcs.telemetrytool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ugcs.common.util.Preconditions;

public class FlightTelemetry {
	private final List<TelemetryRecord> records;

	public FlightTelemetry() {
		records = new ArrayList<>();
	}

	public void add(TelemetryKey key, TelemetryValue value) {
		Preconditions.checkNotNull(key);
		Preconditions.checkNotNull(value);
		records.add(new TelemetryRecord(key, value));
	}

	public List<TelemetryRecord> getRecords() {
		return records;
	}

	public List<TelemetryKey> getTelemetryKeys(List<String> fields) throws IOException{
		Set<TelemetryKey> set = records.stream().map(TelemetryRecord::getKey).collect(Collectors.toSet());
		List<TelemetryKey> filterList = new LinkedList<>();
		if (fields != null) {
			for (String field : fields) {
				for (TelemetryKey key : set) {
					if (field.equals(TelemetryKey.getSubsystemAlias(key.getSubsystem()) + ":" + key.getCode())) {
						filterList.add(key);
						break;
					}
				}
			}

			return filterList;
		}

		filterList.addAll(set);
		return filterList;
	}
}