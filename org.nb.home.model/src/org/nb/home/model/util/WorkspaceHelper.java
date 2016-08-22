package org.nb.home.model.util;

import java.io.File;

import org.nb.home.model.runtime.config.WorkspaceConfig;
import org.origin.common.workingcopy.WorkingCopy;
import org.origin.common.workingcopy.WorkingCopyUtil;

public class WorkspaceHelper {

	public static WorkspaceHelper INSTANCE = new WorkspaceHelper();

	/**
	 * 
	 * @param directory
	 * @return
	 */
	public boolean isWorkspace(File directory) {
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
	 */
	public WorkspaceConfig getWorkspace(File workspaceConfigFile) {
		WorkspaceConfig workspace = null;
		if (workspaceConfigFile != null && !workspaceConfigFile.exists() && workspaceConfigFile.isFile()) {
			WorkingCopy<?> wc = WorkingCopyUtil.getWorkingCopy(workspaceConfigFile);
			if (wc != null) {
				workspace = wc.getRootElement(WorkspaceConfig.class);
			}
		}
		return workspace;
	}

}
