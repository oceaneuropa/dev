package org.origin.core.resources.node.internal.misc;

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
	 * @param iFile
	 * @return
	 * @throws IOException
	 */
	public NodespaceDescription read(IFile iFile) throws IOException {
		InputStream input = iFile.getInputStream();
		JSONObject rootJSON = JSONUtil.load(input, true);
		NodespaceDescription desc = jsonToRoot(rootJSON);
		return desc;
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

		NodespaceDescription desc = null;
		if (rootJSON.has("NodespaceDescription")) {
			JSONObject descJSON = rootJSON.getJSONObject("NodespaceDescription");
			if (descJSON != null) {
				desc = jsonToNodespaceDescription(descJSON);
			}
		}
		return desc;
	}

	/**
	 * Convert NodespaceDescription JSONObject to NodespaceDescription.
	 * 
	 * @param descJSON
	 * @return
	 */
	protected NodespaceDescription jsonToNodespaceDescription(JSONObject descJSON) {
		if (descJSON == null) {
			return null;
		}

		NodespaceDescription desc = new NodespaceDescription();

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
		String name = null;
		if (descJSON.has("name")) {
			name = descJSON.getString("name");
		}
		desc.setName(name);

		return desc;
	}

}

/// **
// *
// * @param file
// * @return
// * @throws IOException
// */
// public NodespaceDescription read(File file) throws IOException {
// JSONObject rootJSON = JSONUtil.load(file);
// NodespaceDescription nodespaceDesc = jsonToRoot(rootJSON);
// return nodespaceDesc;
// }