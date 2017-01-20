package org.origin.core.workspace.internal.resource;

import java.io.IOException;
import java.net.URI;

import org.origin.common.workingcopy.AbstractWorkingCopy;
import org.origin.core.workspace.IProjectDescription;

public class ProjectDescriptionWorkingCopy extends AbstractWorkingCopy<ProjectDescriptionResource> {

	/**
	 * 
	 * @param uri
	 */
	public ProjectDescriptionWorkingCopy(URI uri) {
		super(uri);
	}

	@Override
	protected <T> T getRootElement(ProjectDescriptionResource resource, Class<T> elementClass) throws IOException {
		if (IProjectDescription.class.isAssignableFrom(elementClass)) {
			return (T) resource.getProjectDescription();
		}
		return super.getRootElement(resource, elementClass);
	}

}
