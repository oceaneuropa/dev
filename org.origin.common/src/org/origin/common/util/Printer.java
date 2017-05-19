package org.origin.common.util;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Printer {

	/**
	 * 
	 * @param message
	 */
	public static void pl(String message) {
		System.out.println(message);
	}

	/**
	 * 
	 * @param props
	 */
	public static void pl(Map<?, ?> props) {
		System.out.println("------------------------------------------------------------------------");
		if (props == null) {
			System.out.println("null");

		} else {
			Map<Object, Object> treeMap = new TreeMap<Object, Object>();
			for (Entry<?, ?> entry : props.entrySet()) {
				Object key = entry.getKey();
				Object value = entry.getValue();
				treeMap.put(key, value);
			}
			props = treeMap;

			for (Entry<?, ?> entry : props.entrySet()) {
				Object key = entry.getKey();
				Object value = entry.getValue();
				String type = value.getClass().getSimpleName();
				System.out.println(key + " = " + entry.getValue() + " (" + type + ")");
			}
		}
		System.out.println("------------------------------------------------------------------------");
	}

	/**
	 * 
	 * @param dict
	 */
	public static void pl(Dictionary<String, ?> dict) {
		System.out.println("------------------------------------------------------------------------");
		if (dict == null) {
			System.out.println("null");
		} else {
			for (Enumeration<String> keyItor = dict.keys(); keyItor.hasMoreElements();) {
				String key = keyItor.nextElement();
				Object value = dict.get(key);
				System.out.println(key + " = " + value.toString());
			}
		}
		System.out.println("------------------------------------------------------------------------");
	}

	/**
	 * 
	 * @param objects
	 * @return
	 */
	public static String toString(Iterator<?> itor) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int count = 0;
		while (itor.hasNext()) {
			if (count > 0) {
				sb.append(", ");
			}
			sb.append(itor.next());
			count++;
		}
		sb.append("]");
		return sb.toString();
	}

}
