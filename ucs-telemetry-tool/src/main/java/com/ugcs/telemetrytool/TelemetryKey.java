package com.ugcs.telemetrytool;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ugcs.common.util.Equals;
import com.ugcs.common.util.Preconditions;
import com.ugcs.common.util.Strings;

public class TelemetryKey {
	public static final String DEFAULT_SEMANTIC = "DEFAULT";
	public static final int DEFAULT_SUBSYSTEM_ID = 0;

	public static final Comparator<TelemetryKey> CODE_COMPARATOR = new CodeComparator();

	private static Map<String, String> SUBSYSTEM_ALIASES = newSubsystemAliases();

	private static Map<String, String> newSubsystemAliases() {
		Map<String, String> aliases = new HashMap<>();
		aliases.put("CONTROL_SERVER", "cs");
		aliases.put("FLIGHT_CONTROLLER", "fc");
		aliases.put("GIMBAL", "gb");
		aliases.put("CAMERA", "cam");
		aliases.put("ADSB_TRANSPONDER", "at");
		return aliases;
	}

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

	public String format() {
		String alias = SUBSYSTEM_ALIASES.get(subsystem);
		String str = (!Strings.isNullOrEmpty(alias) ? alias : subsystem)
				+ ":"
				+ code
				+ (subsystemId > DEFAULT_SUBSYSTEM_ID ? "@" + subsystemId : "");
		return str;
	}

	public static TelemetryKey parse(String str) {
		Pattern pattern = Pattern.compile("(?:(?<a>\\w+):)?(?<c>\\w+)(?:@(?<i>\\d+))?");
		Matcher matcher = pattern.matcher(str);

		boolean matches = matcher.matches();
		String alias = matches
				? matcher.group("a")
				: null;
		String code = matches
				? matcher.group("c")
				: null;
		String id = matches
				? matcher.group("i")
				: null;

		String subsystem = null;

		for (Map.Entry<String, String> entry : SUBSYSTEM_ALIASES.entrySet()) {
			if (entry.getValue().equals(alias)) {
				subsystem = entry.getKey();
				break;
			}
		}
		if (subsystem == null)
			subsystem = alias;

		return TelemetryKey.create(
				code,
				TelemetryKey.DEFAULT_SEMANTIC,
				subsystem,
				DEFAULT_SUBSYSTEM_ID);
	}

	public static Comparator<TelemetryKey> codeComparator() {
		return CODE_COMPARATOR;
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

	public boolean equalsByCode(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof TelemetryKey))
			return false;

		TelemetryKey other = (TelemetryKey) obj;
		return codeComparator().compare(this, other) == 0;
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

	static class CodeComparator implements Comparator<TelemetryKey> {

		@Override
		public int compare(TelemetryKey x, TelemetryKey y) {
			int cmp = x.subsystem.compareTo(y.subsystem);
			if (cmp == 0)
				cmp = x.code.compareTo(y.code);
			return cmp;
		}
	}
}