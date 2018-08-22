package com.ugcs.telemetrytool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ugcs.common.util.Preconditions;
import com.ugcs.common.util.codec.CodecInputStream;
import com.ugcs.common.util.value.AbstractValue;

public final class TelemetryReader {

	private static final int FORMAT_SIGNATURE = -655442754; // "UCS TELEMETRY".hashCode()
	private static final int FORMAT_VERSION = 1;
	private static final int FORMAT_END_OF_TELEMETRY = Integer.MIN_VALUE;

	private TelemetryReader() {
	}

	public static Map<FlightKey, FlightTelemetry> read(
			CodecInputStream in, int tolerance, Date intervalStartTime, Date intervalEndTime)
			throws IOException {
		Preconditions.checkNotNull(in);

		Map<FlightKey, FlightTelemetry> model = new HashMap<>();

		int signature = in.readInt();
		if (signature != FORMAT_SIGNATURE) {
			throw new IllegalArgumentException("Invalid format signature");
		}

		int version = in.readInt();
		if (version != FORMAT_VERSION) { // nothing else to support currently
			throw new IllegalArgumentException("Invalid version");
		}

		while (!eosReached(in)) {

			Map<String, String> attributes = readAttributes(in);

			String vehicleName = attributes.get("vehicle.name");

			FlightKey flightKey = null;
			FlightTelemetry flightTelemetry = null;

			TelemetryKey telemetryKey = null;
			TelemetryValue telemetryValue = null;

			Map<Integer, TelemetryKey> fieldKeys = new HashMap<>();

			Set<TelemetryKey> openedKeys = new HashSet<>();
			Date lastValueTime = null;
			Long flightStartTime = null;

			while (!eosReached(in)) {
				int fieldIndex = in.readVarInt();
				if (fieldIndex == FORMAT_END_OF_TELEMETRY) {
					// end of telemetry block
					break;
				}

				// unknown index is followed by the field type
				telemetryKey = fieldKeys.get(fieldIndex);
				if (telemetryKey == null) {
					String code = in.readVarString();
					String semantic = in.readVarString();
					String subsystem = in.readVarString();
					int subsystemId = in.readVarInt();

					telemetryKey = TelemetryKey.create(
							code,
							semantic,
							subsystem,
							subsystemId);
					fieldKeys.put(fieldIndex, telemetryKey);
				} else {
					// telemetry value
					Date time = new Date(in.readVarLong());

					byte[] bytes = in.readVarBytes();
					try {
						telemetryValue = TelemetryValue.create(
								AbstractValue.fromBytes(bytes),
								time);
					} catch (IllegalArgumentException e) {
						System.err.println("WARNING: " + e.getMessage());
						continue;
					}

					if (flightStartTime == null)
						flightStartTime = telemetryValue.getTime().getTime();
					flightKey = new FlightKey(vehicleName, flightStartTime);

					if (telemetryValue.getValue().isAvailable()) {
						if (openedKeys.isEmpty()) {
							if (lastValueTime == null || new Date(lastValueTime.getTime() + tolerance * 1000)
									.before(telemetryValue.getTime())) {

								if (flightTelemetry != null) {
									model.put(flightKey, flightTelemetry);
									flightTelemetry = null;
								}
								// new flight
							}
						}

						if (flightTelemetry == null)
							flightTelemetry = new FlightTelemetry();
						if ((intervalStartTime == null || intervalEndTime == null)
								|| (time.after(intervalStartTime) && time.before(intervalEndTime))) {
							flightTelemetry.add(telemetryKey, telemetryValue);
						}

						openedKeys.add(telemetryKey);
						lastValueTime = telemetryValue.getTime();
					} else {
						openedKeys.remove(telemetryKey);
					}
				}
			}
			// last flight
			if (flightTelemetry != null) {
				model.put(flightKey, flightTelemetry);
			}
		}
		return model;
	}

	private static Map<String, String> readAttributes(CodecInputStream in) throws IOException {
		Preconditions.checkNotNull(in);

		Map<String, String> attributes = new HashMap<>();
		while (true) {
			String attributeKey = in.readNullableVarString();
			if (attributeKey == null)
				break;
			String attributeValue = in.readVarString();
			attributes.put(attributeKey, attributeValue);
		}
		return attributes;
	}

	private static boolean eosReached(InputStream in) throws IOException {
		if (!in.markSupported()) {
			throw new IllegalArgumentException("The stream must support the mark() method.");
		}

		in.mark(1);
		boolean result = -1 == in.read();
		in.reset();
		return result;
	}
}