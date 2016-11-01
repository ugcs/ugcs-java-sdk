package com.ugcs.common.util;

public final class Preconditions {
	
	private Preconditions() {
	}
	
	public static void checkNotNull(Object reference) {
		if (reference == null)
			throw new NullPointerException();
	}

	public static void checkNotEmpty(String value) {
		if (value == null || value.isEmpty())
			throw new IllegalArgumentException();
	}

	public static void checkArgument(boolean condition) {
		if (!condition)
			throw new IllegalArgumentException();
	}

	public static void checkIndex(int index, int arrayLength) {
		if (index < 0 || index >= arrayLength)
			throw new IndexOutOfBoundsException();
	}

	public static void checkIndexRange(int offset, int length, int arrayLength) {
		if (offset < 0 || length < 0 || offset + length > arrayLength)
			throw new IndexOutOfBoundsException();
	}
}
