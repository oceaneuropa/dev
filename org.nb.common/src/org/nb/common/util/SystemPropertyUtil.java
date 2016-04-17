package org.nb.common.util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.osgi.framework.BundleContext;

public class SystemPropertyUtil {

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

}
