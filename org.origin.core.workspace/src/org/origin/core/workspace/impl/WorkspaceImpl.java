package org.origin.core.workspace.impl;

import java.io.File;

import org.origin.core.workspace.IContainer;
import org.origin.core.workspace.IProject;
import org.origin.core.workspace.IResource;
import org.origin.core.workspace.Workspace;
import org.origin.core.workspace.WorkspaceRoot;

public class WorkspaceImpl implements Workspace {

	public static String MSG1 = "file is null";
	public static String MSG2 = "File '%s' exists, but is not a directory.";

	protected File workspaceDir;
	protected WorkspaceRootImpl root;

	/**
	 * 
	 * @param workspaceDir
	 */
	public WorkspaceImpl(File workspaceDir) {
		this.workspaceDir = workspaceDir;

		this.root = new WorkspaceRootImpl();
		this.root.setWorkspace(this);
		this.root.setFile(workspaceDir);
	}

	public File getWorkspaceDir() {
		return this.workspaceDir;
	}

	public WorkspaceRoot getRoot() {
		return this.root;
	}

	@Override
	public IResource createResource(IContainer container, File file) {
		assert (file != null) : MSG1;

		IResource resource = null;

		if (file.isDirectory()) {
			if (container instanceof WorkspaceRoot) {
				ProjectImpl newProject = new ProjectImpl();
				newProject.setWorkspace(this);
				newProject.setFile(file);

			} else if (container instanceof IProject) {
				FolderImpl newFolder = new FolderImpl();
				newFolder.setWorkspace(this);
				newFolder.setFile(file);
				resource = newFolder;
			}

		} else {
			FileImpl newFile = new FileImpl();
			newFile.setWorkspace(this);
			newFile.setFile(file);
			resource = newFile;
		}

		if (resource != null) {
			resource.adapt(Workspace.class, this);
			resource.adapt(File.class, file);
		}
		return resource;
	}

}
