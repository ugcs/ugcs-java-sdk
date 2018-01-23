package com.ugcs.common.util;

import java.util.Arrays;

public final class Equals {
	private Equals() {
		// forbidden
	}
	
	public static boolean equals(boolean a, boolean b) {
		return a == b;
	}

	public static boolean equals(char a, char b) {
		return a == b;
	}

	public static boolean equals(long a, long b) {
		// byte, short and int are handled by this method 
		// through implicit conversion
		return a == b;
	}

	public static boolean equals(float a, float b) {
		return Float.floatToIntBits(a) == Float.floatToIntBits(b);
	}

	public static boolean equals(double a, double b) {
		return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
	}

	public static boolean equals(byte[] a, byte[] b) {
		return Arrays.equals(a, b);
	}

	public static boolean equals(short[] a, short[] b) {
		return Arrays.equals(a, b);
	}

	public static boolean equals(int[] a, int[] b) {
		return Arrays.equals(a, b);
	}

	public static boolean equals(long[] a, long[] b) {
		return Arrays.equals(a, b);
	}

	public static boolean equals(char[] a, char[] b) {
		return Arrays.equals(a, b);
	}

	public static boolean equals(float[] a, float[] b) {
		return Arrays.equals(a, b);
	}

	public static boolean equals(double[] a, double[] b) {
		return Arrays.equals(a, b);
	}

	public static boolean equals(boolean[] a, boolean[] b) {
		return Arrays.equals(a, b);
	}

	public static boolean equals(Object a, Object b) {
		if (a == b)
            return true;
        if (a == null || b == null)
            return false;
        
        boolean result = false;
		if (a.getClass().isArray() && b.getClass().isArray()) {
			if (a instanceof Object[] && b instanceof Object[])
				result = Arrays.deepEquals((Object[])a, (Object[])b);
			else if (a instanceof byte[] && b instanceof byte[])
				result = equals((byte[])a, (byte[])b);
			else if (a instanceof short[] && b instanceof short[])
				result = equals((short[])a, (short[])b);
			else if (a instanceof int[] && b instanceof int[])
				result = equals((int[])a, (int[])b);
			else if (a instanceof long[] && b instanceof long[])
				result = equals((long[])a, (long[])b);
			else if (a instanceof char[] && b instanceof char[])
				result = equals((char[])a, (char[])b);
			else if (a instanceof float[] && b instanceof float[])
				result = equals((float[])a, (float[])b);
			else if (a instanceof double[] && b instanceof double[])
				result = equals((double[])a, (double[])b);
			else if (a instanceof boolean[] && b instanceof boolean[])
				result = equals((boolean[])a, (boolean[])b);
		} else {
			result = a.equals(b);
		}
		return result;
	}
}
