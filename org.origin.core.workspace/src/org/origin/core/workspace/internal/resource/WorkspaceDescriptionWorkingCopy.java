package org.origin.core.workspace.internal.resource;

import java.io.File;

import org.origin.common.workingcopy.AbstractWorkingCopy;
import org.origin.core.workspace.IWorkspaceDescription;

public class WorkspaceDescriptionWorkingCopy extends AbstractWorkingCopy<WorkspaceDescriptionResource, IWorkspaceDescription> {

	/**
	 * 
	 * @param file
	 */
	public WorkspaceDescriptionWorkingCopy(File file) {
		super(file);
	}

	@Override
	protected IWorkspaceDescription getRootElement(WorkspaceDescriptionResource resource) {
		return resource.getWorkspaceDescription();
	}

}
