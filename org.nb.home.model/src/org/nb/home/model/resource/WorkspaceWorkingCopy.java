package org.nb.home.model.resource;

import java.io.File;

import org.nb.home.model.runtime.config.WorkspaceConfig;
import org.origin.common.workingcopy.AbstractWorkingCopy;

public class WorkspaceWorkingCopy extends AbstractWorkingCopy<WorkspaceResource, WorkspaceConfig> {

	/**
	 * 
	 * @param file
	 */
	public WorkspaceWorkingCopy(File file) {
		super(file);
	}

	@Override
	protected WorkspaceConfig getRootElement(WorkspaceResource resource) {
		return resource.getWorkspace();
	}

}
