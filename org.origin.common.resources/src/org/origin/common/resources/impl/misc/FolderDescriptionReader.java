package org.origin.common.resources.impl.misc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.common.resources.FolderDescription;
import org.origin.common.resources.IFile;

public class FolderDescriptionReader {

	private static FolderDescriptionReader INSTANCE = new FolderDescriptionReader();

	public static FolderDescriptionReader getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public FolderDescription read(File file) throws IOException {
		JSONObject rootJSON = JSONUtil.load(file);
		FolderDescription desc = jsonToRoot(rootJSON);
		return desc;
	}

	/**
	 * 
	 * @param iFile
	 * @return
	 * @throws IOException
	 */
	public FolderDescription read(IFile iFile) throws IOException {
		InputStream input = iFile.getInputStream();
		JSONObject rootJSON = JSONUtil.load(input, true);
		FolderDescription desc = jsonToRoot(rootJSON);
		return desc;
	}

	/**
	 * Convert root JSONObject to FolderDescription.
	 * 
	 * @param rootJSON
	 * @return
	 */
	protected FolderDescription jsonToRoot(JSONObject rootJSON) {
		if (rootJSON == null) {
			return null;
		}

		FolderDescription desc = null;

		if (rootJSON.has("FolderDescription")) {
			JSONObject descJSON = rootJSON.getJSONObject("FolderDescription");
			if (descJSON != null) {
				desc = jsonToFolderDescription(descJSON);
			}
		}

		return desc;
	}

	/**
	 * Convert JSONObject to FolderDescription.
	 * 
	 * @param descJSON
	 * @return
	 */
	protected FolderDescription jsonToFolderDescription(JSONObject descJSON) {
		if (descJSON == null) {
			return null;
		}

		FolderDescription desc = new FolderDescription();

		// "version" attribute
		String version = null;
		if (descJSON.has("version")) {
			version = descJSON.getString("version");
		}
		desc.setVersion(version);

		// "id" attribute
		String id = null;
		if (descJSON.has("id")) {
			id = descJSON.getString("id");
		}
		desc.setId(id);

		// "name" attribute
		// String name = null;
		// if (descJSON.has("name")) {
		// name = descJSON.getString("name");
		// }
		// desc.setName(name);

		// "attributes" array
		if (descJSON.has("attributes")) {
			JSONArray attributesJSONArray = descJSON.getJSONArray("attributes");
			if (attributesJSONArray != null) {
				int length = attributesJSONArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject attributeJSONObject = attributesJSONArray.getJSONObject(i);
					if (attributeJSONObject != null) {
						Object[] attribute = jsonToAttribute(attributeJSONObject);
						if (attribute != null) {
							String attrName = (String) attribute[0];
							Object attrValue = attribute[1];
							desc.setAttirbute(attrName, attrValue);
						}
					}
				}
			}
		}

		return desc;
	}

	/**
	 * 
	 * @param attributeJSONObject
	 * @return
	 */
	protected Object[] jsonToAttribute(JSONObject attributeJSONObject) {
		if (attributeJSONObject == null) {
			return null;
		}

		// "name" attribute
		String name = null;
		if (attributeJSONObject.has("name")) {
			name = attributeJSONObject.getString("name");
		}

		// "value" attribute
		Object value = null;
		if (attributeJSONObject.has("value")) { // $NON-NLS-1$
			value = attributeJSONObject.get("value");
		}

		if (name == null || value == null) {
			return null;
		}

		return new Object[] { name, value };
	}

}
