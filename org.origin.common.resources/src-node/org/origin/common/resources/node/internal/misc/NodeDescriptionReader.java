package org.origin.common.resources.node.internal.misc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.common.resources.IFile;
import org.origin.common.resources.node.NodeDescription;

public class NodeDescriptionReader {

	private static NodeDescriptionReader INSTANCE = new NodeDescriptionReader();

	public static NodeDescriptionReader getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public NodeDescription read(File file) throws IOException {
		JSONObject rootJSON = JSONUtil.load(file);
		NodeDescription nodeDesc = jsonToRoot(rootJSON);
		return nodeDesc;
	}

	/**
	 * 
	 * @param iFile
	 * @return
	 * @throws IOException
	 */
	public NodeDescription read(IFile iFile) throws IOException {
		InputStream input = iFile.getInputStream();
		JSONObject rootJSON = JSONUtil.load(input, true);
		NodeDescription nodeDesc = jsonToRoot(rootJSON);
		return nodeDesc;
	}

	/**
	 * Convert root JSONObject to NodeDescription.
	 * 
	 * @param rootJSON
	 * @return
	 */
	protected NodeDescription jsonToRoot(JSONObject rootJSON) {
		if (rootJSON == null) {
			return null;
		}

		NodeDescription nodeDesc = null;

		if (rootJSON.has("NodeDescription")) {
			JSONObject nodeDescJSON = rootJSON.getJSONObject("NodeDescription");
			if (nodeDescJSON != null) {
				nodeDesc = jsonToNodeDescription(nodeDescJSON);
			}
		}

		return nodeDesc;
	}

	/**
	 * Convert NodeDescription JSONObject to NodeDescription.
	 * 
	 * @param nodeDescJSON
	 * @return
	 */
	protected NodeDescription jsonToNodeDescription(JSONObject nodeDescJSON) {
		if (nodeDescJSON == null) {
			return null;
		}

		NodeDescription nodeDesc = new NodeDescription();

		// "version" attribute
		String version = null;
		if (nodeDescJSON.has("version")) {
			version = nodeDescJSON.getString("version");
		}
		nodeDesc.setVersion(version);

		// "id" attribute
		String id = null;
		if (nodeDescJSON.has("id")) {
			id = nodeDescJSON.getString("id");
		}
		nodeDesc.setId(id);

		// "name" attribute
		// String name = null;
		// if (nodeDescJSON.has("name")) {
		// name = nodeDescJSON.getString("name");
		// }
		// nodeDesc.setName(name);

		// "attributes" array
		if (nodeDescJSON.has("attributes")) {
			JSONArray attributesJSONArray = nodeDescJSON.getJSONArray("attributes");
			if (attributesJSONArray != null) {
				int length = attributesJSONArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject attributeJSONObject = attributesJSONArray.getJSONObject(i);
					if (attributeJSONObject != null) {
						// Object[] attribute = jsonToAttribute(attributeJSONObject);
						// if (attribute != null) {
						// String attrName = (String) attribute[0];
						// Object attrValue = attribute[1];
						// nodeDesc.setAttirbute(attrName, attrValue);
						// }
						String[] attrNames = JSONObject.getNames(attributeJSONObject);
						if (attrNames != null) {
							for (String attrName : attrNames) {
								Object attrValue = attributeJSONObject.get(attrName);
								if (attrValue != null) {
									nodeDesc.setAttirbute(attrName, attrValue);
								}
							}
						}
					}
				}
			}
		}

		return nodeDesc;
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
