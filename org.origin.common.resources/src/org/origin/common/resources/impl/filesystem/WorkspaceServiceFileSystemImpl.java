package org.origin.common.resources.impl.filesystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// import org.orbit.fs.common.FileSystem;
import org.origin.common.resource.IPath;
import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.IWorkspaceService;
import org.origin.common.resources.WorkspaceDescription;
import org.osgi.framework.BundleContext;

public class WorkspaceServiceFileSystemImpl implements IWorkspaceService {

	// protected FileSystem fs;
	//
	// public WorkspaceServiceFileSystemImpl(FileSystem fileSystem) {
	// this.fs = fileSystem;
	// }

	@Override
	public void start(BundleContext context) throws IOException {
	}

	@Override
	public void stop(BundleContext context) throws IOException {
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

	/**
	 * 
	 * @param file
	 * @return
	 */
	protected boolean isWorkspace(IPath path) {
		// if (path != null && this.fs.isDirectory(path)) {
		// IPath dotMetadataPath = path.append(".metadata");
		// if (this.fs.isDirectory(dotMetadataPath)) {
		// IPath dotWorkspacePath = path.append(".workspace");
		// if (this.fs.exists(dotWorkspacePath) && !this.fs.isDirectory(dotWorkspacePath)) {
		// return true;
		// }
		// }
		// }
		return false;
	}

	@Override
	public List<String> getWorkspaces() throws IOException {
		List<String> names = new ArrayList<String>();
		// IPath[] paths = this.fs.listRoots();
		// for (IPath path : paths) {
		// if (isWorkspace(path)) {
		// String name = path.getLastSegment();
		// names.add(name);
		// }
		// }
		return names;
	}

	@Override
	public IWorkspace open(String name, String password) throws IOException {
		checkName(name);

		IWorkspace workspace = null;
		// synchronized (this.workspaces) {
		// for (IWorkspace currWorkspace : this.workspaces) {
		// if (name.equals(currWorkspace.getName())) {
		// workspace = currWorkspace;
		// break;
		// }
		// }
		// }
		// IPath[] paths = this.fs.listRoots();
		// for (IPath path : paths) {
		// if (isWorkspace(path)) {
		// if (name.equals(path.getLastSegment())) {
		// workspace = currWorkspace;
		// break;
		// }
		// }
		// }

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
		return null;
	}

	@Override
	public boolean changePassword(String name, String oldPassword, String newPassword) throws IOException {
		return false;
	}

	@Override
	public boolean delete(String name, String password) throws IOException {
		return false;
	}

}
