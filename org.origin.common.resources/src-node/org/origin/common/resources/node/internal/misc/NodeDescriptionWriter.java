package org.origin.common.resources.node.internal.misc;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.common.resources.IFile;
import org.origin.common.resources.node.NodeDescription;

public class NodeDescriptionWriter {

	private static NodeDescriptionWriter INSTANCE = new NodeDescriptionWriter();

	public static NodeDescriptionWriter getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param nodeDesc
	 * @param file
	 * @throws IOException
	 */
	public void write(NodeDescription nodeDesc, File file) throws IOException {
		JSONObject rootJSON = rootToJSON(nodeDesc);
		if (rootJSON != null) {
			JSONUtil.save(rootJSON, file);
		}
	}

	/**
	 * 
	 * @param nodeDesc
	 * @param iFile
	 * @throws IOException
	 */
	public void write(NodeDescription nodeDesc, IFile iFile) throws IOException {
		JSONObject rootJSON = rootToJSON(nodeDesc);
		if (rootJSON != null) {
			OutputStream output = iFile.getOutputStream();
			if (output != null) {
				JSONUtil.save(rootJSON, output, true);
			}
		}
	}

	/**
	 * Convert NodeDescription to root JSONObject
	 * 
	 * @param nodeDesc
	 * @return
	 */
	protected JSONObject rootToJSON(NodeDescription nodeDesc) {
		if (nodeDesc == null) {
			return null;
		}

		JSONObject rootJSON = new JSONObject();

		JSONObject nodeDescJSON = nodeDescriptionToJSON(nodeDesc);
		rootJSON.put("NodeDescription", nodeDescJSON);

		return rootJSON;
	}

	/**
	 * Convert NodeDescription to NodeDescription JSONObject.
	 * 
	 * @param desc
	 * @return
	 */
	protected JSONObject nodeDescriptionToJSON(NodeDescription desc) {
		if (desc == null) {
			return null;
		}
		JSONObject nodeDescJSON = new JSONObject();

		// "version" attribute
		String version = desc.getVersion();
		if (version == null || version.isEmpty()) {
			version = NodeDescription.DEFAULT_VERSION;
		}
		desc.setVersion(version);

		// "id" attribute
		String id = desc.getId();
		if (id != null) {
			nodeDescJSON.put("id", id);
		}

		// "name" attribute
		// String name = desc.getName();
		// if (name != null) {
		// nodeDescJSON.put("name", name);
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
			nodeDescJSON.put("attributes", attributesJSONArray);
		}

		return nodeDescJSON;
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
		attributeJSON.put(attrName, attrValue);

		// "name" attribute
		// attributeJSON.put("name", attrName);

		// "value" attribute
		// attributeJSON.put("value", attrValue);

		return attributeJSON;
	}

}
