package org.origin.core.resources.impl.misc;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONObject;
import org.origin.common.io.IOUtil;
import org.origin.common.json.JSONUtil;
import org.origin.core.resources.IFile;
import org.origin.core.resources.ProjectDescription;

public class ProjectDescriptionWriter {

	private static ProjectDescriptionWriter INSTANCE = new ProjectDescriptionWriter();

	public static ProjectDescriptionWriter getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param desc
	 * @param iFile
	 * @throws IOException
	 */
	public void write(ProjectDescription desc, IFile iFile) throws IOException {
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
	public void write(ProjectDescription desc, OutputStream output) throws IOException {
		JSONObject rootJSON = rootToJSON(desc);
		if (rootJSON != null) {
			JSONUtil.save(rootJSON, output, true);
		}
	}

	/**
	 * Convert ProjectDescription to root JSONObject
	 * 
	 * @param desc
	 * @return
	 */
	protected JSONObject rootToJSON(ProjectDescription desc) {
		if (desc == null) {
			return null;
		}

		JSONObject rootJSON = new JSONObject();

		JSONObject descJSON = projectDescriptionToJSON(desc);
		rootJSON.put("ProjectDescription", descJSON);

		return rootJSON;
	}

	/**
	 * Convert ProjectDescription to ProjectDescription JSONObject.
	 * 
	 * @param desc
	 * @return
	 */
	protected JSONObject projectDescriptionToJSON(ProjectDescription desc) {
		if (desc == null) {
			return null;
		}
		JSONObject descJSON = new JSONObject();

		// "version" attribute
		String version = desc.getVersion();
		if (version == null || version.isEmpty()) {
			version = ProjectDescription.DEFAULT_VERSION;
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

/// **
// *
// * @param desc
// * @param file
// * @throws IOException
// */
// public void write(ProjectDescription desc, File file) throws IOException {
// JSONObject rootJSON = rootToJSON(desc);
// if (rootJSON != null) {
// JSONUtil.save(rootJSON, file);
// }
// }
