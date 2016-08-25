package org.origin.core.workspace.internal.resource;

import java.io.File;

import org.origin.common.workingcopy.AbstractWorkingCopy;
import org.origin.core.workspace.IProjectDescription;

public class ProjectDescriptionWorkingCopy extends AbstractWorkingCopy<ProjectDescriptionResource, IProjectDescription> {

	/**
	 * 
	 * @param file
	 */
	public ProjectDescriptionWorkingCopy(File file) {
		super(file);
	}

	@Override
	protected IProjectDescription getRootElement(ProjectDescriptionResource resource) {
		return resource.getProjectDescription();
	}

}
