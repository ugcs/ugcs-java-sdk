package com.ugcs.telemetrytool;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class Tlm2Kml extends TlmConverter {

	public Tlm2Kml(String[] args) {
		super(args);
	}

	public static void main(String[] args) throws IOException {
		Tlm2Kml tlm2Kml = new Tlm2Kml(args);
		KmlTelemetryWriter writer = new KmlTelemetryWriter();
		tlm2Kml.writing(writer);
	}

	@Override
	public void usage() {
		System.err.println(getApplicationHelp());
		System.exit(1);
	}

	@Override
	public List<String> getWritableFields(String fileName) throws IOException{
		return Arrays.asList("fc:latitude", "fc:longitude", "fc:altitude_amsl");
	}

	@Override
	protected String getApplicationHelp() {
		return "tlm2kml [-h] [-t <seconds>] [-d <output dir>] -f <fileName>\n\n" +
				"Parameters:\n\n" +
				"-t, --tolerance : Tolerance, number of seconds between telemetry records that \n" +
				"                  should be interpreted as a separate flights. Default 60 seconds.\n" +
				"-f              : Path to the source .tlm file.\n" +
				"-d              : Path to the destination directory where to put output files.\n" +
				"                  Default is a current directory.\n" +
				"-h, --help      : Help, display this message.";
	}
}