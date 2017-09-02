package org.origin.core.resources.node.internal.misc;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.core.resources.IFile;
import org.origin.core.resources.node.NodespaceDescription;

public class NodespaceDescriptionWriter {

	private static NodespaceDescriptionWriter INSTANCE = new NodespaceDescriptionWriter();

	public static NodespaceDescriptionWriter getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param nodespaceDesc
	 * @param file
	 * @throws IOException
	 */
	public void write(NodespaceDescription nodespaceDesc, File file) throws IOException {
		JSONObject rootJSON = rootToJSON(nodespaceDesc);
		if (rootJSON != null) {
			JSONUtil.save(rootJSON, file);
		}
	}

	/**
	 * 
	 * @param nodespaceDesc
	 * @param iFile
	 * @throws IOException
	 */
	public void write(NodespaceDescription nodespaceDesc, IFile iFile) throws IOException {
		JSONObject rootJSON = rootToJSON(nodespaceDesc);
		if (rootJSON != null) {
			if (!iFile.exists()) {
				iFile.create();
			}
			OutputStream output = iFile.getOutputStream();
			if (output != null) {
				JSONUtil.save(rootJSON, output, true);
			}
		}
	}

	/**
	 * Convert NodespaceDescription to root JSONObject
	 * 
	 * @param nodespaceDesc
	 * @return
	 */
	protected JSONObject rootToJSON(NodespaceDescription nodespaceDesc) {
		if (nodespaceDesc == null) {
			return null;
		}

		JSONObject rootJSON = new JSONObject();

		JSONObject nodespaceDescJSON = nodespaceDescriptionToJSON(nodespaceDesc);
		rootJSON.put("NodespaceDescription", nodespaceDescJSON);

		return rootJSON;
	}

	/**
	 * Convert NodespaceDescription to NodespaceDescription JSONObject.
	 * 
	 * @param nodespaceDesc
	 * @return
	 */
	protected JSONObject nodespaceDescriptionToJSON(NodespaceDescription nodespaceDesc) {
		if (nodespaceDesc == null) {
			return null;
		}
		JSONObject nodespaceDescJSON = new JSONObject();

		// "version" attribute
		String version = nodespaceDesc.getVersion();
		if (version == null || version.isEmpty()) {
			version = NodespaceDescription.DEFAULT_VERSION;
		}
		nodespaceDesc.setVersion(version);

		// "id" attribute
		String id = nodespaceDesc.getId();
		if (id != null) {
			nodespaceDescJSON.put("id", id);
		}

		// "name" attribute
		String name = nodespaceDesc.getName();
		if (name != null) {
			nodespaceDescJSON.put("name", name);
		}

		return nodespaceDescJSON;
	}

}
