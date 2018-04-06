package org.origin.common.resources.impl.misc;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.common.resources.FolderDescription;
import org.origin.common.resources.IFile;

public class FolderDescriptionWriter {

	private static FolderDescriptionWriter INSTANCE = new FolderDescriptionWriter();

	public static FolderDescriptionWriter getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param desc
	 * @param file
	 * @throws IOException
	 */
	public void write(FolderDescription desc, File file) throws IOException {
		JSONObject rootJSON = rootToJSON(desc);
		if (rootJSON != null) {
			JSONUtil.save(rootJSON, file);
		}
	}

	/**
	 * 
	 * @param desc
	 * @param iFile
	 * @throws IOException
	 */
	public void write(FolderDescription desc, IFile iFile) throws IOException {
		JSONObject rootJSON = rootToJSON(desc);
		if (rootJSON != null) {
			OutputStream output = iFile.getOutputStream();
			if (output != null) {
				JSONUtil.save(rootJSON, output, true);
			}
		}
	}

	/**
	 * Convert FolderDescription to root JSONObject
	 * 
	 * @param desc
	 * @return
	 */
	protected JSONObject rootToJSON(FolderDescription desc) {
		if (desc == null) {
			return null;
		}

		JSONObject rootJSON = new JSONObject();

		JSONObject descJSON = folderDescriptionToJSON(desc);
		rootJSON.put("FolderDescription", descJSON);

		return rootJSON;
	}

	/**
	 * Convert FolderDescription to JSONObject.
	 * 
	 * @param desc
	 * @return
	 */
	protected JSONObject folderDescriptionToJSON(FolderDescription desc) {
		if (desc == null) {
			return null;
		}
		JSONObject descJSON = new JSONObject();

		// "version" attribute
		String version = desc.getVersion();
		if (version == null || version.isEmpty()) {
			version = FolderDescription.DEFAULT_VERSION;
		}
		desc.setVersion(version);

		// "id" attribute
		String id = desc.getId();
		if (id != null) {
			descJSON.put("id", id);
		}

		// "name" attribute
		// String name = desc.getName();
		// if (name != null) {
		// descJSON.put("name", name);
		// }

		// "attributes" array
		JSONArray attributesJSONArray = new JSONArray();

		int index = 0;
		Map<String, Object> attributes = desc.getAttributes();
		for (Iterator<String> itor = attributes.keySet().iterator(); itor.hasNext();) {
			String attrName = itor.next();
			Object attrValue = attributes.get(attrName);

			JSONObject attributeJSONObject = attributeToJSON(attrName, attrValue);
			if (attributeJSONObject != null) {
				attributesJSONArray.put(index++, attributeJSONObject);
			}
		}
		if (index > 0) {
			descJSON.put("attributes", attributesJSONArray);
		}

		return descJSON;
	}

	/**
	 * 
	 * @param attrName
	 * @param attrValue
	 * @return
	 */
	protected JSONObject attributeToJSON(String attrName, Object attrValue) {
		if (attrName == null || attrValue == null) {
			return null;
		}
		JSONObject attributeJSON = new JSONObject();

		// "name" attribute
		attributeJSON.put("name", attrName);

		// "value" attribute
		attributeJSON.put("value", attrValue);

		return attributeJSON;
	}

}
