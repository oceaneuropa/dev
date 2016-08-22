package org.nb.home.model.resource;

import java.io.File;

import org.nb.home.model.runtime.config.ProjectConfig;
import org.origin.common.workingcopy.AbstractWorkingCopy;

public class ProjectWorkingCopy extends AbstractWorkingCopy<ProjectResource, ProjectConfig> {

	/**
	 * 
	 * @param file
	 */
	public ProjectWorkingCopy(File file) {
		super(file);
	}

	@Override
	protected ProjectConfig getRootElement(ProjectResource resource) {
		return resource.getProject();
	}

}
