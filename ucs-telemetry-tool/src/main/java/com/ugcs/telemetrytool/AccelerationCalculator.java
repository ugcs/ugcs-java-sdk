package com.ugcs.telemetrytool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ugcs.common.csv.CsvReader;
import com.ugcs.common.csv.CsvWriter;

public final class AccelerationCalculator {

	private AccelerationCalculator() {
	}

	private static String getApplicationHelp() {
		return "AccelerationCalculator [-h] [-t <seconds>] [-d <output dir>] [-l <fields>] -f <fileName>\n\n"
				+ "Parameters:\n\n"
				+ "-f              : Path to the source .tlm file.\n"
				+ "-d              : Path to the destination directory where to put output files.\n"
				+ "                  Default is a current directory.\n"
				+ "-l, --fields    : Additional file containing list of output fields. \n"
				+ "-h, --help      : Help, display this message.";
	}

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println(AccelerationCalculator.getApplicationHelp());
			System.exit(1);
		}

		String sourceFile = null;
		String destinationDirectory = new File("").getAbsolutePath();

		for (int i = 0; i < args.length; ++i) {
			switch (args[i]) {
				case "-f":
					sourceFile = args[i + 1];
					break;
				case "-d":
					destinationDirectory = args[i + 1];
					break;
				case "-h":
				case "--help":
					System.out.println(AccelerationCalculator.getApplicationHelp());
					break;
				default:
					throw new IllegalArgumentException();
			}
			i++;
		}

		if (sourceFile == null || destinationDirectory == null) {
			throw new IllegalArgumentException();
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SXXX");
		List<Double> timeSpeedValues = null;

		List<String> headers;
		List<String[]> rows;
		try (CsvReader csvReader = new CsvReader(Files.newBufferedReader(Paths.get(sourceFile)))) {
			headers = new ArrayList<>(csvReader.readFields());
			for (int i = 0; i < headers.size(); ++i) {
				int k = headers.get(i).indexOf(":");
				if (k != -1)
					headers.set(i, headers.get(i).substring(k + 1));
			}

			rows = new ArrayList<>();
			List<String> row;
			while ((row = csvReader.readFields()) != null)
				rows.add(row.toArray(new String[row.size()]));
		}

		headers.add("ground_acceleration");
		headers.add("vertical_acceleration");
		headers.add("component_acceleration");

		int groundSpeedFieldPos = headers.indexOf("ground_speed");
		int verticalSpeedFieldPos = headers.indexOf("vertical_speed");
		if (verticalSpeedFieldPos == -1) {
			verticalSpeedFieldPos = headers.indexOf("ground_speed_z");
		}
		if (groundSpeedFieldPos == -1 || verticalSpeedFieldPos == -1) {
			System.exit(2);
		}

		String targetFile = sourceFile.substring(0, sourceFile.lastIndexOf(".")) + "-acceleration.csv";
		try (CsvWriter csvWriter = new CsvWriter(Files.newBufferedWriter(Paths.get(targetFile)))) {
			csvWriter.writeFields(headers.toArray(new String[headers.size()]));
			for (String[] nextLine : rows) {
				double currentTime = 0.0;
				double groundSpeed = !nextLine[groundSpeedFieldPos].isEmpty()
						? Double.valueOf(nextLine[groundSpeedFieldPos])
						: Double.NaN;
				double verticalSpeed = !nextLine[verticalSpeedFieldPos].isEmpty()
						? Double.valueOf(nextLine[verticalSpeedFieldPos])
						: Double.NaN;

				int newLineCapacity = nextLine.length + 3;
				String[] newLine = new String[newLineCapacity];
				System.arraycopy(nextLine, 0, newLine, 0, nextLine.length);

				if (groundSpeed == -1 || verticalSpeed == -1) {
					newLine[newLineCapacity - 1] = newLine[newLineCapacity - 2] = newLine[newLineCapacity - 3] = "";
					csvWriter.writeFields(newLine);
					continue;
				}

				try {
					currentTime = (double)simpleDateFormat.parse(nextLine[0]).getTime();
				} catch (ParseException e) {
					System.out.println(e.getMessage());
					System.exit(1);
				}
				if (timeSpeedValues == null) {
					timeSpeedValues = Arrays.asList(
							currentTime,
							groundSpeed,
							verticalSpeed
					);
					continue;
				}

				double deltaTime = currentTime - timeSpeedValues.get(0);
				double verticalAcceleration = (verticalSpeed - timeSpeedValues.get(2)) / deltaTime;
				double groundAcceleration = (groundSpeed - timeSpeedValues.get(1)) / deltaTime;
				double componentAcceleration = Math.sqrt(Math.pow(verticalAcceleration, 2)
						+ Math.pow(groundAcceleration, 2));

				newLine[newLineCapacity - 1] = !Double.isNaN(componentAcceleration)
						? String.valueOf(componentAcceleration)
						: "";
				newLine[newLineCapacity - 2] = !Double.isNaN(verticalAcceleration)
						? String.valueOf(verticalAcceleration)
						: "";
				newLine[newLineCapacity - 3] = !Double.isNaN(groundAcceleration)
						? String.valueOf(groundAcceleration)
						: "";
				csvWriter.writeFields(newLine);
			}
		}
	}
}