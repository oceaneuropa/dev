package org.origin.core.workspace.internal.resource;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.common.resource.Resource;
import org.origin.core.workspace.IWorkspaceDescription;
import org.origin.core.workspace.impl.WorkspaceDescriptionImpl;

/*

Example:
{workspace}/.metadata/workspace.json
-----------------------------------------------------------------------
{
    "WorkspaceDescription": {
		"natureIds": [
			"org.nb.home.WorkspaceNature"
		]
		"properties": {
			"managementId": "101",
			"url": "http://127.0.0.1:9090",
			"username": "admin",
			"password": "123"
		}
	}
}
-----------------------------------------------------------------------

*/
public class WorkspaceDescriptionReader extends DescriptionReader {

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
		IWorkspaceDescription workspace = null;
		JSONObject document = JSONUtil.load(inputStream, closeInputStream);
		if (document != null) {
			workspace = documentToWorkspaceDescription(document);
		}
		if (workspace != null) {
			resource.getContents().add(workspace);
		}
	}

	/**
	 * Converts an JSON document to a WorkspaceDescription object.
	 * 
	 * @param document
	 * @return
	 */
	protected IWorkspaceDescription documentToWorkspaceDescription(JSONObject document) {
		if (document == null) {
			return null;
		}

		IWorkspaceDescription workspaceDesc = null;

		if (document.has("WorkspaceDescription")) {
			JSONObject workspaceDescJSON = document.getJSONObject("WorkspaceDescription");
			if (workspaceDescJSON != null) {
				workspaceDesc = jsonToWorkspaceDescription(workspaceDescJSON);
			}
		}

		return workspaceDesc;
	}

	/**
	 * Converts JSON object to a WorkspaceDescription object.
	 * 
	 * @param workspaceDescJSON
	 * @return
	 */
	protected IWorkspaceDescription jsonToWorkspaceDescription(JSONObject workspaceDescJSON) {
		if (workspaceDescJSON == null) {
			return null;
		}

		IWorkspaceDescription workspaceDesc = new WorkspaceDescriptionImpl();

		// "natureIds" string array
		jsonToNatureIds(workspaceDescJSON, workspaceDesc);

		// "properties" attribute
		jsonToProperties(workspaceDescJSON, workspaceDesc);

		return workspaceDesc;
	}

}
