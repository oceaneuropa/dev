package org.nb.home.model.util;

import java.io.File;
import java.nio.file.Path;

public class ProjectPathHelper {

	public static ProjectPathHelper INSTANCE = new ProjectPathHelper();

	/**
	 * 
	 * @param workspacePath
	 * @param projectId
	 * @return
	 */
	public Path getProjectPath(Path homePath, String projectId) {
		Path projectPath = null;
		Path workspacePath = HomeSetupUtil.getWorkspacesPath(homePath);
		if (workspacePath != null) {
			projectPath = workspacePath.resolve(projectId);
		}
		return projectPath;
	}

	/**
	 * Get {AGENT_HOME}/workspace/{projectId}
	 * 
	 * @param homePath
	 * @param projectId
	 * @return
	 */
	public File getProjectDirectory(Path homePath, String projectId) {
		File projectFile = null;
		Path projectPath = getProjectPath(homePath, projectId);
		if (projectPath != null) {
			projectFile = projectPath.toFile();
		}
		return projectFile;
	}

	// /**
	// * Get {AGENT_HOME}/workspace/{projectId}/META-INF/project.json file.
	// *
	// * @param homePath
	// * @param projectId
	// * @return
	// */
	// public File getProjectConfigFile(Path homePath, String projectId) {
	// File projectConfigFile = null;
	// Path projectPath = getProjectPath(homePath, projectId);
	// if (projectPath != null) {
	// Path projectConfigPath = projectPath.resolve(WorkspaceConstants.META_INF_FOLDER).resolve(WorkspaceConstants.PROJECT_JSON);
	// if (projectConfigPath != null) {
	// projectConfigFile = projectConfigPath.toFile();
	// }
	// }
	// return projectConfigFile;
	// }
	//
	// /**
	// * Get {AGENT_HOME}/workspace/{projectId}/META-INF/project.json file.
	// *
	// * @param projectDirectory
	// * @return
	// */
	// public File getProjectConfigFile(File projectDirectory) {
	// File mataInfFolder = new File(projectDirectory, WorkspaceConstants.META_INF_FOLDER);
	// return new File(mataInfFolder, WorkspaceConstants.PROJECT_JSON);
	// }

}
