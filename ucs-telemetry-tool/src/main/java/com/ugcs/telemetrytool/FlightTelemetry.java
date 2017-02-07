package com.ugcs.telemetrytool;

import java.util.ArrayList;
import java.util.List;

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
}