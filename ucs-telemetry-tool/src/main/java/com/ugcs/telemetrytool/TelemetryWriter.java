package com.ugcs.telemetrytool;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public interface TelemetryWriter {
	void write(FileWriter out, FlightTelemetry telemetry, List<String> fields) throws IOException;
}
