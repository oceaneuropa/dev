package org.origin.core.workspace.internal.resource;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.core.workspace.IWorkspaceDescription;

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
public class WorkspaceDescriptionWriter extends DescriptionWriter {

	/**
	 * Write contents of the resource to output stream.
	 * 
	 * @param resource
	 * @param output
	 * @throws IOException
	 */
	public void write(WorkspaceDescriptionResource resource, OutputStream output) throws IOException {
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
	public void write(WorkspaceDescriptionResource resource, OutputStream output, boolean closeOutputStream) throws IOException {
		JSONObject document = workspaceToDocument(resource);
		if (document != null) {
			JSONUtil.save(document, output, closeOutputStream);
		}
	}

	/**
	 * Convert workspace description resource to JSON document.
	 * 
	 * @param resource
	 * @return
	 */
	protected JSONObject workspaceToDocument(WorkspaceDescriptionResource resource) {
		if (resource == null) {
			return null;
		}

		JSONObject document = new JSONObject();

		IWorkspaceDescription workspaceDesc = resource.getWorkspaceDescription();
		if (workspaceDesc != null) {
			JSONObject workspaceDescJSON = workspaceDescriptionToJSON(workspaceDesc);
			if (workspaceDescJSON != null) {
				document.put("WorkspaceDescription", workspaceDescJSON);
			}
		}

		return document;
	}

	/**
	 * Convert WorkspaceDescription object to JSONObject.
	 * 
	 * @param workspaceDesc
	 * @return
	 */
	protected JSONObject workspaceDescriptionToJSON(IWorkspaceDescription workspaceDesc) {
		JSONObject workspaceDescJSON = new JSONObject();

		// "natureIds" array
		natureIdsToJson(workspaceDesc, workspaceDescJSON);

		// "properties" attribute
		propertiesToJson(workspaceDesc, workspaceDescJSON);

		return workspaceDescJSON;
	}

}
