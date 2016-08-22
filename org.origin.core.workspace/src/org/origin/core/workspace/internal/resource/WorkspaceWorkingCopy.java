package org.origin.core.workspace.internal.resource;

import java.io.File;

import org.origin.common.workingcopy.AbstractWorkingCopy;

public class WorkspaceWorkingCopy extends AbstractWorkingCopy<WorkspaceResource, WorkspaceDescription> {

	/**
	 * 
	 * @param file
	 */
	public WorkspaceWorkingCopy(File file) {
		super(file);
	}

	@Override
	protected WorkspaceDescription getRootElement(WorkspaceResource resource) {
		return resource.getWorkspace();
	}

}
