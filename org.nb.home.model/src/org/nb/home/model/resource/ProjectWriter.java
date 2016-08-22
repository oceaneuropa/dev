package org.nb.home.model.resource;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.nb.home.model.runtime.config.ProjectConfig;
import org.nb.home.model.runtime.config.ProjectHomeConfig;
import org.nb.home.model.runtime.config.ProjectNodeConfig;
import org.origin.common.json.JSONUtil;

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
public class ProjectWriter {

	/**
	 * Write contents of the resource to output stream.
	 * 
	 * @param resource
	 * @param output
	 * @throws IOException
	 */
	public void write(ProjectResource resource, OutputStream output) throws IOException {
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
	public void write(ProjectResource resource, OutputStream output, boolean closeOutputStream) throws IOException {
		JSONObject document = projectToDocument(resource);
		if (document != null) {
			JSONUtil.save(document, output, closeOutputStream);
		}
	}

	/**
	 * Convert project resource to JSON document.
	 * 
	 * @param resource
	 * @return
	 */
	protected JSONObject projectToDocument(ProjectResource resource) {
		if (resource == null) {
			return null;
		}

		JSONObject document = new JSONObject();

		ProjectConfig project = resource.getProject();
		if (project != null) {
			JSONObject projectConfigJSON = projectToJSON(project);
			if (projectConfigJSON != null) {
				document.put("ProjectConfig", projectConfigJSON);
			}
		}

		return document;
	}

	/**
	 * Convert Project object to JSONObject
	 * 
	 * @param project
	 * @return
	 */
	protected JSONObject projectToJSON(ProjectConfig project) {
		JSONObject projectConfigJSON = new JSONObject();

		// "projectId" attribute
		String projectId = project.getProjectId();
		if (projectId != null) {
			projectConfigJSON.put("projectId", projectId);
		}

		// "projectHomeConfig" array
		JSONArray projectHomeConfigArray = new JSONArray();
		{
			List<ProjectHomeConfig> projectHomeConfigs = project.getProjectHomeConfigs();
			int projectHomeConfigIndex = 0;
			for (Iterator<ProjectHomeConfig> homeConfigItor = projectHomeConfigs.iterator(); homeConfigItor.hasNext();) {
				ProjectHomeConfig projectHomeConfig = homeConfigItor.next();
				JSONObject projectHomeConfigJSON = projectHomeConfigToJSON(projectHomeConfig);
				if (projectHomeConfigJSON != null) {
					projectHomeConfigArray.put(projectHomeConfigIndex++, projectHomeConfigJSON);
				}
			}
		}
		projectConfigJSON.put("ProjectHomeConfig", projectHomeConfigArray);

		return projectConfigJSON;
	}

	/**
	 * Convert ProjectHomeConfig model to JSONObject
	 * 
	 * @param projectHomeConfig
	 * @return
	 */
	protected JSONObject projectHomeConfigToJSON(ProjectHomeConfig projectHomeConfig) {
		JSONObject projectHomeConfigJSON = new JSONObject();

		// "projectHomeId" attribute
		String projectHomeId = projectHomeConfig.getProjectHomeId();
		if (projectHomeId != null) {
			projectHomeConfigJSON.put("projectHomeId", projectHomeId);
		}

		// "projectNodeConfig" array
		JSONArray projectNodeConfigArray = new JSONArray();
		{
			List<ProjectNodeConfig> projectNodeConfigs = projectHomeConfig.getProjectNodeConfigs();
			int projectNodeConfigIndex = 0;
			for (Iterator<ProjectNodeConfig> projectNodeItor = projectNodeConfigs.iterator(); projectNodeItor.hasNext();) {
				ProjectNodeConfig projectNodeConfig = projectNodeItor.next();
				JSONObject projectNodeConfigJSON = projectNodeConfigToJSON(projectNodeConfig);
				if (projectNodeConfigJSON != null) {
					projectNodeConfigArray.put(projectNodeConfigIndex++, projectHomeConfigJSON);
				}
			}
		}
		projectHomeConfigJSON.put("ProjectNodeConfig", projectNodeConfigArray);

		return projectHomeConfigJSON;
	}

	/**
	 * Convert ProjectNodeConfig model to JSONObject
	 * 
	 * @param projectNodeConfig
	 * @return
	 */
	protected JSONObject projectNodeConfigToJSON(ProjectNodeConfig projectNodeConfig) {
		JSONObject projectNodeConfigJSON = new JSONObject();

		// "projectNodeId" attribute
		String projectNodeId = projectNodeConfig.getProjectNodeId();
		if (projectNodeId != null) {
			projectNodeConfigJSON.put("projectNodeId", projectNodeId);
		}

		return projectNodeConfigJSON;
	}

}
