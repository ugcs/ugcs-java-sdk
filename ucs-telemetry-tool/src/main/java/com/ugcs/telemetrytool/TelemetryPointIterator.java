package com.ugcs.telemetrytool;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TelemetryPointIterator implements Iterator<TelemetryPoint> {

	private final List<TelemetryRecord> records;
	private int pos; // index of current iterable element
	private double lastLongitude;
	private double lastLatitude;
	private double lastAltitude;
	private Date lastTime;

	public TelemetryPointIterator(List<TelemetryRecord> records) {
		this.records = records;
		this.lastTime = null;
		this.lastLongitude = this.lastLatitude = this.lastAltitude = Double.NaN;
	}

	private boolean isFullPoint() {
		return (!Double.isNaN(lastLongitude) && !Double.isNaN(lastLatitude) && !Double.isNaN(lastAltitude));
	}

	@Override
	public boolean hasNext() {
		boolean hasLongitude = false;
		boolean hasLatitude = false;
		boolean hasAltitude = false;
		for (int i = pos; i < records.size(); ++i) {
			String subsystem = records.get(i).getKey().getSubsystem();
			String code = records.get(i).getKey().getCode();
			if (records.get(i).getValue().getValue().isAvailable()) {
				if (subsystem.equals("FLIGHT_CONTROLLER")) {
					if (code.equals("longitude"))
						hasLongitude = true;
					else if (code.equals("latitude"))
						hasLatitude = true;
				} else if (subsystem.equals("CONTROL_SERVER") && code.equals("altitude_amsl"))
					hasAltitude = true;
			}
		}
		return (hasLongitude && hasLatitude && hasAltitude)
				|| ((hasLongitude || hasLatitude || hasAltitude) && isFullPoint());
	}

	@Override
	public TelemetryPoint next() {
		double longitude = lastLongitude;
		double latitude = lastLatitude;
		double altitude = lastAltitude;
		for (; pos < records.size(); ++pos) {
			Date currentTime = records.get(pos).getValue().getTime();
			if (lastTime == null)
				lastTime = currentTime;

			if (!lastTime.equals(currentTime) && isFullPoint()
					&& (longitude != lastLongitude || latitude != lastLatitude || altitude != lastAltitude)) {
				lastTime = currentTime;
				break;
			}

			TelemetryKey key = records.get(pos).getKey();
			TelemetryValue value = records.get(pos).getValue();
			if (value.getValue() != null && value.getValue().isAvailable()) {
				switch (key.getCode()) {
					case "longitude":
						if (key.getSubsystem().equals("FLIGHT_CONTROLLER"))
							lastLongitude = Math.toDegrees(value.getValue().doubleValue());
						break;
					case "latitude":
						if (key.getSubsystem().equals("FLIGHT_CONTROLLER"))
							lastLatitude = Math.toDegrees(value.getValue().doubleValue());
						break;
					case "altitude_amsl":
						if (key.getSubsystem().equals("CONTROL_SERVER"))
							lastAltitude = value.getValue().doubleValue();
						break;
				}
			}
			lastTime = currentTime;
		}
		return TelemetryPoint.create(lastLongitude, lastLatitude, lastAltitude);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}