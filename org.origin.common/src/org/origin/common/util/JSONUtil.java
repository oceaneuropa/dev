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
	 * @return
	 */
	public static JSONObject toJson(Map<String, Object> properties) {
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
		return jsonObject;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public static String toJsonString(Map<String, Object> properties) {
		JSONObject jsonObject = toJson(properties);
		return jsonObject.toString();
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

	public static Map<String, Object> toProperties2(String content) {
		Map<String, Object> properties = new LinkedHashMap<String, Object>();
		JSONObject jsonObject = new JSONObject(content);
		JSONArray namesArray = jsonObject.names();
		int length = namesArray.length();
		for (int i = 0; i < length; i++) {
			Object obj = namesArray.get(i);
			if (obj instanceof String) {
				String propName = (String) obj;
				Object propValue = jsonObject.get(propName);
				if (propValue != null) {
					properties.put(propName, propValue);
				}
			}
		}
		return properties;
	}

	public static String toString(JSONObject jsonObject) {
		return jsonObject.toString();
	}

	public static String toString(JSONObject jsonObject, int indentFactor) {
		return jsonObject.toString(indentFactor);
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
		Map<String, Object> properties12 = JSONUtil.toProperties2(contentString);
		for (Iterator<Entry<String, Object>> entryItor = properties12.entrySet().iterator(); entryItor.hasNext();) {
			Entry<String, Object> entry = entryItor.next();
			String propName = entry.getKey();
			Object propValue = entry.getValue();
			System.out.println(propName + " = " + propValue + " (" + propValue.getClass().getName() + ")");
		}
	}

}
