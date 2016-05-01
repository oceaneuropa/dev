package org.origin.common.util;

import org.json.JSONObject;

public class JSONUtil {

	public static JSONObject parse(String content) {
		return new JSONObject(content);
	}

	// public static Object parse(String content) {
	// ObjectMapper mapper = new ObjectMapper();
	// return null;
	// }

	public static String toString(JSONObject jsonObject) {
		return jsonObject.toString();
	}

	public static String toString(JSONObject jsonObject, int indentFactor) {
		return jsonObject.toString(indentFactor);
	}

}
