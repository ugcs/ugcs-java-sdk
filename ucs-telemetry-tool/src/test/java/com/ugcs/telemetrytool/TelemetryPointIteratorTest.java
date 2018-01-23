package com.ugcs.telemetrytool;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.ugcs.common.util.value.AbstractValue;

public class TelemetryPointIteratorTest {
	private static TelemetryKey[] KEYS = new TelemetryKey[] {
			TelemetryKey.create("longitude", "DEFAULT", "FLIGHT_CONTROLLER", 0),
			TelemetryKey.create("latitude", "DEFAULT", "FLIGHT_CONTROLLER", 0),
			TelemetryKey.create("altitude_amsl", "DEFAULT", "CONTROL_SERVER", 0),
			TelemetryKey.create("altitude_amsl", "DEFAULT", "FLIGHT_CONTROLLER", 0),
			TelemetryKey.create("random", "DEFAULT", "FLIGHT_CONTROLLER", 0),
	};

	private FlightTelemetry buildFlightTelemetry(long[][] values) {
		FlightTelemetry telemetry = new FlightTelemetry();

		for (long[] value : values) {
			if (value.length < 1)
				throw new IllegalArgumentException();

			Date t = new Date(value[0]);
			for (int j = 1; j < value.length; ++j) {
				if (value[j] == 0)
					continue;
				int keyIndex = j - 1;
				if (keyIndex >= KEYS.length)
					throw new IllegalArgumentException("Too few columns");
				telemetry.add(
						KEYS[keyIndex],
						TelemetryValue.create(value[j] != -1
								? AbstractValue.of(value[j])
								: AbstractValue.unavailable(), t));
			}
		}
		return telemetry;
	}

	private void checkIterator(long[][] values, long[][] expected) {
		// setup
		FlightTelemetry telemetry = buildFlightTelemetry(values);

		// run/execute
		TelemetryPointIterator iterator =
				new TelemetryPointIterator(telemetry.getRecords());

		// check
		int i = 0;
		while (iterator.hasNext()) {
			// check: expected has at least i rows
			Assert.assertTrue(i <= expected.length);

			// check: ith point equality
			TelemetryPoint point = iterator.next();
			long[] value = new long[]{
					(long) Math.toRadians(point.getLongitude()),
					(long) Math.toRadians(point.getLatitude()),
					(long) point.getAltitudeAmsl()};

			Assert.assertArrayEquals(value, expected[i]);
			i++;
		}
		// check: overall size equality
		Assert.assertEquals(expected.length, i);
	}

	@Test
	public void testEmptyInput() {
		checkIterator(
				// model
				new long[][] {
				},
				// expected
				new long[][] {
				}
		);
	}

	@Test
	public void testNoCoordinates() {
		checkIterator(
				// model
				new long[][] {
						{1, 0, 0, 0, 0, 1},
						{2, 0, 0, 0, 0, 2},
						{3, 0, 0, 0, 0, 3},
						{4, 0, 0, 0, 0, 4}
				},
				// expected
				new long[][] {
				}
		);
	}

	@Test
	public void testNotAvailable() {
		checkIterator(
				// model
				new long[][] {
						{1, -1, -1, -1},
				},
				// expected
				new long[][] {
				}
		);
	}

	@Test
	public void testNotAvailable2() {
		checkIterator(
				// model
				new long[][] {
						{1, -1, 0, 0},
						{2, 0, -1, 0},
						{3, 0, 0, -1},
				},
				// expected
				new long[][] {
				}
		);
	}

	@Test
	public void testNotAvailable3() {
		checkIterator(
				// model
				new long[][] {
						{1, 1, 2, 3},
						{2, 0, -1, 0},
						{3, 2, 0, -1},
						{3, -1, 4, 5},
				},
				// expected
				new long[][] {
						{1, 2, 3},
						{2, 4, 5},
				}
		);
	}

	@Test
	public void testInvalidSubsystem() {
		checkIterator(
				// model
				new long[][] {
						{1, 1, 0, 0, 0},
						{2, 0, 2, 0, 0},
						{3, 0, 0, 0, 3},
						{4, 4, 5, 0, 6}
				},
				// expected
				new long[][] {
				}
		);
	}

	@Test
	public void testMixedCoordinateDistribution() {
		checkIterator(
				// model
				new long[][] {
						{1, 1, 0, 0},
						{2, 0, 2, 0},
						{3, 0, 0, 3},
						{4, 4, 5, 6},
						{5, 0, 0, 0},
						{6, 0, 2, 0},
						{7, 0, 0, 3},
				},
				// expected
				new long[][]{
						{1, 2, 3},
						{4, 5, 6},
						{4, 2, 6},
						{4, 2, 3}
				}
		);
	}

	@Test
	public void testTimestampPointPriority() {
		checkIterator(
				// model
				new long[][] {
						{1, 1, 2, 3},
						{1, 2, 5, 4},
						{1, 3, 4, 5},
						{1, 4, 5, 6}
				},
				// expected
				new long[][] {
						{4, 5, 6},
				}
		);
	}

	@Test
	public void testTimestampPointPriority2() {
		checkIterator(
				// model
				new long[][] {
						{1, 1, 2, 3},
						{1, 2, 5, 4},
						{1, 3, 4, 5},
						{1, 4, 5, 6},
						{2, 1, 2, 3},
						{2, 2, 5, 4},
						{2, 3, 4, 5},
				},
				// expected
				new long[][] {
						{4, 5, 6},
						{3, 4, 5},
				}
		);
	}

	@Test
	public void testCollapse() {
		checkIterator(
				// model
				new long[][] {
						{1, 1, 2, 3},
						{2, 2, 0, 4},
						{3, 3, 4, 5},
						{4, 4, 5, 6}
				},
				// expected
				new long[][] {
						{1, 2, 3},
						{2, 2, 4},
						{3, 4, 5},
						{4, 5, 6}
				}
		);
	}

	@Test
	public void testCollapse2() {
		checkIterator(
				// model
				new long[][] {
						{1, 1, 0, 0},
						{2, 2, 0, 4},
						{3, 0, 4, 5},
						{4, 4, 5, 6}
				},
				// expected
				new long[][] {
						{2, 4, 5},
						{4, 5, 6}
				}
		);
	}

	@Test
	public void testCollapse3() {
		checkIterator(
				// model
				new long[][] {
						{1, 1, 0, 0},
						{2, 2, 0, 4},
						{3, 0, 0, 5},
						{4, 4, 0, 6}
				},
				// expected
				new long[][] {
				}
		);
	}

	@Test
	public void testCollapse4() {
		checkIterator(
				// model
				new long[][] {
						{1, 1, 0, 0},
						{2, 2, 0, 4},
						{2, 0, 3, 4},
						{3, 0, 0, 5},
						{4, 4, 0, 6},
						{4, 3, 2, 6},
						{4, 4, 3, 6},
				},
				// expected
				new long[][] {
						{2, 3, 4},
						{2, 3, 5},
						{4, 3, 6}
				}
		);
	}

	@Test
	public void testCollapse5() {
		checkIterator(
				// model
				new long[][] {
						{7, 1, 2, 0},
						{7, 2, 5, 0},
						{7, 3, 0, 0}
				},
				// expected
				new long[][] {
				}
		);
	}
}
