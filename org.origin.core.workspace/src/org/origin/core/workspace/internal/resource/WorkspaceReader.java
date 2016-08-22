package org.origin.core.workspace.internal.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.common.resource.Resource;

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
public class WorkspaceReader {

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
		WorkspaceDescription workspace = null;
		JSONObject document = JSONUtil.load(inputStream, closeInputStream);
		if (document != null) {
			workspace = documentToWorkspace(document);
		}
		if (workspace != null) {
			resource.getContents().add(workspace);
		}
	}

	/**
	 * Converts an JSON document to a Workspace object.
	 * 
	 * @param document
	 * @return
	 */
	protected WorkspaceDescription documentToWorkspace(JSONObject document) {
		if (document == null) {
			return null;
		}

		WorkspaceDescription workspace = null;

		if (document.has("WorkspaceConfig")) {
			JSONObject workspaceJSON = document.getJSONObject("WorkspaceConfig");
			if (workspaceJSON != null) {
				workspace = jsonToWorkspace(workspaceJSON);
			}
		}

		return workspace;
	}

	/**
	 * Converts JSON object to a Workspace object.
	 * 
	 * @param workspaceJSON
	 * @return
	 */
	protected WorkspaceDescription jsonToWorkspace(JSONObject workspaceJSON) {
		if (workspaceJSON == null) {
			return null;
		}

		WorkspaceDescription workspace = new WorkspaceDescription();

		// "name" attribute
		String name = null;
		if (workspaceJSON.has("name")) {
			name = workspaceJSON.getString("name");
		}
		workspace.setName(name);

		// "managementId" attribute
		String managementId = null;
		if (workspaceJSON.has("managementId")) {
			managementId = workspaceJSON.getString("managementId");
		}
		workspace.setManagementId(managementId);

		// "properties" attribute
		Map<String, Object> properties = null;
		if (workspaceJSON.has("properties")) {
			String propertiesString = workspaceJSON.getString("properties");
			if (propertiesString != null) {
				properties = JSONUtil.toProperties(propertiesString);
			}
		}
		if (properties != null) {
			workspace.setProperties(properties);
		}

		return workspace;
	}

}
