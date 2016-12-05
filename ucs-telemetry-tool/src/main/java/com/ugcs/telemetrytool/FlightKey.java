package com.ugcs.telemetrytool;

import com.ugcs.common.util.Preconditions;

public class FlightKey {
	private final String vehicleName;
	private final long flightStartTime;
	private final long flightDuration;

	public FlightKey(String vehicleName, long flightStartTime, long flightDuration) {
		Preconditions.checkNotNull(vehicleName);
		this.vehicleName = vehicleName;
		this.flightStartTime = flightStartTime;
		this.flightDuration = flightDuration;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public long getFlightStartTime() {
		return flightStartTime;
	}
}