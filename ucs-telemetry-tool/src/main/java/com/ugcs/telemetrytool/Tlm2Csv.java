package com.ugcs.telemetrytool;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public final class Tlm2Csv extends TlmConverter{

	public Tlm2Csv(String[] args) {
		super(args);
	}

	public static void main(String[] args) throws IOException {
		Tlm2Csv tlm2Csv = new Tlm2Csv(args);
		CsvTelemetryWriter writer = new CsvTelemetryWriter();
		tlm2Csv.writing(writer);
	}

	@Override
	public void usage() {
		System.err.println(getApplicationHelp());
		System.exit(1);
	}

	@Override
	public List<String> getWritableFields(String fileName) throws IOException{
		if (fileName == null) {
			return null;
		} else if ((new File(fileName)).exists()) {
			List<String> fields = new LinkedList<>(Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8));
			if (fields.isEmpty()) {
				return null;
			}
			fields.removeIf(p -> p.startsWith("#"));
			return fields;
		} else {
			return null;
		}
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
				"-h, --help      : Help, display this message.";
	}
}