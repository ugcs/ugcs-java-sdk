package com.ugcs.telemetrytool;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface TelemetryWriter {

	void write(OutputStream out, FlightTelemetry telemetry, List<TelemetryKey> telemetryKeys) throws IOException;
}
