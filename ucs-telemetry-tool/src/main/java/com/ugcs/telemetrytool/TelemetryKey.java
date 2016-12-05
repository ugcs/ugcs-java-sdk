package com.ugcs.telemetrytool;

import com.ugcs.common.util.Equals;
import com.ugcs.common.util.Preconditions;
import com.ugcs.common.util.Strings;

public class TelemetryKey {
	public static final String DEFAULT_SEMANTIC = "DEFAULT";
	public static final int DEFAULT_SUBSYSTEM_ID = 0;

	private final String code;
	private final String semantic;
	private final String subsystem;
	private final int subsystemId;

	private TelemetryKey(String code, String semantic, String subsystem, int subsystemId) {
		Preconditions.checkNotNull(code);
		Preconditions.checkNotNull(semantic);
		Preconditions.checkNotNull(subsystem);

		this.code = code;
		this.semantic = semantic;
		this.subsystem = subsystem;
		this.subsystemId = subsystemId;
	}

	public static TelemetryKey create(String code, String semantic, String subsystem, int subsystemId) {
		return new TelemetryKey(code, semantic, subsystem, subsystemId);
	}

	public String getCode() {
		return code;
	}

	public String getSemantic() {
		return semantic;
	}

	public String getSubsystem() {
		return subsystem;
	}

	public int getSubsystemId() {
		return subsystemId;
	}

	public static String getSubsystemAlias(String subsystem) {
		switch (subsystem) {
			case "CONTROL_SERVER" :
				return "cs";
			case "FLIGHT_CONTROLLER" :
				return "fc";
			case "GIMBAL" :
				return "gb";
			case "CAMERA" :
				return "cam";
			case "ADSB_TRANSPONDER" :
				return "at";
			default:
				return null;
		}
	}

	@Override
	public int hashCode() {
		int h = 17;
		h = 31 * h + (code != null ? code.hashCode() : 0);
		h = 31 * h + (semantic != null ? semantic.hashCode() : 0);
		h = 31 * h + (subsystem != null ? subsystem.hashCode() : 0);
		h = 31 * h + subsystemId;
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof TelemetryKey))
			return false;

		TelemetryKey other = (TelemetryKey) obj;
		return Equals.equals(code, other.code)
				&& Equals.equals(semantic, other.semantic)
				&& Equals.equals(subsystem, other.subsystem)
				&& Equals.equals(subsystemId, other.subsystemId);
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("<tlm> ")
				.append(Strings.nullToEmpty(code))
				.append(" @ ")
				.append(subsystem)
				.append("#")
				.append(subsystemId)
				.toString();
	}
}
