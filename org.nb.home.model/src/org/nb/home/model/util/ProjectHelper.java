package org.nb.home.model.util;

import java.io.File;
import java.io.IOException;

import org.nb.home.model.resource.ProjectResourceFactory;
import org.nb.home.model.resource.ProjectWorkingCopy;
import org.nb.home.model.runtime.config.ProjectConfig;
import org.origin.common.workingcopy.WorkingCopyUtil;

public class ProjectHelper {

	public static ProjectHelper INSTANCE = new ProjectHelper();

	/**
	 * 
	 * @param projectConfigFile
	 * @return
	 * @throws IOException
	 */
	public ProjectConfig getProject(File projectConfigFile) throws IOException {
		ProjectConfig project = null;
		if (projectConfigFile != null && projectConfigFile.exists()) {
			ProjectWorkingCopy wc = WorkingCopyUtil.getWorkingCopy(projectConfigFile, ProjectResourceFactory.class, ProjectWorkingCopy.class);
			if (wc != null) {
				project = wc.getRootElement(ProjectConfig.class);
			}
		}
		return project;
	}

}
