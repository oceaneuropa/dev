package org.origin.common.json;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.origin.common.util.DateUtil;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {

	protected static final String PROPERTIES_SCHEMA_NAME = "__schema__";

	public static Charset getDefaultCharset() {
		return StandardCharsets.UTF_8;
	}

	/**
	 * Load JSONObject from a File.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static JSONObject load(File file) throws IOException {
		JSONObject jsonObject = null;
		InputStream is = null;
		try {
			if (file != null && file.exists()) {
				is = new FileInputStream(file);
			}
			if (is != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuffer buffer = new StringBuffer();

				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}

				String stringContent = buffer.toString();
				jsonObject = new JSONObject(stringContent);
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonObject;
	}

	/**
	 * Save JSONObject to a File.
	 * 
	 * @param file
	 * @param jsonObject
	 * @throws IOException
	 */
	public static void save(File file, JSONObject jsonObject) throws IOException {
		String stringContent = jsonObject.toString(4);

		ByteArrayInputStream is = null;
		FileOutputStream fos = null;

		byte[] bytes = stringContent.getBytes(getDefaultCharset());
		is = new ByteArrayInputStream(bytes);

		try {
			if (is != null) {
				if (!file.exists()) {
					file.createNewFile();
				}
				fos = new FileOutputStream(file);
				fos.write(bytes);
				fos.flush();
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Convert properties to a JSON string.
	 * 
	 * @param properties
	 * @return
	 */
	public static String toJsonString(Map<String, Object> properties) {
		return toJsonObject(properties, true).toString();
	}

	/**
	 * Convert a list to a JSON string.
	 * 
	 * @param elements
	 * @return
	 */
	public static String toJsonString(List<?> elements) {
		return toJsonObject(elements, true).toString();
	}

	/**
	 * Convert properties to a JSONObject.
	 * 
	 * @param properties
	 * @param createSchema
	 * @return
	 */
	public static JSONObject toJsonObject(Map<String, Object> properties, boolean createSchema) {
		JSONObject jsonObject = new JSONObject();
		if (properties != null) {
			for (Iterator<Entry<String, Object>> entryItor = properties.entrySet().iterator(); entryItor.hasNext();) {
				Entry<String, Object> entry = entryItor.next();
				String propName = entry.getKey();
				Object propValue = entry.getValue();
				if (propValue == null) {
					propValue = "";
				}

				if (propValue instanceof List) {
					// if a property value is List, serialize the List to a JSON object string and use it as the property value.
					String propsString = toJsonObject((List<Object>) propValue, createSchema).toString();
					jsonObject.put(propName, propsString);

				} else if (propValue instanceof Map) {
					// if a property value is Map, serialize the Map to a JSON object string and use it as the property value.
					String propsString = toJsonObject((Map<String, Object>) propValue, createSchema).toString();
					jsonObject.put(propName, propsString);

				} else {
					jsonObject.put(propName, propValue);
				}
			}

			if (createSchema) {
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
					} else if (propValue instanceof List) {
						schemaJsonObject.put(propName, List.class.getName());
					} else if (propValue instanceof Map) {
						schemaJsonObject.put(propName, Map.class.getName());
					} else {
						// if not recognized, serialize it as String
						schemaJsonObject.put(propName, String.class.getName());
					}
				}
				jsonObject.put(PROPERTIES_SCHEMA_NAME, schemaJsonObject.toString());
			}
		}
		return jsonObject;
	}

	/**
	 * Convert a list to a JSONObject.
	 * 
	 * @param elements
	 * @param createSchema
	 * @return
	 */
	public static JSONObject toJsonObject(List<?> elements, boolean createSchema) {
		JSONObject jsonObject = new JSONObject();
		if (elements != null) {
			for (int i = 0; i < elements.size(); i++) {
				String indexString = String.valueOf(i);
				Object element = elements.get(i);
				if (element == null) {
					element = "";
				}

				if (element instanceof List) {
					// if a property value is List, serialize the List to a JSON object string and use it as the property value.
					String propsString = toJsonObject((List<Object>) element, createSchema).toString();
					jsonObject.put(indexString, propsString);

				} else if (element instanceof Map) {
					// if a property value is Map, serialize the Map to a JSON object string and use it as the property value.
					String propsString = toJsonObject((Map<String, Object>) element, createSchema).toString();
					jsonObject.put(indexString, propsString);

				} else {
					jsonObject.put(indexString, element);
				}
			}

			if (createSchema) {
				JSONObject schemaJsonObject = new JSONObject();
				for (int i = 0; i < elements.size(); i++) {
					String indexString = String.valueOf(i);
					Object element = elements.get(i);

					if (element instanceof Date) {
						schemaJsonObject.put(indexString, Date.class.getName());
					} else if (element instanceof Boolean) {
						schemaJsonObject.put(indexString, Boolean.class.getName());
					} else if (element instanceof Float) {
						schemaJsonObject.put(indexString, Float.class.getName());
					} else if (element instanceof Double) {
						schemaJsonObject.put(indexString, Double.class.getName());
					} else if (element instanceof Long) {
						schemaJsonObject.put(indexString, Long.class.getName());
					} else if (element instanceof Integer) {
						schemaJsonObject.put(indexString, Integer.class.getName());
					} else if (element instanceof String) {
						schemaJsonObject.put(indexString, String.class.getName());
					} else if (element instanceof List) {
						schemaJsonObject.put(indexString, List.class.getName());
					} else if (element instanceof Map) {
						schemaJsonObject.put(indexString, Map.class.getName());
					} else {
						// if not recognized, serialize it as Object
						schemaJsonObject.put(indexString, Object.class.getName());
					}
				}
				jsonObject.put(PROPERTIES_SCHEMA_NAME, schemaJsonObject.toString());
			}
		}
		return jsonObject;
	}

	/**
	 * Convert JSON string to Map.
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Map<String, Object> toProperties(String jsonString) {
		return toProperties(jsonString, true);
	}

	/**
	 * Convert JSON string to Map.
	 * 
	 * @param jsonString
	 * @param loadBySchema
	 * @return
	 */
	public static Map<String, Object> toProperties(String jsonString, boolean loadBySchema) {
		Map<String, Object> properties = new LinkedHashMap<String, Object>();
		if (jsonString == null || jsonString.isEmpty()) {
			return properties;
		}

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (jsonObject == null) {
			return properties;
		}

		JSONArray namesArray = jsonObject.names();
		int length = namesArray.length();

		Map<String, Class> propertyNameToPropertyTypeMap = null;
		if (loadBySchema && jsonObject.has(PROPERTIES_SCHEMA_NAME)) {
			// ---------------------------------------------------------------------------
			// Load property type map begins
			// ---------------------------------------------------------------------------
			propertyNameToPropertyTypeMap = new LinkedHashMap<String, Class>();
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
							propertyNameToPropertyTypeMap.put(propName, propClass);
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

				if (loadBySchema && propertyNameToPropertyTypeMap != null) {
					String rawStringValue = null;
					Object rawObjectValue = jsonObject.get(propName);
					if (rawObjectValue != null) {
						rawStringValue = rawObjectValue.toString();
					}

					Class propClass = propertyNameToPropertyTypeMap.get(propName);
					if (Date.class.isAssignableFrom(propClass)) {
						// Convert string into Date
						if (rawObjectValue != null && !Date.class.isAssignableFrom(rawObjectValue.getClass())) {
							try {
								Date dateValue = DateUtil.toDate(rawStringValue, DateUtil.getCommonDateFormats());
								if (dateValue != null) {
									propValue = dateValue;
								}
							} catch (Exception e) {
								e.printStackTrace();
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
								Integer integerValue = Integer.valueOf(rawStringValue);
								if (integerValue != null) {
									propValue = integerValue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} else if (List.class.isAssignableFrom(propClass)) {
						// Convert JSON object string into List
						if (rawStringValue != null) {
							List<Object> listValue = toList(rawStringValue, loadBySchema);
							if (listValue != null) {
								propValue = listValue;
							}
						}

					} else if (Map.class.isAssignableFrom(propClass)) {
						// Convert JSON object string into Map
						if (rawStringValue != null) {
							Map<String, Object> mapValue = toProperties(rawStringValue, loadBySchema);
							if (mapValue != null) {
								propValue = mapValue;
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

	/**
	 * Convert JSON string to List.
	 * 
	 * @param jsonString
	 * @param loadBySchema
	 * @return
	 */
	public static List<Object> toList(String jsonString, boolean loadBySchema) {
		List<Object> elements = new ArrayList<Object>();
		if (jsonString == null || jsonString.isEmpty()) {
			return elements;
		}

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (jsonObject == null) {
			return elements;
		}

		JSONArray indexArray = jsonObject.names();
		int length = indexArray.length();

		Map<String, Class> elementIndexToElementTypeMap = null;
		if (loadBySchema && jsonObject.has(PROPERTIES_SCHEMA_NAME)) {
			// ---------------------------------------------------------------------------
			// Load element type map begins
			// ---------------------------------------------------------------------------
			elementIndexToElementTypeMap = new LinkedHashMap<String, Class>();
			String elementsSchema = jsonObject.getString(PROPERTIES_SCHEMA_NAME);
			if (elementsSchema != null) {
				JSONObject jsonObjectForSchema = new JSONObject(elementsSchema);
				for (int i = 0; i < length; i++) {
					Object obj = indexArray.get(i);
					if (obj instanceof String) {
						String indexString = (String) obj;
						if (PROPERTIES_SCHEMA_NAME.equals(indexString)) {
							continue;
						}

						String elementClassName = jsonObjectForSchema.getString(indexString);
						Class elementClass = null;
						try {
							elementClass = Class.forName(elementClassName);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (elementClass != null) {
							elementIndexToElementTypeMap.put(indexString, elementClass);
						}
					}
				}
			}
			// ---------------------------------------------------------------------------
			// Load property type map ends
			// ---------------------------------------------------------------------------
		}

		for (int i = 0; i < length; i++) {
			Object obj = indexArray.get(i);
			if (obj instanceof String) {
				String indexString = (String) obj;
				Object elementValue = null;

				if (PROPERTIES_SCHEMA_NAME.equals(indexString)) {
					continue;
				}

				if (loadBySchema && elementIndexToElementTypeMap != null) {
					String rawStringValue = null;
					Object rawObjectValue = jsonObject.get(indexString);
					if (rawObjectValue != null) {
						rawStringValue = rawObjectValue.toString();
					}

					Class elementClass = elementIndexToElementTypeMap.get(indexString);
					if (Date.class.isAssignableFrom(elementClass)) {
						// Convert string into Date
						if (rawObjectValue != null && !Date.class.isAssignableFrom(rawObjectValue.getClass())) {
							try {
								Date dateValue = DateUtil.toDate(rawStringValue, DateUtil.getCommonDateFormats());
								if (dateValue != null) {
									elementValue = dateValue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} else if (Boolean.class.isAssignableFrom(elementClass)) {
						// Convert string into Boolean
						if (rawObjectValue != null && !Boolean.class.isAssignableFrom(rawObjectValue.getClass())) {
							try {
								Boolean booleanValue = Boolean.valueOf(rawStringValue);
								if (booleanValue != null) {
									elementValue = booleanValue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} else if (Float.class.isAssignableFrom(elementClass)) {
						// Convert string into Float
						if (rawObjectValue != null && !Float.class.isAssignableFrom(rawObjectValue.getClass())) {
							try {
								Float floatValue = Float.valueOf(rawStringValue);
								if (floatValue != null) {
									elementValue = floatValue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} else if (Double.class.isAssignableFrom(elementClass)) {
						// Convert string into Double
						if (rawObjectValue != null && !Double.class.isAssignableFrom(rawObjectValue.getClass())) {
							try {
								Double floatValue = Double.valueOf(rawStringValue);
								if (floatValue != null) {
									elementValue = floatValue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} else if (Long.class.isAssignableFrom(elementClass)) {
						// Convert string into Long
						if (rawObjectValue != null && !Long.class.isAssignableFrom(rawObjectValue.getClass())) {
							try {
								Long longValue = Long.valueOf(rawStringValue);
								if (longValue != null) {
									elementValue = longValue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} else if (Integer.class.isAssignableFrom(elementClass)) {
						// Convert string into Integer
						if (rawObjectValue != null && !Integer.class.isAssignableFrom(rawObjectValue.getClass())) {
							try {
								Integer integerValue = Integer.valueOf(rawStringValue);
								if (integerValue != null) {
									elementValue = integerValue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} else if (List.class.isAssignableFrom(elementClass)) {
						// Convert JSON object string into List
						if (rawStringValue != null) {
							List<Object> listValue = toList(rawStringValue, loadBySchema);
							if (listValue != null) {
								elementValue = listValue;
							}
						}

					} else if (Map.class.isAssignableFrom(elementClass)) {
						// Convert JSON object string into Map
						if (rawStringValue != null) {
							Map<String, Object> mapValue = toProperties(rawStringValue, loadBySchema);
							if (mapValue != null) {
								elementValue = mapValue;
							}
						}
					}

					if (elementValue == null) {
						elementValue = jsonObject.get(indexString);
					}

				} else {
					elementValue = jsonObject.get(indexString);
				}

				if (elementValue != null) {
					try {
						int index = Integer.parseInt(indexString);
						elements.set(index, elementValue);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return elements;
	}

	/**
	 * 
	 * @param failOnUnknownProperties
	 * @return
	 */
	public static ObjectMapper createObjectMapper(boolean failOnUnknownProperties) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
		return mapper;
	}

	/**
	 * Convert Properties to JSONObject.
	 * 
	 * @param properties
	 * @return
	 */
	private static JSONObject toJson(Properties properties) {
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

	private static Map<String, Object> toProperties1(String content) {
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
		String contentString = JSONUtil.toJsonString(properties);
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
