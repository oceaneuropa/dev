package org.origin.core.workspace.impl;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.origin.common.runtime.IStatus;
import org.origin.common.runtime.Status;
import org.origin.core.workspace.IWorkspace;
import org.origin.core.workspace.WorkspaceManagement;

public class WorkspaceManagementImpl implements WorkspaceManagement {

	protected File workspacesDir;
	protected Map<String, IWorkspace> workspaceMap = new LinkedHashMap<String, IWorkspace>();

	/**
	 * 
	 * @param workspacesDir
	 */
	public WorkspaceManagementImpl(File workspacesDir) {
		this.workspacesDir = workspacesDir;
	}

	public File getWorkspaceDir() {
		return this.workspacesDir;
	}

	@Override
	public IWorkspace[] getWorkspaces() {
		return null;
	}

	@Override
	public IWorkspace getWorkspace(String name) {
		return null;
	}

	@Override
	public IStatus createWorkspace(String name) {
		return Status.OK_STATUS;
	}

	@Override
	public IStatus deleteWorkspace(IWorkspace workspace) {
		return Status.OK_STATUS;
	}

}
