package org.origin.core.workspace.internal.resource;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.json.JSONObject;
import org.origin.common.json.JSONUtil;

/*

Example:
-----------------------------------------------------------------------
{
    "WorkspaceConfig": {
    	"name": "workspace1",
		"managementId": "101",
		"properties": {
			"url": "http://127.0.0.1:9090",
			"username": "admin",
			"password": "123"
		}
	}
}
-----------------------------------------------------------------------

*/
public class WorkspaceWriter {

	/**
	 * Write contents of the resource to output stream.
	 * 
	 * @param resource
	 * @param output
	 * @throws IOException
	 */
	public void write(WorkspaceResource resource, OutputStream output) throws IOException {
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
	public void write(WorkspaceResource resource, OutputStream output, boolean closeOutputStream) throws IOException {
		JSONObject document = workspaceToDocument(resource);
		if (document != null) {
			JSONUtil.save(document, output, closeOutputStream);
		}
	}

	/**
	 * Convert workspace resource to JSON document.
	 * 
	 * @param resource
	 * @return
	 */
	protected JSONObject workspaceToDocument(WorkspaceResource resource) {
		if (resource == null) {
			return null;
		}

		JSONObject document = new JSONObject();

		WorkspaceDescription workspace = resource.getWorkspace();
		if (workspace != null) {
			JSONObject workspaceJSON = workspaceToJSON(workspace);
			if (workspaceJSON != null) {
				document.put("WorkspaceConfig", workspaceJSON);
			}
		}

		return document;
	}

	/**
	 * Convert Workspace object to JSONObject.
	 * 
	 * @param workspace
	 * @return
	 */
	protected JSONObject workspaceToJSON(WorkspaceDescription workspace) {
		JSONObject workspaceJSON = new JSONObject();

		// "name" attribute
		String name = workspace.getName();
		if (name != null) {
			workspaceJSON.put("name", name);
		}

		// "managementId" attribute
		String managementId = workspace.getManagementId();
		if (managementId != null) {
			workspaceJSON.put("managementId", managementId);
		}

		// "properties" attribute
		Map<String, Object> properties = workspace.getProperties();
		String propertiesString = JSONUtil.toJsonString(properties);
		if (propertiesString != null) {
			workspaceJSON.put("properties", propertiesString);
		}

		return workspaceJSON;
	}

}
