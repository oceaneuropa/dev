package org.origin.core.workspace.internal.resource;

import java.io.File;

import org.origin.common.workingcopy.AbstractWorkingCopy;

public class ProjectWorkingCopy extends AbstractWorkingCopy<ProjectResource, ProjectDescription> {

	/**
	 * 
	 * @param file
	 */
	public ProjectWorkingCopy(File file) {
		super(file);
	}

	@Override
	protected ProjectDescription getRootElement(ProjectResource resource) {
		return resource.getProject();
	}

}
