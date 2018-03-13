package org.orbit.platform.runtime.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommonModelHelper {

	public static CommonModelHelper INSTANCE = new CommonModelHelper();

	/**
	 * Convert string array to a map. The string is in the format of "{key1}={value1},{key2}={value2},...,{keyN}={valueN}"
	 * 
	 * @param string
	 * @return
	 */
	public Map<Object, Object> toMap(String string) {
		if (string == null || string.isEmpty()) {
			return null;
		}
		Map<Object, Object> parametersMap = new LinkedHashMap<Object, Object>();
		String[] segments = null;
		if (string.contains(";")) {
			// multiple segments in the string, separated by ';'
			segments = string.split(";");
		} else if (string.contains(",")) {
			// multiple segments in the string, separated by ','
			segments = string.split(",");
		} else {
			// single segment in the string
			segments = new String[] { string };
		}
		if (segments != null && segments.length > 0) {
			parametersMap = toMap(segments);
		}
		return parametersMap;
	}

	/**
	 * Convert string array to a map. Each string in the array is in the format of "{key}={value}".
	 * 
	 * @param strings
	 * @return
	 */
	public static Map<Object, Object> toMap(String... strings) {
		if (strings == null || strings.length == 0) {
			return null;
		}
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		for (String string : strings) {
			String key = null;
			Object value = null;
			int index = string.indexOf("=");
			if (index > 0 && index < string.length() - 1) {
				String part1 = string.substring(0, index);
				String part2 = string.substring(index + 1);
				key = part1 != null ? part1.trim() : null;
				value = part2 != null ? part2.trim() : null;
			}
			map.put(key, value);
		}
		return map;
	}

}
