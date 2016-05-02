package org.origin.common.util;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {

	protected static final String PROPERTIES_SCHEMA_NAME = "__schema__";

	/**
	 * Convert Properties to JSONObject.
	 * 
	 * @param properties
	 * @return
	 */
	public static JSONObject toJson(Properties properties) {
		JSONObject jsonObject = new JSONObject();
		for (Enumeration<String> namesItor = (Enumeration<String>) properties.propertyNames(); namesItor.hasMoreElements();) {
			String propName = namesItor.nextElement();
			String propValue = properties.getProperty(propName);
			if (propValue == null) {
				propValue = "";
			}
			jsonObject.put(propName, propValue);
		}
		return jsonObject;
	}

	/**
	 * 
	 * @param properties
	 * @param createPropertySchema
	 * @return
	 */
	public static JSONObject toJsonObject(Map<String, Object> properties, boolean createPropertySchema) {
		JSONObject jsonObject = new JSONObject();
		for (Iterator<Entry<String, Object>> entryItor = properties.entrySet().iterator(); entryItor.hasNext();) {
			Entry<String, Object> entry = entryItor.next();
			String propName = entry.getKey();
			Object propValue = entry.getValue();
			if (propValue == null) {
				propValue = "";
			}
			jsonObject.put(propName, propValue);
		}

		if (createPropertySchema) {
			JSONObject schemaJsonObject = new JSONObject();
			for (Iterator<Entry<String, Object>> entryItor = properties.entrySet().iterator(); entryItor.hasNext();) {
				Entry<String, Object> entry = entryItor.next();
				String propName = entry.getKey();
				Object propValue = entry.getValue();
				if (propValue instanceof Date) {
					schemaJsonObject.put(propName, Date.class.getName());
				} else if (propValue instanceof Boolean) {
					schemaJsonObject.put(propName, Boolean.class.getName());
				} else if (propValue instanceof Float) {
					schemaJsonObject.put(propName, Float.class.getName());
				} else if (propValue instanceof Double) {
					schemaJsonObject.put(propName, Double.class.getName());
				} else if (propValue instanceof Long) {
					schemaJsonObject.put(propName, Long.class.getName());
				} else if (propValue instanceof Integer) {
					schemaJsonObject.put(propName, Integer.class.getName());
				} else if (propValue instanceof String) {
					schemaJsonObject.put(propName, String.class.getName());
				} else {
					// if not recognized, serialize it as String
					schemaJsonObject.put(propName, String.class.getName());
				}
			}
			jsonObject.put(PROPERTIES_SCHEMA_NAME, schemaJsonObject.toString());
		}

		return jsonObject;
	}

	public static Map<String, Object> toProperties1(String content) {
		Map<String, Object> properties = new LinkedHashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, ?> values = mapper.readValue(content, Map.class);
			if (values != null) {
				for (Iterator<String> namesItor = values.keySet().iterator(); namesItor.hasNext();) {
					String propName = namesItor.next();
					Object propValue = values.get(propName);
					if (propValue != null) {
						properties.put(propName, propValue);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * 
	 * @param content
	 * @param loadByPropertiesSchema
	 * @return
	 */
	public static Map<String, Object> toProperties(String content, boolean loadByPropertiesSchema) {
		Map<String, Object> properties = new LinkedHashMap<String, Object>();
		JSONObject jsonObject = new JSONObject(content);

		JSONArray namesArray = jsonObject.names();
		int length = namesArray.length();

		Map<String, Class> propertySchemaMap = null;
		if (loadByPropertiesSchema && jsonObject.has(PROPERTIES_SCHEMA_NAME)) {
			// ---------------------------------------------------------------------------
			// Load property type map begins
			// ---------------------------------------------------------------------------
			propertySchemaMap = new LinkedHashMap<String, Class>();
			String propertiesSchema = jsonObject.getString(PROPERTIES_SCHEMA_NAME);
			if (propertiesSchema != null) {
				JSONObject schemaJsonObject = new JSONObject(propertiesSchema);
				for (int i = 0; i < length; i++) {
					Object obj = namesArray.get(i);
					if (obj instanceof String) {
						String propName = (String) obj;
						if (PROPERTIES_SCHEMA_NAME.equals(propName)) {
							continue;
						}

						String propClassName = schemaJsonObject.getString(propName);
						Class propClass = null;
						try {
							propClass = Class.forName(propClassName);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (propClass != null) {
							propertySchemaMap.put(propName, propClass);
						}
					}
				}
			}
			// ---------------------------------------------------------------------------
			// Load property type map ends
			// ---------------------------------------------------------------------------
		}

		for (int i = 0; i < length; i++) {
			Object obj = namesArray.get(i);
			if (obj instanceof String) {
				String propName = (String) obj;
				Object propValue = null;

				if (PROPERTIES_SCHEMA_NAME.equals(propName)) {
					continue;
				}

				if (loadByPropertiesSchema && propertySchemaMap != null) {
					String rawStringValue = null;
					Object rawObjectValue = jsonObject.get(propName);
					if (rawObjectValue != null) {
						rawStringValue = rawObjectValue.toString();
					}

					Class propClass = propertySchemaMap.get(propName);
					if (Date.class.isAssignableFrom(propClass)) {
						// Convert string into Date
						if (rawObjectValue != null && !Date.class.isAssignableFrom(rawObjectValue.getClass())) {
							try {
								Date dateValue = DateUtil.toDate(rawStringValue, DateUtil.SIMPLE_DATE_FORMAT0, DateUtil.SIMPLE_DATE_FORMAT1, DateUtil.SIMPLE_DATE_FORMAT2);
								if (dateValue != null) {
									propValue = dateValue;
								}
							} catch (Exception e) {
								// e.printStackTrace();
							}
						}

					} else if (Boolean.class.isAssignableFrom(propClass)) {
						// Convert string into Boolean
						if (rawObjectValue != null && !Boolean.class.isAssignableFrom(rawObjectValue.getClass())) {
							try {
								Boolean booleanValue = Boolean.valueOf(rawStringValue);
								if (booleanValue != null) {
									propValue = booleanValue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} else if (Float.class.isAssignableFrom(propClass)) {
						// Convert string into Float
						if (rawObjectValue != null && !Float.class.isAssignableFrom(rawObjectValue.getClass())) {
							try {
								Float floatValue = Float.valueOf(rawStringValue);
								if (floatValue != null) {
									propValue = floatValue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} else if (Double.class.isAssignableFrom(propClass)) {
						// Convert string into Double
						if (rawObjectValue != null && !Double.class.isAssignableFrom(rawObjectValue.getClass())) {
							try {
								Double floatValue = Double.valueOf(rawStringValue);
								if (floatValue != null) {
									propValue = floatValue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} else if (Long.class.isAssignableFrom(propClass)) {
						// Convert string into Long
						if (rawObjectValue != null && !Long.class.isAssignableFrom(rawObjectValue.getClass())) {
							try {
								Long longValue = Long.valueOf(rawStringValue);
								if (longValue != null) {
									propValue = longValue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} else if (Integer.class.isAssignableFrom(propClass)) {
						// Convert string into Integer
						if (rawObjectValue != null && !Integer.class.isAssignableFrom(rawObjectValue.getClass())) {
							try {
								Integer floatValue = Integer.valueOf(rawStringValue);
								if (floatValue != null) {
									propValue = floatValue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					if (propValue == null) {
						propValue = jsonObject.get(propName);
					}

				} else {
					propValue = jsonObject.get(propName);
				}

				if (propValue != null) {
					properties.put(propName, propValue);
				}
			}
		}
		return properties;
	}

	public static void main(String[] args) {
		System.out.println("------------------------------------------------------------------------------------------------");
		Map<String, Object> properties = new LinkedHashMap<String, Object>();
		properties.put("url", "http://127.0.0.1:9090/indexservice/v1");
		properties.put("description", "IndexService1");
		properties.put("mydate", new Date());
		properties.put("myinteger", new Integer(10));
		properties.put("mylong", new Long(2000000));
		properties.put("myfloat", new Float(2.1));
		properties.put("myboolean", new Boolean(false));
		String contentString = JSONUtil.toJsonObject(properties, true).toString();
		System.out.println(contentString);

		System.out.println("------------------------------------------------------------------------------------------------");
		Map<String, Object> properties11 = JSONUtil.toProperties1(contentString);
		for (Iterator<Entry<String, Object>> entryItor = properties11.entrySet().iterator(); entryItor.hasNext();) {
			Entry<String, Object> entry = entryItor.next();
			String propName = entry.getKey();
			Object propValue = entry.getValue();
			System.out.println(propName + " = " + propValue + " (" + propValue.getClass().getName() + ")");
		}

		System.out.println("------------------------------------------------------------------------------------------------");
		Map<String, Object> properties12 = JSONUtil.toProperties(contentString, true);
		for (Iterator<Entry<String, Object>> entryItor = properties12.entrySet().iterator(); entryItor.hasNext();) {
			Entry<String, Object> entry = entryItor.next();
			String propName = entry.getKey();
			Object propValue = entry.getValue();
			System.out.println(propName + " = " + propValue + " (" + propValue.getClass().getName() + ")");
		}
	}

}
