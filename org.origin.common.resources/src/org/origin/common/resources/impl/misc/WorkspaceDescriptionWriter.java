package org.origin.common.resources.impl.misc;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONObject;
import org.origin.common.io.IOUtil;
import org.origin.common.json.JSONUtil;
import org.origin.common.resources.IFile;
import org.origin.common.resources.WorkspaceDescription;

public class WorkspaceDescriptionWriter {

	private static WorkspaceDescriptionWriter INSTANCE = new WorkspaceDescriptionWriter();

	public static WorkspaceDescriptionWriter getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param desc
	 * @param iFile
	 * @throws IOException
	 */
	public void write(WorkspaceDescription desc, IFile iFile) throws IOException {
		JSONObject rootJSON = rootToJSON(desc);
		if (rootJSON != null) {
			if (!iFile.exists()) {
				iFile.create();
			}
			OutputStream output = null;
			try {
				output = iFile.getOutputStream();
				if (output != null) {
					JSONUtil.save(rootJSON, output, true);
				}
			} finally {
				IOUtil.closeQuietly(output, true);
			}
		}
	}

	/**
	 * 
	 * @param desc
	 * @param output
	 * @throws IOException
	 */
	public void write(WorkspaceDescription desc, OutputStream output) throws IOException {
		JSONObject rootJSON = rootToJSON(desc);
		if (rootJSON != null) {
			JSONUtil.save(rootJSON, output, true);
		}
	}

	/**
	 * Convert WorkspaceDescription to root JSONObject
	 * 
	 * @param desc
	 * @return
	 */
	protected JSONObject rootToJSON(WorkspaceDescription desc) {
		if (desc == null) {
			return null;
		}

		JSONObject rootJSON = new JSONObject();

		JSONObject descJSON = workspaceDescriptionToJSON(desc);
		rootJSON.put("WorkspaceDescription", descJSON);

		return rootJSON;
	}

	/**
	 * Convert WorkspaceDescription to WorkspaceDescription JSONObject.
	 * 
	 * @param desc
	 * @return
	 */
	protected JSONObject workspaceDescriptionToJSON(WorkspaceDescription desc) {
		if (desc == null) {
			return null;
		}
		JSONObject descJSON = new JSONObject();

		// "version" attribute
		String version = desc.getVersion();
		if (version == null || version.isEmpty()) {
			version = WorkspaceDescription.DEFAULT_VERSION;
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

		// "password" attribute
		String password = desc.getPassword();
		if (password != null) {
			descJSON.put("password", password);
		}

		// "description" attribute
		String description = desc.getDescription();
		if (description != null) {
			descJSON.put("description", description);
		}

		return descJSON;
	}

}
