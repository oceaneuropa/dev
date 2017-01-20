package org.nb.home.model.util;

import java.io.File;
import java.io.IOException;

import org.nb.home.model.runtime.config.WorkspaceConfig;
import org.origin.common.workingcopy.WorkingCopy;
import org.origin.common.workingcopy.WorkingCopyUtil;

public class WorkspaceHelper {

	public static WorkspaceHelper INSTANCE = new WorkspaceHelper();

	/**
	 * 
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	public boolean isWorkspace(File directory) throws IOException {
		if (directory != null && directory.isDirectory()) {
			File workspaceConfigFile = HomeSetupUtil.getWorkspaceConfigFile(directory);
			WorkspaceConfig workspace = getWorkspace(workspaceConfigFile);
			if (workspace != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param workspaceConfigFile
	 * @return
	 * @throws IOException
	 */
	public WorkspaceConfig getWorkspace(File workspaceConfigFile) throws IOException {
		WorkspaceConfig workspace = null;
		if (workspaceConfigFile != null && !workspaceConfigFile.exists() && workspaceConfigFile.isFile()) {
			WorkingCopy wc = WorkingCopyUtil.getWorkingCopy(workspaceConfigFile);
			if (wc != null) {
				workspace = wc.getRootElement(WorkspaceConfig.class);
			}
		}
		return workspace;
	}

}
