package org.origin.core.workspace.internal.resource;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.core.workspace.IProjectDescription;

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
public class ProjectDescriptionWriter extends DescriptionWriter {

	/**
	 * Write contents of the resource to output stream.
	 * 
	 * @param resource
	 * @param output
	 * @throws IOException
	 */
	public void write(ProjectDescriptionResource resource, OutputStream output) throws IOException {
		write(resource, output, false);
	}

	/**
	 * Write contents of the resource to output stream.
	 * 
	 * @param resource
	 * @param output
	 * @param closeOutputStream
	 * @throws IOException
	 */
	public void write(ProjectDescriptionResource resource, OutputStream output, boolean closeOutputStream) throws IOException {
		JSONObject document = projectDescriptionToDocument(resource);
		if (document != null) {
			JSONUtil.save(document, output, closeOutputStream);
		}
	}

	/**
	 * Convert project description resource to JSON document.
	 * 
	 * @param resource
	 * @return
	 */
	protected JSONObject projectDescriptionToDocument(ProjectDescriptionResource resource) {
		if (resource == null) {
			return null;
		}

		JSONObject document = new JSONObject();

		IProjectDescription projectDesc = resource.getProjectDescription();
		if (projectDesc != null) {
			JSONObject projectDescJSON = projectDescriptionToJSON(projectDesc);
			if (projectDescJSON != null) {
				document.put("ProjectDescription", projectDescJSON);
			}
		}

		return document;
	}

	/**
	 * Convert ProjectDescription object to JSONObject
	 * 
	 * @param projectDesc
	 * @return
	 */
	protected JSONObject projectDescriptionToJSON(IProjectDescription projectDesc) {
		JSONObject projectDescJSON = new JSONObject();

		// "natureIds" array
		natureIdsToJson(projectDesc, projectDescJSON);

		// "properties" attribute
		propertiesToJson(projectDesc, projectDescJSON);

		return projectDescJSON;
	}

}
