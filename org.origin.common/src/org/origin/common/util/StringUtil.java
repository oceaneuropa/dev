package org.origin.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StringUtil {

	public static char[] HEX_CHARS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	public static String EMPTY_STRING = "";

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String get(String value) {
		return get(value, "n/a");
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static String get(String value, String defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

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
	 * 
	 * @param list
	 * @return
	 */
	public static String toString(List<String> list) {
		String listString = null;
		if (list != null && !list.isEmpty()) {
			listString = "";
			for (int i = 0; i < list.size(); i++) {
				String str = list.get(i);
				if (i > 0) {
					listString += ",";
				}
				listString += str;
			}
		}
		return listString;
	}

	/**
	 * 
	 * @param listString
	 * @return
	 */
	public static List<String> toList(String listString) {
		List<String> list = new ArrayList<String>();
		if (listString != null && !listString.isEmpty()) {
			String[] strArray = listString.split(",");
			if (strArray != null) {
				for (String str : strArray) {
					list.add(str);
				}
			}
		}
		return list;
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

	public static void main2(String[] args) {
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

	/**
	 * 
	 * @param allStrings
	 * @param string
	 * @param appendUniqueToAll
	 * @return
	 */
	public static String getUniqueString(Set<String> allStrings, String string, boolean appendUniqueToAll) {
		String uniqueString = string;

		int endingNumberLength = getEndingNumberLength(string);

		String baseString = string;
		int endingNumber = 0;
		if (endingNumberLength > 0) {
			baseString = string.substring(0, (string.length() - endingNumberLength));
			endingNumber = getEndingNumber(string, endingNumberLength);
		}

		while (allStrings.contains(uniqueString)) {
			uniqueString = baseString + (++endingNumber);
		}

		if (appendUniqueToAll) {
			if (uniqueString != null && !allStrings.contains(uniqueString)) {
				allStrings.add(uniqueString);
			}
		}

		return uniqueString;
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	private static int getEndingNumberLength(String string) {
		int stringLength = string.length();

		int endingNumberLength = 0;
		while (endingNumberLength < stringLength) {
			try {
				int nextEndingNumberLength = endingNumberLength + 1;
				if (nextEndingNumberLength == stringLength) {
					break;
				}

				String numberStr = string.substring(stringLength - nextEndingNumberLength);
				Integer.parseInt(numberStr);
				endingNumberLength = nextEndingNumberLength;

			} catch (Exception e) {
				break;
			}
		}

		return endingNumberLength;
	}

	/**
	 * 
	 * @param string
	 * @param endingNumberLength
	 * @return
	 */
	private static int getEndingNumber(String string, int endingNumberLength) {
		if (endingNumberLength > 0) {
			try {
				String numberStr = string.substring(string.length() - endingNumberLength);
				return Integer.parseInt(numberStr);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

}
