package com.ugcs.telemetrytool;

import com.ugcs.common.util.Preconditions;

public class FlightKey {

	private final String vehicleName;
	private final long flightStartTime;

	public FlightKey(String vehicleName, long flightStartTime) {
		Preconditions.checkNotNull(vehicleName);
		this.vehicleName = vehicleName;
		this.flightStartTime = flightStartTime;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public long getFlightStartTime() {
		return flightStartTime;
	}
}