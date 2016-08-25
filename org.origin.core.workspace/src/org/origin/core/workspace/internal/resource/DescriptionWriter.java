package org.origin.core.workspace.internal.resource;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.core.workspace.IResourceDescription;

public class DescriptionWriter {

	/**
	 * Read "natureIds" values from resource description object and set the content to JSON object.
	 * 
	 * @param description
	 * @param descriptionJSON
	 */
	public void natureIdsToJson(IResourceDescription description, JSONObject descriptionJSON) {
		// "natureIds" array
		String[] natureIds = description.getNatureIds();
		if (natureIds != null && natureIds.length > 0) {
			JSONArray natureIdJSONArray = new JSONArray();
			for (int i = 0; i < natureIds.length; i++) {
				natureIdJSONArray.put(i, natureIds[i]);
			}
			descriptionJSON.put("natureIds", natureIdJSONArray);
		}
	}

	/**
	 * Read "properties" values from resource description object and set the content to JSON object.
	 * 
	 * @param description
	 * @param descriptionJSON
	 */
	public void propertiesToJson(IResourceDescription description, JSONObject descriptionJSON) {
		// "properties" attribute
		Map<String, Object> properties = description.getProperties();
		String propertiesString = JSONUtil.toJsonString(properties);
		if (propertiesString != null) {
			descriptionJSON.put("properties", propertiesString);
		}
	}

}
