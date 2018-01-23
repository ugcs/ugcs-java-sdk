package com.ugcs.telemetrytool;

import com.ugcs.common.util.Preconditions;

public class TelemetryRecord {

	private final TelemetryKey key;
	private final TelemetryValue value;

	public TelemetryRecord(TelemetryKey key, TelemetryValue value) {
		Preconditions.checkNotNull(key);
		Preconditions.checkNotNull(value);

		this.key = key;
		this.value = value;
	}

	public TelemetryKey getKey() {
		return key;
	}

	public TelemetryValue getValue() {
		return value;
	}
}