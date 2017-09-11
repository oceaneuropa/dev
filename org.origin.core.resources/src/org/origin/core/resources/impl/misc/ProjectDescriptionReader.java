package org.origin.core.resources.impl.misc;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;
import org.origin.common.io.IOUtil;
import org.origin.common.json.JSONUtil;
import org.origin.core.resources.IFile;
import org.origin.core.resources.ProjectDescription;

public class ProjectDescriptionReader {

	private static ProjectDescriptionReader INSTANCE = new ProjectDescriptionReader();

	public static ProjectDescriptionReader getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param iFile
	 * @return
	 * @throws IOException
	 */
	public ProjectDescription read(IFile iFile) throws IOException {
		InputStream input = null;
		try {
			input = iFile.getInputStream();
			JSONObject rootJSON = JSONUtil.load(input, true);
			ProjectDescription desc = jsonToRoot(rootJSON);
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
	public ProjectDescription read(InputStream input) throws IOException {
		JSONObject rootJSON = JSONUtil.load(input, true);
		ProjectDescription desc = jsonToRoot(rootJSON);
		return desc;
	}

	/**
	 * Convert root JSONObject to ProjectDescription.
	 * 
	 * @param rootJSON
	 * @return
	 */
	protected ProjectDescription jsonToRoot(JSONObject rootJSON) {
		if (rootJSON == null) {
			return null;
		}

		ProjectDescription desc = null;
		if (rootJSON.has("ProjectDescription")) {
			JSONObject descJSON = rootJSON.getJSONObject("ProjectDescription");
			if (descJSON != null) {
				desc = jsonToProjectDescription(descJSON);
			}
		}
		return desc;
	}

	/**
	 * Convert ProjectDescription JSONObject to ProjectDescription.
	 * 
	 * @param descJSON
	 * @return
	 */
	protected ProjectDescription jsonToProjectDescription(JSONObject descJSON) {
		if (descJSON == null) {
			return null;
		}

		ProjectDescription desc = new ProjectDescription();

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
// public ProjectDescription read(File file) throws IOException {
// JSONObject rootJSON = JSONUtil.load(file);
// ProjectDescription desc = jsonToRoot(rootJSON);
// return desc;
// }