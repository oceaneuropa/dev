package com.osgi.example1.util;

import java.io.File;
import java.util.Map;

public class PropertyUtil {

	/**
	 * Retrieve a property as a string.
	 *
	 * @param properties
	 *            the properties to retrieve the value from
	 * @param propertyName
	 *            the name of the property to retrieve
	 * @param defaultValue
	 *            the default value
	 * @return the property as a boolean or the default value
	 */
	public static String getProperty(Map<String, String> properties, String propertyName, String defaultValue) {
		String value = properties.get(propertyName);
		if (value != null) {
			return value;
		}
		return defaultValue;
	}

	/**
	 * Retrieve a property as a boolean.
	 *
	 * @param properties
	 *            the properties to retrieve the value from
	 * @param propertyName
	 *            the name of the property to retrieve
	 * @param defaultValue
	 *            the default value
	 * @return the property as a boolean or the default value
	 */
	public static boolean getBoolean(Map<String, String> properties, String propertyName, boolean defaultValue) {
		String value = properties.get(propertyName);
		if (value != null) {
			return Boolean.valueOf(value);
		}
		return defaultValue;
	}

	/**
	 * Retrieve a property as a long.
	 *
	 * @param properties
	 *            the properties to retrieve the value from
	 * @param propertyName
	 *            the name of the property to retrieve
	 * @param defaultValue
	 *            the default value
	 * @return the property as a long or the default value
	 */
	public static int getInt(Map<String, String> properties, String propertyName, int defaultValue) {
		String value = properties.get(propertyName);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}

	/**
	 * Retrieve a property as a long.
	 *
	 * @param properties
	 *            the properties to retrieve the value from
	 * @param propertyName
	 *            the name of the property to retrieve
	 * @param defaultValue
	 *            the default value
	 * @return the property as a long or the default value
	 */
	public static long getLong(Map<String, String> properties, String propertyName, long defaultValue) {
		String value = properties.get(propertyName);
		if (value != null) {
			try {
				return Long.parseLong(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}

	/**
	 * Retrieve a property as a File.
	 *
	 * @param properties
	 *            the properties to retrieve the value from
	 * @param propertyName
	 *            the name of the property to retrieve
	 * @param defaultFile
	 *            the default value
	 * @return the property as a File or the default value
	 */
	public static File getFile(Map<String, String> properties, String propertyName, File defaultFile) {
		String value = properties.get(propertyName);
		if (value != null) {
			return new File(value);
		}
		return defaultFile;
	}

}
