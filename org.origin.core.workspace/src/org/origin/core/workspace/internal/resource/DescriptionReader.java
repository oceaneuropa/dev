package org.origin.core.workspace.internal.resource;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.core.workspace.IResourceDescription;

public class DescriptionReader {

	/**
	 * Read "natureIds" content from JSON object and set the values to resource description object.
	 * 
	 * @param descriptionJSON
	 * @param description
	 */
	protected void jsonToNatureIds(JSONObject descriptionJSON, IResourceDescription description) {
		// "natureIds" string array
		if (descriptionJSON.has("natureIds")) {
			JSONArray natureIdJSONArray = descriptionJSON.getJSONArray("natureIds");
			if (natureIdJSONArray != null) {
				int length = natureIdJSONArray.length();
				for (int i = 0; i < length; i++) {
					Object obj = natureIdJSONArray.get(i);
					if (obj instanceof String) {
						String natureId = (String) obj;
						description.addNatureId(natureId);
					}
				}
			}
		}
	}

	/**
	 * Read "properties" content from JSON object and set the values to resource description object.
	 * 
	 * @param descriptionJSON
	 * @param description
	 */
	protected void jsonToProperties(JSONObject descriptionJSON, IResourceDescription description) {
		// "properties" attribute
		Map<String, Object> properties = null;
		if (descriptionJSON.has("properties")) {
			String propertiesString = descriptionJSON.getString("properties");
			if (propertiesString != null) {
				properties = JSONUtil.toProperties(propertiesString);
			}
		}
		if (properties != null) {
			description.setProperties(properties);
		}
	}

}
