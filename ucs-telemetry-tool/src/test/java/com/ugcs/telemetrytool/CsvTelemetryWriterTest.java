package com.ugcs.telemetrytool;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.ugcs.common.csv.CsvReader;
import org.junit.Assert;
import org.junit.Test;

import com.ugcs.common.io.Bytes;
import com.ugcs.common.io.ArrayOutputStream;
import com.ugcs.common.util.value.AbstractValue;

public class CsvTelemetryWriterTest {

	private static TelemetryKey[] KEYS = new TelemetryKey[] {
			TelemetryKey.create("1", "DEFAULT", "CONTROL_SERVER", 0),
			TelemetryKey.create("2", "DEFAULT", "CONTROL_SERVER", 0),
			TelemetryKey.create("3", "DEFAULT", "CONTROL_SERVER", 0),
			TelemetryKey.create("4", "DEFAULT", "CONTROL_SERVER", 0),
			TelemetryKey.create("5", "DEFAULT", "CONTROL_SERVER", 0),
			TelemetryKey.create("6", "DEFAULT", "CONTROL_SERVER", 0),
	};


	private FlightTelemetry buildFlightTelemetry(long[][] values) {
		FlightTelemetry telemetry = new FlightTelemetry();

		for (int i = 0; i < values.length; ++i) {
			if (values[i].length < 1)
				throw new IllegalArgumentException();

			Date t = new Date(values[i][0]);
			for (int j = 1; j < values[i].length; ++j) {
				if (values[i][j] == 0)
					continue;
				int keyIndex = j - 1;
				if (keyIndex >= KEYS.length)
					throw new IllegalArgumentException("Too few columns");
				telemetry.add(KEYS[keyIndex], TelemetryValue.create(AbstractValue.of(values[i][j]), t));
			}
		}
		return telemetry;
	}

	private void checkCsv(InputStream in, long[][] expected) throws IOException, ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss.S");
		CsvReader csvReader = new CsvReader(new InputStreamReader(in));
		try {
			in.mark(655536);
			List<String> row = null;
			int rowIndex = 0;
			while ((row = csvReader.readFields()) != null) {
				if (row.isEmpty() || row.size() == 1 && row.get(0).isEmpty())
					continue;

				if (rowIndex > 0) {
					int expectedIndex = rowIndex - 1;
					Assert.assertTrue("Invalid expectedIndex: " + expectedIndex + ", but got " + expected.length,
							expectedIndex < expected.length);

					long[] expectedRow = expected[expectedIndex];
					long[] csvRow = new long[row.size()];
					csvRow[0] = simpleDateFormat.parse(row.get(0)).getTime();
					for (int i = 1; i < row.size(); ++i)
						csvRow[i] = Integer.parseInt(row.get(i));

					try {
						Assert.assertArrayEquals(expectedRow, csvRow);
					} catch (Throwable e) {
						in.reset();
						Bytes.copy(in, System.out);
						System.out.println("---");

						System.out.println("Csv:\t" + Arrays.toString(csvRow));
						System.out.println("Exp.:\t" + Arrays.toString(expectedRow));
						throw e;
					}
				}
				rowIndex++;
			}
			Assert.assertTrue("Invalid rows count expected: " + expected.length + ", but got " + (rowIndex - 1),
					(rowIndex - 1) == expected.length);
		} finally {
			csvReader.close();
		}
	}

	private void testCsvExport(long[][] values, long[][] expected) throws IOException, ParseException {
		// setup
		FlightTelemetry telemetry = buildFlightTelemetry(values);

		// run/execute

		ArrayOutputStream out = new ArrayOutputStream();
		CsvTelemetryWriter writer = new CsvTelemetryWriter();

		Set<TelemetryKey> keySet = new HashSet<>();
		for (TelemetryRecord record : telemetry.getRecords())
			keySet.add(record.getKey());
		List<TelemetryKey> keys = new ArrayList<>(keySet);
		Collections.sort(keys, TelemetryKey.codeComparator());

		writer.write(out, telemetry, keys);

		// check
		checkCsv(out.getInputStream(), expected);
	}

	private void testCsvExport2(long[][] values, long[][] expected, List<String> fields) throws IOException, ParseException {

		// setup
		FlightTelemetry telemetry = buildFlightTelemetry(values);

		// run/execute

		ArrayOutputStream out = new ArrayOutputStream();
		CsvTelemetryWriter writer = new CsvTelemetryWriter();

		Set<TelemetryKey> telemetryKeys = new LinkedHashSet<>();
		for (String field : fields) {
			// key parse
			TelemetryKey fieldKey = TelemetryKey.parse(field);
			for (TelemetryRecord record : telemetry.getRecords()) {
				if (fieldKey.equalsByCode(record.getKey())) {
					telemetryKeys.add(record.getKey());
				}
			}
		}

		writer.write(out, telemetry, new ArrayList<>(telemetryKeys));

		// check
		checkCsv(out.getInputStream(), expected);
	}

	@Test
	public void testSingleLineWriteOrdered() throws IOException, ParseException {
		testCsvExport2(
				// model
				new long[][] {
						{1, 2, 3, 4, 5, 6},
						{1, 2, 3, 4, 5, 6},
						{1, 2, 3, 4, 5, 6},
				},
				// expected csv
				new long[][] {
						{1, 6, 4, 2, 5, 3},
				},
				Arrays.asList("cs:5", "cs:3", "cs:1", "cs:4", "cs:2")
		);
	}

	@Test
	public void testSingleLineWrite() throws IOException, ParseException {
		testCsvExport(
				// model
				new long[][] {
						{1, 2, 3, 4, 5, 6},
				},
				// expected csv
				new long[][] {
						{1, 2, 3, 4, 5, 6},
				}
		);
	}

	@Test
	public void testSimpleNoCollapse() throws IOException, ParseException {
		testCsvExport(
				// model
				new long[][] {
						{1, 2, 3, 4, 5},
						{2, 2, 1, 4, 5},
						{3, 2, 3, 4, 5},
						{4, 2, 3, 2, 5},
						{5, 2, 3, 4, 5},
						{6, 2, 3, 4, 1},
						{7, 3, 3, 4, 5},
				},
				// expected csv
				new long[][] {
						{1, 2, 3, 4, 5},
						{2, 2, 1, 4, 5},
						{3, 2, 3, 4, 5},
						{4, 2, 3, 2, 5},
						{5, 2, 3, 4, 5},
						{6, 2, 3, 4, 1},
						{7, 3, 3, 4, 5},
				}
		);
	}


	@Test
	public void testCollapse() throws IOException, ParseException {
		testCsvExport(
				// model
				new long[][] {
						{1, 0, 14, 0},
						{1, 23, 0, 0},
						{1, 12, 15, 56},
				},
				// expected csv
				new long[][] {
						{1, 23, 14, 56},
						{1, 12, 15, 56},
				}
		);
	}

	@Test
	public void testCollapseSimple() throws IOException, ParseException {
		testCsvExport(
				// model
				new long[][] {
						{1, 23, 0, 0},
						{1, 0, 15, 0},
						{1, 0, 0, 56},
				},
				// expected csv
				new long[][] {
						{1, 23, 15, 56},
				}
		);
	}

	@Test
	public void testCollapseSimple2() throws IOException, ParseException {
		testCsvExport(
				// model
				new long[][] {
						{1, 23, 0, 0},
						{1, 0, 15, 0},
						{1, 0, 0, 56},
						{2, 1, 1, 1},
				},
				// expected csv
				new long[][] {
						{1, 23, 15, 56},
						{2, 1, 1, 1},
				}
		);
	}

	@Test
	public void testEmptyRows() throws IOException, ParseException {
		testCsvExport(
				// model
				new long[][] {
						{1, 0, 0, 0},
						{1, 0, 0, 0},
						{1, 0, 0, 0},
				},
				// expected csv
				new long[][] {
				}
		);
	}
}