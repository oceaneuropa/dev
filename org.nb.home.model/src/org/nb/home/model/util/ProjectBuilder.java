package org.nb.home.model.util;

import java.io.File;
import java.io.IOException;

import org.nb.home.model.resource.ProjectResource;
import org.nb.home.model.runtime.config.ProjectConfig;

public class ProjectBuilder {

	public static ProjectBuilder INSTANCE = new ProjectBuilder();

	/**
	 * 
	 * @param projectConfigFile
	 * @param projectId
	 * @throws IOException
	 */
	public void createNewProjectConfigFile(File projectConfigFile, String projectId) throws IOException {
		ProjectResource resource = new ProjectResource(projectConfigFile.toURI());
		{
			ProjectConfig projectConfig = new ProjectConfig();
			projectConfig.setProjectId(projectId);
			resource.getContents().add(projectConfig);
		}
		resource.save();
	}

}
