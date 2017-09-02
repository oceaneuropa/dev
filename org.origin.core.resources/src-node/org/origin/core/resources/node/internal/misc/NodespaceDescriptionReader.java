package org.origin.core.resources.node.internal.misc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.core.resources.IFile;
import org.origin.core.resources.node.NodespaceDescription;

public class NodespaceDescriptionReader {

	private static NodespaceDescriptionReader INSTANCE = new NodespaceDescriptionReader();

	public static NodespaceDescriptionReader getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public NodespaceDescription read(File file) throws IOException {
		JSONObject rootJSON = JSONUtil.load(file);
		NodespaceDescription nodespaceDesc = jsonToRoot(rootJSON);
		return nodespaceDesc;
	}

	/**
	 * 
	 * @param iFile
	 * @return
	 * @throws IOException
	 */
	public NodespaceDescription read(IFile iFile) throws IOException {
		InputStream input = iFile.getInputStream();
		JSONObject rootJSON = JSONUtil.load(input, true);
		NodespaceDescription nodespaceDesc = jsonToRoot(rootJSON);
		return nodespaceDesc;
	}

	/**
	 * Convert root JSONObject to NodespaceDescription.
	 * 
	 * @param rootJSON
	 * @return
	 */
	protected NodespaceDescription jsonToRoot(JSONObject rootJSON) {
		if (rootJSON == null) {
			return null;
		}

		NodespaceDescription nodespaceDesc = null;

		if (rootJSON.has("NodespaceDescription")) {
			JSONObject nodespaceDescJSON = rootJSON.getJSONObject("NodespaceDescription");
			if (nodespaceDescJSON != null) {
				nodespaceDesc = jsonToNodespaceDescription(nodespaceDescJSON);
			}
		}

		return nodespaceDesc;
	}

	/**
	 * Convert NodespaceDescription JSONObject to NodespaceDescription.
	 * 
	 * @param nodespaceDescJSON
	 * @return
	 */
	protected NodespaceDescription jsonToNodespaceDescription(JSONObject nodespaceDescJSON) {
		if (nodespaceDescJSON == null) {
			return null;
		}

		NodespaceDescription nodespaceDesc = new NodespaceDescription();

		// "version" attribute
		String version = null;
		if (nodespaceDescJSON.has("version")) {
			version = nodespaceDescJSON.getString("version");
		}
		nodespaceDesc.setVersion(version);

		// "id" attribute
		String id = null;
		if (nodespaceDescJSON.has("id")) {
			id = nodespaceDescJSON.getString("id");
		}
		nodespaceDesc.setId(id);

		// "name" attribute
		String name = null;
		if (nodespaceDescJSON.has("name")) {
			name = nodespaceDescJSON.getString("name");
		}
		nodespaceDesc.setName(name);

		return nodespaceDesc;
	}

}
