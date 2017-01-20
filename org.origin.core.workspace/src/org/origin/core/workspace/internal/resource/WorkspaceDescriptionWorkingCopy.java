package org.origin.core.workspace.internal.resource;

import java.io.IOException;
import java.net.URI;

import org.origin.common.workingcopy.AbstractWorkingCopy;
import org.origin.core.workspace.IWorkspaceDescription;

public class WorkspaceDescriptionWorkingCopy extends AbstractWorkingCopy<WorkspaceDescriptionResource> {

	/**
	 * 
	 * @param uri
	 */
	public WorkspaceDescriptionWorkingCopy(URI uri) {
		super(uri);
	}

	@Override
	protected <T> T getRootElement(WorkspaceDescriptionResource resource, Class<T> elementClass) throws IOException {
		if (IWorkspaceDescription.class.isAssignableFrom(elementClass)) {
			return (T) resource.getWorkspaceDescription();
		}
		return super.getRootElement(resource, elementClass);
	}

}
