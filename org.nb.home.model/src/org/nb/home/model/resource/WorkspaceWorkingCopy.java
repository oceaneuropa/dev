package org.nb.home.model.resource;

import java.io.IOException;
import java.net.URI;

import org.nb.home.model.runtime.config.WorkspaceConfig;
import org.origin.common.workingcopy.AbstractWorkingCopy;

public class WorkspaceWorkingCopy extends AbstractWorkingCopy<WorkspaceResource> {

	/**
	 * 
	 * @param file
	 */
	public WorkspaceWorkingCopy(URI uri) {
		super(uri);
	}

	@Override
	protected <T> T getRootElement(WorkspaceResource resource, Class<T> elementClass) throws IOException {
		if (WorkspaceConfig.class.isAssignableFrom(elementClass)) {
			return (T) resource.getWorkspace();
		}
		return super.getRootElement(resource, elementClass);
	}

}
