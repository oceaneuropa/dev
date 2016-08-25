package org.nb.home.model.util;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

import org.nb.home.model.HomeConstants;
import org.origin.common.env.SetupUtil;
import org.origin.core.workspace.WorkspaceConstants;

public class HomeSetupUtil {

	// -----------------------------------------------------------------------------------------
	// Home
	// -----------------------------------------------------------------------------------------
	/**
	 * 
	 * @param props
	 * @return
	 */
	public static Path getHomePath(Map<Object, Object> props) {
		return SetupUtil.getHomePath(props, HomeConstants.AGENT_HOME_DIR);
	}

	// -----------------------------------------------------------------------------------------
	// Workspace
	// -----------------------------------------------------------------------------------------
	/**
	 * Get workspaces path.
	 * 
	 * @param homePath
	 * @return
	 */
	public static Path getWorkspacesPath(Path homePath) {
		Path workspacesPath = null;
		if (homePath != null) {
			workspacesPath = homePath.resolve("workspaces");
		}
		return workspacesPath;
	}

	/**
	 * Get workspaces directory.
	 * 
	 * @param homePath
	 * @return
	 */
	public static File getWorkspacesFolder(Path homePath) {
		File workspacesFolder = null;
		Path workspacesPath = getWorkspacesPath(homePath);
		if (workspacesPath != null) {
			workspacesFolder = workspacesPath.toFile();
		}
		return workspacesFolder;
	}

	/**
	 * 
	 * @param workspaceDirectory
	 * @return
	 */
	public static File getWorkspaceConfigFile(File workspaceDirectory) {
		File metadataDir = new File(workspaceDirectory, WorkspaceConstants.METADATA_FOLDER);
		return new File(metadataDir, WorkspaceConstants.WORKSPACE_JSON);
	}

}
