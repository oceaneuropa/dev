package org.origin.common.resources.impl.misc;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;
import org.origin.common.io.IOUtil;
import org.origin.common.json.JSONUtil;
import org.origin.common.resources.IFile;
import org.origin.common.resources.WorkspaceDescription;

public class WorkspaceDescriptionReader {

	private static WorkspaceDescriptionReader INSTANCE = new WorkspaceDescriptionReader();

	public static WorkspaceDescriptionReader getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param iFile
	 * @return
	 * @throws IOException
	 */
	public WorkspaceDescription read(IFile iFile) throws IOException {
		InputStream input = null;
		try {
			input = iFile.getInputStream();
			JSONObject rootJSON = JSONUtil.load(input, true);
			WorkspaceDescription desc = jsonToRoot(rootJSON);
			return desc;
		} finally {
			IOUtil.closeQuietly(input, true);
		}
	}

	/**
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public WorkspaceDescription read(InputStream input) throws IOException {
		JSONObject rootJSON = JSONUtil.load(input, true);
		WorkspaceDescription desc = jsonToRoot(rootJSON);
		return desc;
	}

	/**
	 * Convert root JSONObject to WorkspaceDescription.
	 * 
	 * @param rootJSON
	 * @return
	 */
	protected WorkspaceDescription jsonToRoot(JSONObject rootJSON) {
		if (rootJSON == null) {
			return null;
		}

		WorkspaceDescription desc = null;
		if (rootJSON.has("WorkspaceDescription")) {
			JSONObject descJSON = rootJSON.getJSONObject("WorkspaceDescription");
			if (descJSON != null) {
				desc = jsonToWorkspaceDescription(descJSON);
			}
		}
		return desc;
	}

	/**
	 * Convert WorkspaceDescription JSONObject to WorkspaceDescription.
	 * 
	 * @param descJSON
	 * @return
	 */
	protected WorkspaceDescription jsonToWorkspaceDescription(JSONObject descJSON) {
		if (descJSON == null) {
			return null;
		}

		WorkspaceDescription desc = new WorkspaceDescription();

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

		// "password" attribute
		String password = null;
		if (descJSON.has("password")) {
			password = descJSON.getString("password");
		}
		desc.setPassword(password);

		// "description" attribute
		String description = null;
		if (descJSON.has("description")) {
			description = descJSON.getString("description");
		}
		desc.setDescription(description);

		return desc;
	}

}
