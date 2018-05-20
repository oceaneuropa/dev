package org.origin.common.resources.impl.local;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.IWorkspaceService;
import org.origin.common.resources.ResourcesFactory;
import org.origin.common.resources.WorkspaceDescription;
import org.origin.common.resources.impl.misc.WorkspaceNameComparator;
import org.osgi.framework.BundleContext;

public class WorkspaceServiceLocalImpl implements IWorkspaceService {

	protected File workspacesFolder;
	protected List<IWorkspace> workspaces = new ArrayList<IWorkspace>();

	/**
	 * 
	 * @param workspacesFolder
	 */
	public WorkspaceServiceLocalImpl(File workspacesFolder) {
		this.workspacesFolder = workspacesFolder;
	}

	@Override
	public void start(BundleContext context) throws IOException {
		load();
	}

	@Override
	public void stop(BundleContext context) throws IOException {
		synchronized (this.workspaces) {
			this.workspaces.clear();
		}
	}

	protected void load() {
		synchronized (this.workspaces) {
			this.workspaces.clear();

			File[] memberFiles = this.workspacesFolder.listFiles();
			if (memberFiles != null) {
				for (File memberFile : memberFiles) {
					if (isWorkspace(memberFile)) {
						IWorkspace workspace = ResourcesFactory.getInstance().createWorkspace(memberFile);
						if (workspace != null) {
							this.workspaces.add(workspace);
						}
					}
				}				
			}

			Collections.sort(this.workspaces, WorkspaceNameComparator.ASC);
		}
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	protected boolean isWorkspace(File file) {
		if (file != null && file.isDirectory()) {
			File dotMetadataFolder = new File(file, ".metadata");
			if (dotMetadataFolder.isDirectory()) {
				File dotWorkspaceFile = new File(dotMetadataFolder, ".workspace");
				if (dotWorkspaceFile.isFile()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param name
	 * @throws IOException
	 */
	protected void checkName(String name) throws IOException {
		if (name == null || name.isEmpty()) {
			throw new IOException("Workspace name is empyt.");
		}
	}

	@Override
	public List<String> getWorkspaces() throws IOException {
		List<String> names = new ArrayList<String>();
		synchronized (this.workspaces) {
			for (IWorkspace workspace : this.workspaces) {
				names.add(workspace.getName());
			}
		}
		return names;
	}

	@Override
	public IWorkspace open(String name, String password) throws IOException {
		checkName(name);

		IWorkspace workspace = null;
		synchronized (this.workspaces) {
			for (IWorkspace currWorkspace : this.workspaces) {
				if (name.equals(currWorkspace.getName())) {
					workspace = currWorkspace;
					break;
				}
			}
		}

		if (workspace == null) {
			throw new IOException("Workspace does not exist.");
		}

		boolean isPasswordRequired = false;
		boolean matchPassword = false;
		WorkspaceDescription desc = workspace.getDescription();
		if (desc != null && desc.getPassword() != null && !desc.getPassword().isEmpty()) {
			isPasswordRequired = true;
			if (desc.getPassword().equals(password)) {
				matchPassword = true;
			}
		}
		if (isPasswordRequired) {
			if (matchPassword) {
				return workspace;
			} else {
				throw new IOException("Wrong password");
			}
		} else {
			return workspace;
		}
	}

	@Override
	public IWorkspace create(String name, String password) throws IOException {
		checkName(name);

		WorkspaceDescription desc = new WorkspaceDescription();
		desc.setId(name);
		desc.setName(name);
		desc.setPassword(password);
		desc.setWorkspaceFolderPath(this.workspacesFolder.getAbsolutePath());

		IWorkspace workspace = new WorkspaceLocalImpl();
		workspace.create(desc);

		synchronized (this.workspaces) {
			this.workspaces.add(workspace);
			Collections.sort(this.workspaces, WorkspaceNameComparator.ASC);
		}

		return workspace;
	}

	@Override
	public boolean changePassword(String name, String oldPassword, String newPassword) throws IOException {
		checkName(name);

		IWorkspace workspace = open(name, oldPassword);
		if (workspace != null) {
			WorkspaceDescription desc = workspace.getDescription();
			if (desc == null) {
				desc = new WorkspaceDescription();
				desc.setId(name);
				desc.setName(name);
			}
			desc.setPassword(newPassword);

			workspace.setDescription(desc);
		}

		return false;
	}

	@Override
	public boolean delete(String name, String password) throws IOException {
		checkName(name);

		IWorkspace workspace = open(name, password);
		if (workspace != null) {
			workspace.dispose();
			return workspace.delete();
		}

		return false;
	}

}