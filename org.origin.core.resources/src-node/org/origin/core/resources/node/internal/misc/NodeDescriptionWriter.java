package org.origin.core.resources.node.internal.misc;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.core.resources.IFile;
import org.origin.core.resources.node.NodeDescription;

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
	 * @param nodeDesc
	 * @return
	 */
	protected JSONObject nodeDescriptionToJSON(NodeDescription nodeDesc) {
		if (nodeDesc == null) {
			return null;
		}
		JSONObject nodeDescJSON = new JSONObject();

		// "version" attribute
		String version = nodeDesc.getVersion();
		if (version == null || version.isEmpty()) {
			version = NodeDescription.DEFAULT_VERSION;
		}
		nodeDesc.setVersion(version);

		// "id" attribute
		String id = nodeDesc.getId();
		if (id != null) {
			nodeDescJSON.put("id", id);
		}

		// "name" attribute
		String name = nodeDesc.getName();
		if (name != null) {
			nodeDescJSON.put("name", name);
		}

		return nodeDescJSON;
	}

}
