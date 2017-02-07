package com.ugcs.telemetrytool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ugcs.common.util.Preconditions;
import com.ugcs.common.util.codec.CodecInputStream;

public class TelemetryModel {

	private final Map<FlightKey, FlightTelemetry> data;

	private TelemetryModel(CodecInputStream in,
						   int tolerance,
						   Date intervalStartTime,
						   Date intervalEndTime) throws IOException {
		data = TelemetryReader.read(in, tolerance, intervalStartTime, intervalEndTime);
	}

	public static TelemetryModel loadFromTlm(CodecInputStream in,
											 int tolerance,
											 Date intervalStartTime,
											 Date intervalEndTime) throws IOException{
		Preconditions.checkNotNull(in);
		return new TelemetryModel(in, tolerance, intervalStartTime, intervalEndTime);
	}

	public List<FlightKey> getFlightKeys() {
		List<FlightKey> keyList = new ArrayList<>();
		for (FlightKey key : data.keySet()) {
			if (!data.get(key).getRecords().isEmpty())
				keyList.add(key);
		}
		return keyList;
	}

	public FlightTelemetry getFlightTelemetry(FlightKey key) {
		Preconditions.checkNotNull(key);
		return data.get(key);
	}
}
