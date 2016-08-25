package org.origin.core.workspace.internal.resource;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.common.resource.Resource;
import org.origin.core.workspace.IProjectDescription;
import org.origin.core.workspace.impl.ProjectDescriptionImpl;

/*

Example:
{project}/.metadata/project.json
-----------------------------------------------------------------------
{
    "ProjectDescription": {
		"natureIds": [
			"org.nb.home.ProjectNature"
		],
		"properties": {
			"key1": "value1",
			"key2": "value2"
		}
	}
}
-----------------------------------------------------------------------

*/
public class ProjectDescriptionReader extends DescriptionReader {

	/**
	 * Read from input stream into the given resource.
	 * 
	 * @param resource
	 * @param inputStream
	 * @throws IOException
	 */
	public void read(Resource resource, InputStream inputStream) throws IOException {
		read(resource, inputStream, false);
	}

	/**
	 * Read from input stream into the given resource.
	 * 
	 * @param resource
	 * @param inputStream
	 * @param closeInputStream
	 * @throws IOException
	 */
	public void read(Resource resource, InputStream inputStream, boolean closeInputStream) throws IOException {
		IProjectDescription projectDesc = null;
		JSONObject document = JSONUtil.load(inputStream, closeInputStream);
		if (document != null) {
			projectDesc = documentToProjectDescription(document);
		}
		if (projectDesc != null) {
			resource.getContents().add(projectDesc);
		}
	}

	/**
	 * Converts an JSON document to a ProjecDescription object.
	 * 
	 * @param document
	 * @return
	 */
	protected IProjectDescription documentToProjectDescription(JSONObject document) {
		if (document == null) {
			return null;
		}

		IProjectDescription projectDesc = null;

		if (document.has("ProjectDescription")) {
			JSONObject projectDescJSON = document.getJSONObject("ProjectDescription");
			if (projectDescJSON != null) {
				projectDesc = jsonToProjectDescription(projectDescJSON);
			}
		}

		return projectDesc;
	}

	/**
	 * Converts JSON object to a ProjecDescription object.
	 * 
	 * @param projectDescJSON
	 * @return
	 */
	protected IProjectDescription jsonToProjectDescription(JSONObject projectDescJSON) {
		if (projectDescJSON == null) {
			return null;
		}

		IProjectDescription projectDesc = new ProjectDescriptionImpl();

		// "natureIds" string array
		jsonToNatureIds(projectDescJSON, projectDesc);

		// "properties" attribute
		jsonToProperties(projectDescJSON, projectDesc);

		return projectDesc;
	}

}
