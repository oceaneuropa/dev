package org.origin.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtil {

	public static BigDecimal toBigDecimal(int i) {
		return toBigDecimal(i, 0, RoundingMode.CEILING);
	}

	public static BigDecimal toBigDecimal(int i, int scale, RoundingMode roundingMode) {
		BigDecimal value = new BigDecimal(i);
		value = value.setScale(scale, roundingMode);
		return value;
	}

	public static BigDecimal toBigDecimal(long l) {
		return toBigDecimal(l, 0, RoundingMode.CEILING);
	}

	public static BigDecimal toBigDecimal(long l, int scale, RoundingMode roundingMode) {
		BigDecimal value = new BigDecimal(l);
		value = value.setScale(scale, roundingMode);
		return value;
	}

	public static BigDecimal toBigDecimal(double d) {
		return toBigDecimal(d, 2, RoundingMode.CEILING);
	}

	public static BigDecimal toBigDecimal(double d, int scale, RoundingMode roundingMode) {
		BigDecimal value = new BigDecimal(d);
		value = value.setScale(scale, roundingMode);
		return value;
	}

	public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
		return multiply(a, b, 2, RoundingMode.CEILING);
	}

	public static BigDecimal multiply(BigDecimal a, BigDecimal b, int scale, RoundingMode roundingMode) {
		if (a == null) {
			return b;
		}
		if (b == null) {
			return a;
		}
		BigDecimal result = a.multiply(b);
		result = result.setScale(scale, roundingMode);
		return result;
	}

}
