package org.origin.common.util;

public class StatUtil {

	public static int sum(int... n) {
		int sum = 0;
		for (int v : n) {
			sum += v;
		}
		return sum;
	}

	public static float sum(float... f) {
		float sum = 0;
		for (float v : f) {
			sum += v;
		}
		return sum;
	}

	public static double sum(double... d) {
		double sum = 0;
		for (double v : d) {
			sum += v;
		}
		return sum;
	}

	public static double avg(int scale, int... n) {
		int size = n.length;
		int sum = sum(n);
		double avg = sum / size;
		double avgWithScale = BigDecimalUtil.rounding(avg, scale);
		return avgWithScale;
	}

	/**
	 * 
	 * @param subNum
	 * @param baseNum
	 * @param multiplyWith
	 * @return
	 */
	public static int normalizeByPercentage(int subNum, int baseNum, int multiplyWith) {
		double ratio = (double) subNum / (double) baseNum;
		double ratioWithScale2 = BigDecimalUtil.rounding(ratio);
		return (int) (ratioWithScale2 * multiplyWith);
	}

}
