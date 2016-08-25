package org.origin.core.workspace.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.origin.core.workspace.IContainer;
import org.origin.core.workspace.IProject;
import org.origin.core.workspace.IProjectDescription;
import org.origin.core.workspace.IResource;
import org.origin.core.workspace.IWorkspace;
import org.origin.core.workspace.IWorkspaceDescription;
import org.origin.core.workspace.WorkspaceConstants;

public class WorkspaceImpl extends ContainerImpl implements IWorkspace {

	public static String MSG1 = "file is null";
	public static String MSG2 = "File '%s' exists, but is not a directory.";

	/**
	 * 
	 * @param workspaceDir
	 * @return
	 */
	public static File getWorkspaceDescriptionFile(File workspaceDir) {
		return new File(workspaceDir, WorkspaceConstants.METADATA_FOLDER + File.pathSeparator + WorkspaceConstants.WORKSPACE_JSON);
	}

	protected IWorkspaceDescription workspaceDescription;

	/**
	 * Store a table of project handles that have been requested from this root. This maps project id string to project handle.
	 */
	protected Map<String, IProject> projectHandlesMap = Collections.synchronizedMap(new LinkedHashMap<String, IProject>());

	public WorkspaceImpl() {
	}

	/**
	 * 
	 * @param file
	 */
	public WorkspaceImpl(File file) {
		super(file);
	}

	@Override
	public IResource createResource(IContainer container, File file) {
		assert (file != null) : MSG1;

		IResource resource = null;

		if (file.isDirectory()) {
			if (container instanceof IWorkspace) {
				ProjectImpl newProject = new ProjectImpl(file);
				newProject.setWorkspace(this);

			} else if (container instanceof IProject) {
				FolderImpl newFolder = new FolderImpl(file);
				newFolder.setWorkspace(this);
				resource = newFolder;
			}

		} else {
			FileImpl newFile = new FileImpl(file);
			newFile.setWorkspace(this);
			resource = newFile;
		}

		if (resource != null) {
			resource.adapt(IWorkspace.class, this);
			resource.adapt(File.class, file);
		}
		return resource;
	}

	@Override
	public int getType() {
		return IResource.WORKSPACE;
	}

	@Override
	public File getDescriptionFile() {
		return getWorkspaceDescriptionFile(this.file);
	}

	@Override
	public IContainer getParent() {
		// root doesn't have container
		return null;
	}

	@Override
	public IProject getProject() {
		// root doesn't have container project
		return null;
	}

	public File getWorkspaceDir() {
		return this.file;
	}

	/**
	 * Create a {workspace} folder and serialize the workspace description to {workspace}/.metadata/.workspace.json file.
	 */
	@Override
	public void create(IWorkspaceDescription workspaceDescription) {

	}

	@Override
	public void load() throws IOException {

	}

	@Override
	public void save() throws IOException {

	}

	@Override
	public IWorkspaceDescription getDescription() {
		return null;
	}

	@Override
	public void setDescription(IWorkspaceDescription description) {

	}

	@Override
	public void delete() {

	}

	@Override
	public synchronized IProject[] getProjects() {
		if (this.file == null || !this.file.exists() || !this.file.isDirectory()) {
			return EMPTY_PROJECTS;
		}

		IWorkspace workspace = getWorkspace();
		checkWorkspace(workspace);

		this.projectHandlesMap.clear();

		List<IProject> projects = new ArrayList<IProject>();
		File[] memberFiles = this.file.listFiles();
		if (memberFiles != null) {
			for (File memberFile : memberFiles) {
				if (memberFile.isDirectory()) {
					IProjectDescription projectDesc = ProjectImpl.loadProjectDescription(memberFile);
					if (projectDesc != null) {
						ProjectImpl project = new ProjectImpl(memberFile);
						project.setDescription(projectDesc);
						projects.add(project);
						this.projectHandlesMap.put(project.getName(), project);
					}
				}
			}
		}

		return projects.toArray(new IProject[projects.size()]);
	}

	@Override
	public synchronized IProject getProject(String projectName) {
		if (this.file == null || !this.file.exists() || !this.file.isDirectory()) {
			return null;
		}

		IWorkspace workspace = getWorkspace();
		checkWorkspace(workspace);

		IProject project = this.projectHandlesMap.get(projectName);
		if (project == null) {
			File projectDir = new File(this.file, projectName);

			// not a directory. projectId is invalid.
			if (!projectDir.isDirectory()) {
				return null;
			}

			if (projectDir.exists()) {
				IProjectDescription projectDesc = ProjectImpl.loadProjectDescription(projectDir);
				if (projectDesc != null) {
					ProjectImpl newProject = new ProjectImpl(projectDir);
					newProject.setDescription(projectDesc);

					this.projectHandlesMap.put(projectName, project);
				}
			}
		}

		return project;
	}

}
