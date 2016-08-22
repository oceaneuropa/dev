package org.nb.home.model.resource;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.nb.home.model.runtime.config.ProjectConfig;
import org.nb.home.model.runtime.config.ProjectHomeConfig;
import org.nb.home.model.runtime.config.ProjectNodeConfig;
import org.origin.common.json.JSONUtil;
import org.origin.common.resource.Resource;

/*

Example:
-----------------------------------------------------------------------
{

    "ProjectConfig": {
		"projectId": "project1",
		"ProjectHomeConfig": [
			{
				"projectHomeId": "homeConfig1",
				"ProjectNodeConfig": [
					{
						"projectNodeId": "Node1"
					},
					{
						"projectNodeId": "Node2"
					}
				]
			},
			{
				"projectHomeId": "homeConfig2",
				"ProjectNodeConfig": [
					{
						"projectNodeId": "Node3"
					},
					{
						"projectNodeId": "Node4"
					}
				]
			}
		]
	}
}
-----------------------------------------------------------------------

*/
public class ProjectReader {

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
		ProjectConfig project = null;
		JSONObject document = JSONUtil.load(inputStream, closeInputStream);
		if (document != null) {
			project = documentToProject(document);
		}
		if (project != null) {
			resource.getContents().add(project);
		}
	}

	/**
	 * Converts an JSON document to a Project object.
	 * 
	 * @param document
	 * @return
	 */
	protected ProjectConfig documentToProject(JSONObject document) {
		if (document == null) {
			return null;
		}

		ProjectConfig project = null;

		if (document.has("ProjectConfig")) {
			JSONObject projectConfigJSON = document.getJSONObject("ProjectConfig");
			if (projectConfigJSON != null) {
				project = jsonToProject(projectConfigJSON);
			}
		}

		return project;
	}

	/**
	 * Converts JSON object to a Project object.
	 * 
	 * @param projectConfigJSON
	 * @return
	 */
	protected ProjectConfig jsonToProject(JSONObject projectConfigJSON) {
		if (projectConfigJSON == null) {
			return null;
		}

		ProjectConfig projectConfig = new ProjectConfig();

		// "projectId" attribute
		String projectId = null;
		if (projectConfigJSON.has("projectId")) {
			projectId = projectConfigJSON.getString("projectId");
		}
		projectConfig.setProjectId(projectId);

		// "ProjectHomeConfig" array
		if (projectConfigJSON.has("ProjectHomeConfig")) {
			JSONArray projectHomeConfigArray = projectConfigJSON.getJSONArray("ProjectHomeConfig");
			if (projectHomeConfigArray != null) {
				int length = projectHomeConfigArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject projectHomeConfigJSON = projectHomeConfigArray.getJSONObject(i);
					if (projectHomeConfigJSON != null) {
						ProjectHomeConfig projectHomeConfig = jsonToProjectHomeConfig(projectHomeConfigJSON);
						if (projectHomeConfig != null) {
							projectConfig.addProjectHomeConfig(projectHomeConfig);
						}
					}
				}
			}
		}

		return projectConfig;
	}

	/**
	 * Convert projectHomeConfig JSONObject to ProjectHomeConfig.
	 * 
	 * @param projectHomeConfigJSON
	 * @return
	 */
	protected ProjectHomeConfig jsonToProjectHomeConfig(JSONObject projectHomeConfigJSON) {
		if (projectHomeConfigJSON == null) {
			return null;
		}

		ProjectHomeConfig projectHomeConfig = new ProjectHomeConfig();

		// "projectHomeId" attribute
		String projectHomeId = null;
		if (projectHomeConfigJSON.has("projectHomeId")) {
			projectHomeId = projectHomeConfigJSON.getString("projectHomeId");
		}
		projectHomeConfig.setProjectHomeId(projectHomeId);

		// "ProjectNodeConfig" array
		if (projectHomeConfigJSON.has("ProjectNodeConfig")) {
			JSONArray projectNodeConfigArray = projectHomeConfigJSON.getJSONArray("ProjectNodeConfig");
			if (projectNodeConfigArray != null) {
				int length = projectNodeConfigArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject projectNodeConfigJSON = projectNodeConfigArray.getJSONObject(i);
					if (projectNodeConfigJSON != null) {
						ProjectNodeConfig projectNodeConfig = jsonToProjectNodeConfig(projectNodeConfigJSON);
						if (projectNodeConfig != null) {
							projectHomeConfig.addProjectNodeConfig(projectNodeConfig);
						}
					}
				}
			}
		}

		return projectHomeConfig;
	}

	/**
	 * Convert projectNodeConfig JSONObject to ProjectNodeConfig.
	 * 
	 * @param projectNodeConfigJSON
	 * @return
	 */
	protected ProjectNodeConfig jsonToProjectNodeConfig(JSONObject projectNodeConfigJSON) {
		if (projectNodeConfigJSON == null) {
			return null;
		}

		ProjectNodeConfig projectNodeConfig = new ProjectNodeConfig();

		// "projectNodeId" attribute
		String projectNodeId = null;
		if (projectNodeConfigJSON.has("projectNodeId")) {
			projectNodeId = projectNodeConfigJSON.getString("projectNodeId");
		}
		projectNodeConfig.setProjectNodeId(projectNodeId);

		return projectNodeConfig;
	}

}
