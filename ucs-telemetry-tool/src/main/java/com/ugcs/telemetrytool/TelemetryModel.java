package com.ugcs.telemetrytool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ugcs.common.util.Preconditions;
import com.ugcs.common.util.codec.CodecInputStream;

public class TelemetryModel {

	private final Map<FlightKey, FlightTelemetry> data;

	private TelemetryModel(CodecInputStream in, int tolerance) throws IOException {
		data = TelemetryReader.read(in, tolerance);
	}

	public static TelemetryModel loadFromTlm(CodecInputStream in, int tolerance) throws IOException{
		Preconditions.checkNotNull(in);
		return new TelemetryModel(in, tolerance);
	}

	public List<FlightKey> getFlightKeys() {
		return new ArrayList<>(data.keySet());
	}

	public FlightTelemetry getFlightTelemetry(FlightKey key) {
		Preconditions.checkNotNull(key);
		return data.get(key);
	}
}
