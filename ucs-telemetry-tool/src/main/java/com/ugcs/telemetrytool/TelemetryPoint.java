package com.ugcs.telemetrytool;

public class TelemetryPoint {
	private final double longitude;
	private final double latitude;
	private final double altitudeAmsl;

	public TelemetryPoint(double longitude, double latitude, double altitudeAmsl) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitudeAmsl = altitudeAmsl;
	}

	public static TelemetryPoint create(double longitude, double latitude, double altitudeAmsl) {
		return new TelemetryPoint(longitude, latitude, altitudeAmsl);
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getAltitudeAmsl() {
		return altitudeAmsl;
	}
}