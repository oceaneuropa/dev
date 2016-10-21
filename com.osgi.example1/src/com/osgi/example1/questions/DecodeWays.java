package com.osgi.example1.questions;

public class DecodeWays {

	public static int numDecodings(String s) {
		if (s == null || s.isEmpty()) {
			return 0;
		}

		int prevCount = 1;
		int count = 1;

		for (int i = 0; i < s.length(); i++) {
			int newCount = 0;

			// one char matching
			int oneChar = Integer.parseInt(s.substring(i, i + 1));
			if (1 <= oneChar && oneChar <= 9) {
				newCount = count;
			}

			// two char matching
			if (i > 0) {
				int twoChar = Integer.parseInt(s.substring(i - 1, i + 1));
				if (10 <= twoChar && twoChar <= 26) {
					newCount += prevCount;
				}
			}

			if (newCount == 0) {
				return 0;
			}

			prevCount = count;
			count = newCount;
		}

		return count;
	}

	public static void main(String[] args) {
		// int result1 = DecodeWays.numDecodings("12"); // 2
		// System.out.println("12 -> " + result1);

		int result2 = DecodeWays.numDecodings("123"); // 3
		System.out.println("123 -> " + result2);

		int result4 = DecodeWays.numDecodings("1224"); // 5
		System.out.println("1224 -> " + result4);

		int result3 = DecodeWays.numDecodings("123456789"); // 3
		System.out.println("123456789 -> " + result3);

		int result5 = DecodeWays.numDecodings("12221521"); // 26
		System.out.println("12221521 -> " + result5);
	}

}
