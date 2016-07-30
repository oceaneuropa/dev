package org.origin.common.util;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.osgi.framework.BundleContext;

public class PropertyUtil {

	/**
	 * Get property by key from BundleContext. If not found, get property by key from System property.
	 * 
	 * Then set the property value to the given properties table.
	 * 
	 * @param name
	 * @param bundleContext
	 * @param props
	 */
	public static void loadProperty(BundleContext bundleContext, Map<Object, Object> props, String name) {
		loadProperty(bundleContext, props, name, null);
	}

	/**
	 * 
	 * @param bundleContext
	 * @param props
	 * @param name
	 * @param propClass
	 */
	public static void loadProperty(BundleContext bundleContext, Map<Object, Object> props, String name, Class<?> propClass) {
		// load property from BundelContext
		String stringValue = bundleContext.getProperty(name);

		// load property from system property
		if (stringValue == null) {
			stringValue = System.getProperty(name);
		}

		// load property from environment variable
		if (stringValue == null) {
			stringValue = System.getenv(name);
		}

		if (stringValue == null) {
			System.err.println("PropertyUtil.loadProperty() cannot find property value for '" + name + "'.");
			return;
		}

		if (propClass != null) {
			Object propValue = null;
			Exception conversionException = null;
			if (Date.class.isAssignableFrom(propClass)) {
				// Convert string into Date
				try {
					Date dateValue = DateUtil.toDate(stringValue, DateUtil.getCommonDateFormats());
					if (dateValue != null) {
						propValue = dateValue;
					}
				} catch (Exception e) {
					conversionException = e;
				}

			} else if (Boolean.class.isAssignableFrom(propClass)) {
				// Convert string into Boolean
				try {
					Boolean booleanValue = Boolean.valueOf(stringValue);
					if (booleanValue != null) {
						propValue = booleanValue;
					}
				} catch (Exception e) {
					conversionException = e;
				}

			} else if (Float.class.isAssignableFrom(propClass)) {
				// Convert string into Float
				try {
					Float floatValue = Float.valueOf(stringValue);
					if (floatValue != null) {
						propValue = floatValue;
					}
				} catch (Exception e) {
					conversionException = e;
				}

			} else if (Double.class.isAssignableFrom(propClass)) {
				// Convert string into Double
				try {
					Double floatValue = Double.valueOf(stringValue);
					if (floatValue != null) {
						propValue = floatValue;
					}
				} catch (Exception e) {
					conversionException = e;
				}

			} else if (Long.class.isAssignableFrom(propClass)) {
				// Convert string into Long
				try {
					Long longValue = Long.valueOf(stringValue);
					if (longValue != null) {
						propValue = longValue;
					}
				} catch (Exception e) {
					conversionException = e;
				}

			} else if (Integer.class.isAssignableFrom(propClass)) {
				// Convert string into Integer
				try {
					Integer integerValue = Integer.valueOf(stringValue);
					if (integerValue != null) {
						propValue = integerValue;
					}
				} catch (Exception e) {
					conversionException = e;
				}
			}

			if (propValue != null) {
				props.put(name, propValue);

			} else {
				System.err.println("PropertyUtil.loadProperty() cannot convert '" + stringValue + "' to " + propClass.getName() + ". " + (conversionException != null ? conversionException.getMessage() : ""));
				conversionException.printStackTrace();
				props.put(name, stringValue);
			}

		} else {
			props.put(name, stringValue);
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
	public static String getString(Map<?, ?> properties, String propertyName, String defaultValue) {
		Object value = properties.get(propertyName);
		if (value != null) {
			return value.toString();
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
	public static boolean getBoolean(Map<?, ?> properties, String propertyName, boolean defaultValue) {
		Object value = properties.get(propertyName);
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		if (value != null) {
			return Boolean.valueOf(value.toString());
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
	public static int getInt(Map<?, ?> properties, String propertyName, int defaultValue) {
		Object value = properties.get(propertyName);
		if (value instanceof Integer) {
			return (Integer) value;
		}
		if (value != null) {
			try {
				return Integer.parseInt(value.toString());
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
	public static long getLong(Map<?, ?> properties, String propertyName, long defaultValue) {
		Object value = properties.get(propertyName);
		if (value instanceof Long) {
			return (Long) value;
		}
		if (value != null) {
			try {
				return Long.parseLong(value.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}

	/**
	 * Retrieve a property as a float.
	 * 
	 * @param properties
	 *            the properties to retrieve the value from
	 * @param propertyName
	 *            the name of the property to retrieve
	 * @param defaultValue
	 *            the default value
	 * @return
	 */
	public static float getFloat(Map<?, ?> properties, String propertyName, float defaultValue) {
		Object value = properties.get(propertyName);
		if (value instanceof Float) {
			return (Float) value;
		}
		if (value != null) {
			try {
				return Float.parseFloat(value.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}

	/**
	 * Retrieve a property as a double.
	 * 
	 * @param properties
	 *            the properties to retrieve the value from
	 * @param propertyName
	 *            the name of the property to retrieve
	 * @param defaultValue
	 *            the default value
	 * @return
	 */
	public static double getDouble(Map<?, ?> properties, String propertyName, double defaultValue) {
		Object value = properties.get(propertyName);
		if (value instanceof Double) {
			return (Double) value;
		}
		if (value != null) {
			try {
				return Double.parseDouble(value.toString());
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
	public static File getFile(Map<?, ?> properties, String propertyName, File defaultFile) {
		Object value = properties.get(propertyName);
		if (value instanceof File) {
			return (File) value;
		}
		if (value != null) {
			return new File(value.toString());
		}
		return defaultFile;
	}

	private static void printSystemProperties() {
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

	private static void printSystemEnvironmentVariables() {
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
	 * Get property value.
	 * 
	 * @param typeName
	 * @param valueString
	 * @return
	 */
	public static Object getPropertyValue(String typeName, String valueString) {
		if (valueString == null) {
			return valueString;
		}
		Object value = null;
		if ("string".equals(typeName)) {
			value = valueString;
		} else if ("boolean".equals(typeName)) {
			try {
				value = Boolean.parseBoolean(valueString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("int".equals(typeName)) {
			try {
				value = Integer.parseInt(valueString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("long".equals(typeName)) {
			try {
				value = Long.parseLong(valueString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("float".equals(typeName)) {
			try {
				value = Float.parseFloat(valueString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("double".equals(typeName)) {
			try {
				value = Double.parseDouble(valueString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("date".equals(typeName)) {
			long time = -1;
			try {
				time = Long.parseLong(valueString);
			} catch (Exception e) {
				time = -1;
			}
			if (time >= 0) {
				value = DateUtil.toDate(time);
			} else {
				value = DateUtil.toDate(valueString, DateUtil.getCommonDateFormats());
			}
		}
		if (value == null) {
			value = valueString;
		}
		return value;
	}

}
