package org.origin.core.resources.node.internal.misc;

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
	 * @param desc
	 * @param iFile
	 * @throws IOException
	 */
	public void write(NodespaceDescription desc, IFile iFile) throws IOException {
		JSONObject rootJSON = rootToJSON(desc);
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
	 * @param desc
	 * @return
	 */
	protected JSONObject rootToJSON(NodespaceDescription desc) {
		if (desc == null) {
			return null;
		}

		JSONObject rootJSON = new JSONObject();

		JSONObject descJSON = nodespaceDescriptionToJSON(desc);
		rootJSON.put("NodespaceDescription", descJSON);

		return rootJSON;
	}

	/**
	 * Convert NodespaceDescription to NodespaceDescription JSONObject.
	 * 
	 * @param desc
	 * @return
	 */
	protected JSONObject nodespaceDescriptionToJSON(NodespaceDescription desc) {
		if (desc == null) {
			return null;
		}
		JSONObject descJSON = new JSONObject();

		// "version" attribute
		String version = desc.getVersion();
		if (version == null || version.isEmpty()) {
			version = NodespaceDescription.DEFAULT_VERSION;
		}
		desc.setVersion(version);

		// "id" attribute
		String id = desc.getId();
		if (id != null) {
			descJSON.put("id", id);
		}

		// "name" attribute
		String name = desc.getName();
		if (name != null) {
			descJSON.put("name", name);
		}

		return descJSON;
	}

}

// /**
// *
// * @param desc
// * @param file
// * @throws IOException
// */
// public void write(NodespaceDescription desc, File file) throws IOException {
// JSONObject rootJSON = rootToJSON(desc);
// if (rootJSON != null) {
// JSONUtil.save(rootJSON, file);
// }
// }