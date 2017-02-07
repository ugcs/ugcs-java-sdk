package com.ugcs.telemetrytool;

import java.io.IOException;
import java.text.ParseException;

public final class Tlm2Csv extends TlmConverter{

	public Tlm2Csv(String[] args) throws ParseException {
		super(args);
	}

	public static void main(String[] args) throws IOException, ParseException {
		Tlm2Csv tlm2Csv = new Tlm2Csv(args);
		CsvTelemetryWriter writer = new CsvTelemetryWriter();
		tlm2Csv.write(writer);
	}

	@Override
	public void usage() {
		System.err.println(getApplicationHelp());
		System.exit(1);
	}

	@Override
	protected String getApplicationHelp() {
		return "tlm2csv [-h] [-t <seconds>] [-d <output dir>] [-l <fields>] -f <fileName>\n\n" +
				"Parameters:\n\n" +
				"-t, --tolerance : Tolerance, number of seconds between telemetry records that \n" +
				"                  should be interpreted as a separate flights. Default 60 seconds.\n" +
				"-f              : Path to the source .tlm file.\n" +
				"-d              : Path to the destination directory where to put output files.\n" +
				"                  Default is a current directory.\n" +
				"-l, --fields    : Additional file containing list of output fields. \n" +
				"-s, --start     : Start time interval for telemetry convert. \n" +
				"-e, --end       : End time interval for telemetry convert. \n" +
				"-h, --help      : Help, display this message.";
	}

	@Override
	String getExtension() {
		return "csv";
	}
}