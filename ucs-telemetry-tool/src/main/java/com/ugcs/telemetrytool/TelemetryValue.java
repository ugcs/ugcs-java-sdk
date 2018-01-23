package com.ugcs.telemetrytool;

import java.util.Date;

import com.ugcs.common.util.Preconditions;
import com.ugcs.common.util.value.AbstractValue;

public final class TelemetryValue {

	private final AbstractValue value;
	private final Date time;

	private TelemetryValue(AbstractValue value, Date time) {
		Preconditions.checkNotNull(value);
		Preconditions.checkNotNull(time);

		this.value = value;
		this.time = time;
	}

	public static TelemetryValue create(AbstractValue value, Date time) {
		return new TelemetryValue(value, time);
	}

	public AbstractValue getValue() {
		return value;
	}

	public Date getTime() {
		return time;
	}
}
