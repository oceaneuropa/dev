package org.origin.common.util;

import java.util.List;

public class StatUtil {

	public static int min(int... n) {
		int min = Integer.MAX_VALUE;
		for (int v : n) {
			if (v < min) {
				min = v;
			}
		}
		return min;
	}

	public static Integer min(List<Integer> n) {
		int min = Integer.MAX_VALUE;
		for (int v : n) {
			if (v < min) {
				min = v;
			}
		}
		return min;
	}

	public static int max(int... n) {
		int max = Integer.MIN_VALUE;
		for (int v : n) {
			if (v > max) {
				max = v;
			}
		}
		return max;
	}

	public static Integer max(List<Integer> n) {
		int max = Integer.MIN_VALUE;
		for (int v : n) {
			if (v > max) {
				max = v;
			}
		}
		return max;
	}

	public static long sum(int... n) {
		long sum = 0;
		for (int v : n) {
			sum += v;
		}
		return sum;
	}

	public static long sum(List<Integer> n) {
		long sum = 0;
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
		long sum = sum(n);
		double avg = sum / size;
		double avgWithScale = BigDecimalUtil.rounding(avg, scale);
		return avgWithScale;
	}

	public static double avg(int scale, List<Integer> n) {
		int size = n.size();
		long sum = sum(n);
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
