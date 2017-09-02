package org.origin.core.resources.node.internal.misc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.core.resources.IFile;
import org.origin.core.resources.node.NodeDescription;

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
		String name = null;
		if (nodeDescJSON.has("name")) {
			name = nodeDescJSON.getString("name");
		}
		nodeDesc.setName(name);

		return nodeDesc;
	}

}
