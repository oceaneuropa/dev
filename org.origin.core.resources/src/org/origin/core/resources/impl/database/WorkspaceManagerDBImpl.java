package org.origin.core.resources.impl.database;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import org.origin.core.resources.IWorkspace;
import org.origin.core.resources.WorkspaceManager;
import org.osgi.framework.BundleContext;

public class WorkspaceManagerDBImpl implements WorkspaceManager {

	protected WorkspacesTableHandler workspaceTableHandler;

	public WorkspaceManagerDBImpl(String id) {
		this.workspaceTableHandler = new WorkspacesTableHandler(id);
	}

	protected Connection getConnection() {
		return null;
	}

	@Override
	public void start(BundleContext context) {

	}

	@Override
	public void stop(BundleContext context) {

	}

	@Override
	public List<String> getWorkspaces() throws IOException {

		return null;
	}

	@Override
	public IWorkspace openWorkspace(String name, String password) throws IOException {
		return null;
	}

	@Override
	public IWorkspace createWorkspace(String name, String password) throws IOException {
		return null;
	}

	@Override
	public boolean changeWorkspacePassword(String name, String oldPassword, String newPassword) throws IOException {
		return false;
	}

	@Override
	public boolean deleteWorkspace(String name, String password) throws IOException {
		return false;
	}

}
