package org.origin.common.util;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.osgi.framework.BundleContext;

public class PropertyUtil {

	public static void printSystemProperties() {
		System.out.println("System Properties:");
		System.out.println("----------------------------------------------------------------------------------------");
		Properties props = System.getProperties();
		for (Iterator<Entry<Object, Object>> entryItor = props.entrySet().iterator(); entryItor.hasNext();) {
			Entry<Object, Object> entry = entryItor.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			System.out.println(key + "=" + value);
		}
		System.out.println("----------------------------------------------------------------------------------------");
	}

	public static void printSystemEnvironmentVariables() {
		System.out.println("System Environment Variables:");
		System.out.println("----------------------------------------------------------------------------------------");
		Map<String, String> envs = System.getenv();
		for (Iterator<Entry<String, String>> entryItor = envs.entrySet().iterator(); entryItor.hasNext();) {
			Entry<String, String> entry = entryItor.next();
			String key = entry.getKey();
			String value = entry.getValue();
			System.out.println(key + "=" + value);
		}
		System.out.println("----------------------------------------------------------------------------------------");
	}

	/**
	 * Get property by key from BundleContext. If not found, get property by key from System property.
	 * 
	 * Then set the property value to the given properties table.
	 * 
	 * @param key
	 * @param bundleContext
	 * @param props
	 */
	public static void loadProperty(BundleContext bundleContext, Hashtable<String, Object> props, String key) {
		// load property from BundelContext
		String value = bundleContext.getProperty(key);

		// load property from system property
		if (value == null) {
			value = System.getProperty(key);
		}

		// load property from environment variable
		if (value == null) {
			value = System.getenv(key);
		}

		if (value != null) {
			props.put(key, value);
		}
	}

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
