package com.ugcs.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Strings {
	public static final String EMPTY_STRING = "";

	// Predefined strings used for logging:
	public static final String NL = Strings.getLineSeparator();
	public static final String TAB = "\t";
	public static final String NL1T = NL + TAB;
	public static final String NL2T = NL1T + TAB;
	public static final String NL3T = NL2T + TAB;

	private Strings() {
		// forbidden
	}

	public static String nullToEmpty(String string) {
		return string == null ? Strings.EMPTY_STRING : string;
	}

	public static String emptyToNull(String string) {
		return isNullOrEmpty(string) ? null : string;
	}

	public static boolean isNullOrEmpty(String string) {
		return string == null || string.isEmpty();
	}

	public static String getLineSeparator() {
		String lineSeparator = System.getProperty("line.separator");
		if (Strings.isNullOrEmpty(lineSeparator))
			lineSeparator = "\n";
		return lineSeparator;
	}

	public static String padLeft(String source, int targetLength, char padChar) {
		return padLeft(source, targetLength, padChar, false);
	}

	public static String padLeft(String source, int targetLength, char padChar, boolean truncate) {
		if (source.length() < targetLength) {
			StringBuilder builder = new StringBuilder(targetLength);
			for (int i = source.length(); i < targetLength; ++i)
				builder.append(padChar);
			builder.append(source);
			return builder.toString();
		}
		return truncate
				? truncateWithDots(source, targetLength)
				: source;
	}

	public static String padRight(String source, int targetLength, char padChar) {
		return padRight(source, targetLength, padChar, false);
	}

	public static String padRight(String source, int targetLength, char padChar, boolean truncate) {
		if (source.length() < targetLength) {
			StringBuilder builder = new StringBuilder(targetLength);
			builder.append(source);
			for (int i = source.length(); i < targetLength; ++i)
				builder.append(padChar);
			return builder.toString();
		}
		return truncate
				? truncateWithDots(source, targetLength)
				: source;
	}

	private static String truncateWithDots(String text, int targetLength) {
		if (text.length() <= targetLength)
			return text;
		String dots = targetLength == 1
				? "."
				: "..";
		return text.substring(0, targetLength - dots.length()) + dots;
	}

	public static int findIndexOf(String source, boolean ignoreCase, String... variants) {
		boolean isEmptySource = isNullOrEmpty(source);
		if (variants != null && variants.length > 0) {
			for (int index = 0; index < variants.length; index++) {
				String variant = variants[index];
				if (isEmptySource) {
					if (isNullOrEmpty(variant))
						return index;
				} else {
					if (ignoreCase && source.equalsIgnoreCase(variant))
						return index;
					if (!ignoreCase && source.equals(variant))
						return index;
				}
			}
		}
		return -1;
	}

	public static boolean isEqualOneOf(String source, boolean ignoreCase, String... variants) {
		if (isNullOrEmpty(source) && (variants == null || variants.length == 0))
			return true;
		return findIndexOf(source, ignoreCase, variants) != -1;
	}

	public static String[] split(String source, String regex) {
		if (Strings.isNullOrEmpty(source))
			return new String[0];
		if (Strings.isNullOrEmpty(regex))
			return new String[]{source};

		String[] split = source.split(regex);
		if (split == null || split.length == 0)
			return split;
		List<String> result = new ArrayList<>();
		for (String item : split)
			if (!Strings.isNullOrEmpty(item))
				result.add(item);
		return result.toArray(new String[result.size()]);
	}

	public static String[] collectNotEmpty(String... items) {
		if (items == null || items.length == 0)
			return null;
		Set<String> result = new HashSet<>();
		for (String item : items) {
			if (!Strings.isNullOrEmpty(item)) {
				item = item.trim();
				if (item.length() > 0)
					result.add(item);
			}
		}
		return result.toArray(new String[result.size()]);
	}

	public static String trim(String source, String... trimedVariants) {
		if (isNullOrEmpty(source))
			return source;
		if (trimedVariants == null || trimedVariants.length == 0)
			return source;

		String result = source;
		for (String trimedVariant : trimedVariants) {
			if (isNullOrEmpty(trimedVariant))
				continue;
			int trimLen = trimedVariant.length();
			int resultLen = 0;
			while (resultLen != result.length()) {
				resultLen = result.length();
				if (result.startsWith(trimedVariant))
					result = result.substring(trimLen);
				if (result.endsWith(trimedVariant))
					result = result.substring(0, resultLen - trimLen);
			}
		}
		return result;
	}

	public static String exceptionToStirng(Throwable error, String usedNewLine) {
		if (error == null)
			return Strings.EMPTY_STRING;

		StringWriter errWriter = new StringWriter();
		error.printStackTrace(new PrintWriter(errWriter));
		String result = errWriter.toString();

		if (!Strings.isNullOrEmpty(usedNewLine) && !NL.equals(usedNewLine))
			result = result.replace(Strings.NL, usedNewLine);
		return result;
	}

	public static boolean hasDigitsOnly(String string) {
		if (isNullOrEmpty(string))
			return false;
		for (char c : string.toCharArray())
			if (!Character.isDigit(c))
				return false;
		return true;
	}

	public static void requireNotEmpty(String string, String message) {
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException(message);
		}
	}

	//Convert SCREAMING_SNAKE_CASE string dto camelCase
	public static String screamingSnakeCaseToCamelCase(String screamingSnakeCase) {
		StringBuilder camelCaseString = new StringBuilder();
		boolean undescoreFlag = false;
		for (int i = 0; i < screamingSnakeCase.length(); i++) {
			char character = screamingSnakeCase.charAt(i);
			if (character == '_')
				undescoreFlag = true;
			else {
				camelCaseString.append(undescoreFlag ? character : Character.toLowerCase(character));
				undescoreFlag = false;
			}
		}
		return camelCaseString.toString();
	}
}
