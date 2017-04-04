package org.origin.common.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

	public static char[] HEX_CHARS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	public static String EMPTY_STRING = "";

	/**
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean equals(String value1, String value2) {
		if (value1 == null && value2 == null) {
			return true;
		}
		if ((value1 != null && value1.equals(value2)) || (value2 != null && value2.equals(value1))) {
			return true;
		}
		return false;
	}

	/**
	 * Returns a string representation of the byte array as a series of hexadecimal characters.
	 *
	 * @see com.google.gwt.util.tools.shared.StringUtils
	 *
	 * @param bytes
	 *            byte array to convert
	 * @return a string representation of the byte array as a series of hexadecimal characters
	 */
	public static String toHexString(byte[] bytes) {
		char[] hexString = new char[2 * bytes.length];
		int j = 0;
		for (int i = 0; i < bytes.length; i++) {
			hexString[j++] = HEX_CHARS[(bytes[i] & 0xF0) >> 4];
			hexString[j++] = HEX_CHARS[bytes[i] & 0x0F];
		}
		return new String(hexString);
	}

	/**
	 * Remove starting characters from a string.
	 * 
	 * @param src
	 * @param c
	 * @return
	 */
	public static String removeStartingCharacters(String src, String c) {
		if (src != null) {
			if (src.isEmpty() || src.equals(c)) {
				return EMPTY_STRING;
			}
			while (src.startsWith(c)) {
				src = src.substring(c.length());
				if (src.isEmpty() || src.equals(c)) {
					return EMPTY_STRING;
				}
			}
		}
		return src;
	}

	/**
	 * Remove ending characters from a string.
	 * 
	 * @param src
	 * @param c
	 * @return
	 */
	public static String removeEndingCharacters(String src, String c) {
		if (src != null) {
			if (src.isEmpty() || src.equals(c)) {
				return EMPTY_STRING;
			}
			while (src.endsWith(c)) {
				src = src.substring(0, src.lastIndexOf(c));
				if (src.isEmpty() || src.equals(c)) {
					return EMPTY_STRING;
				}
			}
		}
		return src;
	}

	public static void main(String[] args) {
		List<String> partNamesList = new ArrayList<String>();
		// partNamesList.add("item");
		// partNamesList.add("item1");
		// partNamesList.add("item2");
		partNamesList.add("a");
		partNamesList.add("");
		// partNamesList.add("c");

		// String partNamesStr = "";
		// int i = 0;
		// for (Iterator<String> partItor = partNamesList.iterator(); partItor.hasNext();) {
		// String partName = partItor.next();
		// if (i > 0) {
		// partNamesStr += ",";
		// }
		// partNamesStr += partName;
		// i++;
		// }

		String partNamesStr = "";
		for (int i = 0; i < partNamesList.size(); i++) {
			String partName = partNamesList.get(i);
			if (i > 0) {
				partNamesStr += ",";
			}
			partNamesStr += partName;
		}

		System.out.println("partNamesStr = " + partNamesStr);

		String[] partNames = null;
		if (partNamesStr != null && !partNamesStr.isEmpty()) {
			partNames = partNamesStr.split(",");
		}

		if (partNames == null) {
			System.out.println("partNames is null");

		} else {
			System.out.println("partNames.length = " + partNames.length);
			for (String partName : partNames) {
				System.out.println("partName = " + partName);
			}
		}
	}

}
