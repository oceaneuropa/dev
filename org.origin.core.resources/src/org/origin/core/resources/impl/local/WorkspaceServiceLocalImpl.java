package org.origin.core.resources.impl.filesystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.origin.core.resources.IWorkspace;
import org.origin.core.resources.WorkspaceDescription;
import org.origin.core.resources.WorkspaceManager;
import org.origin.core.resources.impl.misc.WorkspaceNameComparator;
import org.osgi.framework.BundleContext;

public class WorkspaceManagerFSImpl implements WorkspaceManager {

	protected File workspacesFolder;
	protected List<IWorkspace> workspaces = new ArrayList<IWorkspace>();

	/**
	 * 
	 * @param workspacesFolder
	 */
	public WorkspaceManagerFSImpl(File workspacesFolder) {
		this.workspacesFolder = workspacesFolder;
	}

	@Override
	public void start(BundleContext context) {
		loadWorkspaces();
	}

	@Override
	public void stop(BundleContext context) {
		synchronized (this.workspaces) {
			this.workspaces.clear();
		}
	}

	protected void loadWorkspaces() {
		synchronized (this.workspaces) {
			this.workspaces.clear();

			File[] memberFiles = this.workspacesFolder.listFiles();
			for (File memberFile : memberFiles) {
				if (isWorkspace(memberFile)) {
					IWorkspace workspace = loadWorkspace(memberFile);
					if (workspace != null) {
						this.workspaces.add(workspace);
					}
				}
			}

			Collections.sort(this.workspaces, WorkspaceNameComparator.ASC);
		}
	}

	protected boolean isWorkspace(File file) {
		if (file != null && file.isDirectory()) {
			File dotMetadataFolder = new File(file, ".metadata");
			if (dotMetadataFolder.isDirectory()) {
				File dotWorkspaceFile = new File(dotMetadataFolder, ".workspace");
				if (dotWorkspaceFile.exists()) {
					return true;
				}
			}
		}
		return false;
	}

	protected IWorkspace loadWorkspace(File file) {
		IWorkspace workspace = new WorkspaceFSImpl(file);
		return workspace;
	}

	@Override
	public List<String> getWorkspaces() throws IOException {
		List<String> workspaceNames = new ArrayList<String>();
		return workspaceNames;
	}

	protected void checkName(String name) throws IOException {
		if (name == null || name.isEmpty()) {
			throw new IOException("Workspace name is empyt.");
		}
	}

	@Override
	public IWorkspace openWorkspace(String name, String password) throws IOException {
		checkName(name);

		IWorkspace matchedWorkspace = null;
		synchronized (this.workspaces) {
			for (IWorkspace workspace : this.workspaces) {
				if (name.equals(workspace.getName())) {
					matchedWorkspace = workspace;
					break;
				}
			}
		}

		if (matchedWorkspace != null) {
			boolean isPasswordRequired = false;
			boolean matchPassword = false;
			WorkspaceDescription desc = matchedWorkspace.getDescription();
			if (desc != null && desc.getPassword() != null && !desc.getPassword().isEmpty()) {
				isPasswordRequired = true;
				if (desc.getPassword().equals(password)) {
					matchPassword = true;
				}
			}
			if (isPasswordRequired) {
				if (matchPassword) {
					return matchedWorkspace;
				} else {
					throw new IOException("Wrong password");
				}
			} else {
				return matchedWorkspace;
			}
		} else {
			throw new IOException("Workspace does not exist.");
		}
	}

	@Override
	public IWorkspace createWorkspace(String name, String password) throws IOException {
		checkName(name);

		WorkspaceDescription desc = new WorkspaceDescription();
		desc.setId(name);
		desc.setName(name);
		desc.setPassword(password);

		IWorkspace workspace = new WorkspaceFSImpl();
		workspace.create(desc);

		synchronized (this.workspaces) {
			this.workspaces.add(workspace);
			Collections.sort(this.workspaces, WorkspaceNameComparator.ASC);
		}

		return workspace;
	}

	@Override
	public boolean changeWorkspacePassword(String name, String oldPassword, String newPassword) throws IOException {
		checkName(name);

		IWorkspace matchedWorkspace = openWorkspace(name, oldPassword);
		if (matchedWorkspace != null) {
			WorkspaceDescription desc = matchedWorkspace.getDescription();

			if (desc == null) {
				desc = new WorkspaceDescription();
				desc.setId(name);
				desc.setName(name);
			}
			desc.setPassword(newPassword);

			matchedWorkspace.setDescription(desc);
		}

		return false;
	}

	@Override
	public boolean deleteWorkspace(String name, String password) throws IOException {
		checkName(name);

		IWorkspace matchedWorkspace = openWorkspace(name, password);
		if (matchedWorkspace != null) {
			matchedWorkspace.dispose();
			return matchedWorkspace.delete();
		}

		return false;
	}

}
