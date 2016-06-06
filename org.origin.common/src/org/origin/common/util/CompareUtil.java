package org.origin.common.util;

import java.util.Date;
import java.util.Map;

public class CompareUtil {

	/**
	 * 
	 * @param str1
	 * @param str2
	 * @param equalsIfBothNull
	 * @return
	 */
	public static boolean equals(String str1, String str2, boolean equalsIfBothNull) {
		if (equalsIfBothNull && str1 == null && str2 == null) {
			return true;
		}
		if (str1 != null && str2 != null && str1.equals(str2)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param integer1
	 * @param integer2
	 * @param equalsIfBothNull
	 * @return
	 */
	public static boolean equals(Integer integer1, Integer integer2, boolean equalsIfBothNull) {
		if (equalsIfBothNull && integer1 == null && integer2 == null) {
			return true;
		}
		if (integer1 != null && integer2 != null && integer1.equals(integer2)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param date1
	 * @param date2
	 * @param equalsIfBothNull
	 * @return
	 */
	public static boolean equals(Date date1, Date date2, boolean equalsIfBothNull) {
		if (equalsIfBothNull && date1 == null && date2 == null) {
			return true;
		}
		if (date1 != null && date2 != null && date1.equals(date2)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param map1
	 * @param map2
	 * @param equalsIfBothNull
	 * @return
	 */
	public static boolean equals(Map<?, ?> map1, Map<?, ?> map2, boolean equalsIfBothNull) {
		if (equalsIfBothNull && map1 == null && map2 == null) {
			return true;
		}
		if (map1 != null && map2 != null && map1.equals(map2)) {
			return true;
		}
		return false;
	}

}
