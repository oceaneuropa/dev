package org.nb.common.util;

public class StringUtil {

	public static boolean equals(String value1, String value2) {
		if ((value1 != null && value1.equals(value2)) || (value2 != null && value2.equals(value1))) {
			return true;
		}
		if (value1 == null && value2 == null) {
			return true;
		}
		return false;
	}

}
