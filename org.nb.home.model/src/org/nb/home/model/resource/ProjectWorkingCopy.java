package org.nb.home.model.resource;

import java.io.IOException;
import java.net.URI;

import org.nb.home.model.runtime.config.ProjectConfig;
import org.origin.common.workingcopy.AbstractWorkingCopy;

public class ProjectWorkingCopy extends AbstractWorkingCopy<ProjectResource> {

	/**
	 * 
	 * @param uri
	 */
	public ProjectWorkingCopy(URI uri) {
		super(uri);
	}

	@Override
	protected <T> T getRootElement(ProjectResource resource, Class<T> elementClass) throws IOException {
		if (ProjectConfig.class.isAssignableFrom(elementClass)) {
			return (T) resource.getProject();
		}
		return super.getRootElement(resource, elementClass);
	}

}
