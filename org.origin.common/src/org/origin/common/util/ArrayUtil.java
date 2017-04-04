package org.origin.common.util;

import java.util.Arrays;
import java.util.List;

public class ArrayUtil {

	/**
	 * Convert int array into string with specified separator.
	 * 
	 * @param data
	 * @param separator
	 * @return
	 */
	public static String toArrayString(int[] data, String separator) {
		String arrayStr = "";
		for (int i = 0; i < data.length; i++) {
			if (i > 0) {
				arrayStr += separator;
			}
			arrayStr += data[i];
		}
		return arrayStr;
	}

	/**
	 * Convert string with specified separator into int array.
	 * 
	 * @param arrayStr
	 * @param separator
	 * @return
	 */
	public static int[] toArray(String arrayStr, String separator) {
		if (arrayStr == null || arrayStr.isEmpty()) {
			return new int[0];
		}
		List<String> strList = Arrays.asList(arrayStr.split(separator));
		int[] data = new int[strList.size()];
		for (int i = 0; i < strList.size(); i++) {
			String str = strList.get(i);
			data[i] = Integer.parseInt(str);
		}
		return data;
	}

}
