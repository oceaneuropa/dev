package com.osgi.example1.questions;

public class RegularExpressionMatching {

	/**
	 * 
	 * @param source
	 * @param pattern
	 * @return
	 */
	public static boolean isMatch(String source, String pattern) {
		if (source != null && source.equals(pattern)) {
			return true;
		}
		if (source == null || source.isEmpty() || pattern == null || pattern.isEmpty()) {
			return false;
		}
		// invalid if starts with '*' or have continuous '*'
		if (pattern.startsWith("*")) {
			throw new RuntimeException("Invalid pattern input at index [0].");
		}
		for (int i = 2; i < pattern.length(); i++) {
			if ('*' == pattern.charAt(i) && '*' == pattern.charAt(i - 1)) {
				throw new RuntimeException("Invalid pattern input at index [" + i + "].");
			}
		}

		return isMatch(source.toCharArray(), pattern.toCharArray(), 0, 0);
	}

	/**
	 * 
	 * @param source
	 * @param pattern
	 * @param sIndex
	 * @param pIndex
	 * @return
	 */
	public static boolean isMatch(char[] source, char[] pattern, int sIndex, int pIndex) {
		if (sIndex >= source.length) {
			// no more source
			if (pIndex >= pattern.length) {
				// no more pattern
				return true;
			} else if (pIndex < pattern.length && allStarPatterns(pattern, pIndex)) {
				// has more pattern
				return true;
			}
		}
		if (sIndex >= source.length || pIndex >= pattern.length) {
			return false;
		}

		char s = source[sIndex];
		char p = pattern[pIndex];

		boolean isFollowedByStar = isFollowedByStar(pattern, pIndex);
		boolean isEndOfSource = (sIndex == source.length - 1) ? true : false;
		// boolean isEndOfPattern = (pIndex == pattern.length - 1) ? true : false;
		// if (isFollowedByStar) {
		// isEndOfPattern = (pIndex == pattern.length - 2) ? true : false;
		// }

		if (!isFollowedByStar) {
			if (s == p || '.' == p) {
				// current source char matches current pattern char
				// compare next char in source with next char in pattern.
				return isMatch(source, pattern, sIndex + 1, pIndex + 1);

			} else {
				// source char doesn't match pattern char
				return false;
			}
		}

		// current pattern char is followed by '*'
		if (s == p || '.' == p) {
			// source char matches pattern char

			// check two cases:
			if (isMatch(source, pattern, sIndex, pIndex + 2)) {
				// pattern char repeat 0 times
				return true;
			}

			// pattern char repeat 1 or more times
			return isEndOfSource || isMatch(source, pattern, sIndex + 1, pIndex);

		} else {
			// source char doesn't match pattern char
			// pattern char repeat 0 times
			return isMatch(source, pattern, sIndex, pIndex + 2);
		}
	}

	/**
	 * 
	 * @param pattern
	 * @param pIndex
	 * @return
	 */
	protected static boolean isFollowedByStar(char[] pattern, int pIndex) {
		return (pIndex + 1 < pattern.length && '*' == pattern[pIndex + 1]) ? true : false;
	}

	/**
	 * 
	 * @param pattern
	 * @param pIndex
	 * @return
	 */
	protected static boolean allStarPatterns(char[] pattern, int pIndex) {
		if (pIndex >= pattern.length) {
			return false;
		}

		boolean allStarPatterns = false;
		for (int i = pIndex; i < pattern.length; i = i + 2) {
			if (i == pattern.length) {
				allStarPatterns = false;
				break;
			}
			char nextP = pattern[i + 1];
			if ('*' == nextP) {
				allStarPatterns = true;
			} else {
				allStarPatterns = false;
				break;
			}
		}
		return allStarPatterns;
	}

	public static void main(String[] args) {
		// boolean result1 = RegularExpressionMatching.isMatch("a", ".");
		// System.out.println("a|. -> " + result1);
		//
		// boolean result2 = RegularExpressionMatching.isMatch("aab", "a*b");
		// System.out.println("aab|a*b -> " + result2);

		boolean result3 = RegularExpressionMatching.isMatch("g", "g*a*b*");
		System.out.println("g|g*a*b* -> " + result3);
	}

}
