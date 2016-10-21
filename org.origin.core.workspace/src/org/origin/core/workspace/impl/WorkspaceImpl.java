package org.origin.core.workspace.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.origin.common.workingcopy.WorkingCopy;
import org.origin.common.workingcopy.WorkingCopyUtil;
import org.origin.core.workspace.IContainer;
import org.origin.core.workspace.IProject;
import org.origin.core.workspace.IResource;
import org.origin.core.workspace.IWorkspace;
import org.origin.core.workspace.IWorkspaceDescription;
import org.origin.core.workspace.WorkspaceConstants;
import org.origin.core.workspace.internal.resource.WorkspaceDescriptionResource;
import org.origin.core.workspace.nature.WorkspaceNature;

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

	/**
	 * 
	 * @param workspaceDir
	 * @return
	 */
	public static IWorkspaceDescription loadWorkspaceDescription(File workspaceDir) {
		IWorkspaceDescription projectDesc = null;
		if (workspaceDir != null && workspaceDir.isDirectory()) {
			File workspaceDescFile = getWorkspaceDescriptionFile(workspaceDir);
			if (workspaceDescFile != null && workspaceDescFile.exists()) {
				WorkingCopy<?> workingCopy = WorkingCopyUtil.getWorkingCopy(workspaceDescFile);
				if (workingCopy != null) {
					projectDesc = workingCopy.getRootElement(IWorkspaceDescription.class);
				}
			}
		}
		return projectDesc;
	}

	protected IWorkspaceDescription workspaceDesc;

	/**
	 * Store a table of project handles that have been requested from this root. This maps project id string to project handle.
	 */
	protected Map<String, IProject> projectHandlesMap = Collections.synchronizedMap(new LinkedHashMap<String, IProject>());

	/**
	 * New instance for a workspace handle.
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

	/**
	 * Create new workspace from the workspace handle.
	 * 
	 * 1. Create a {workspace} folder.
	 * 
	 * 2. Serialize the workspace description to {workspace}/.metadata/workspace.json file.
	 * 
	 * 3. Load natures and configure natures.
	 */
	@Override
	public void create(IWorkspaceDescription workspaceDesc) {
		if (workspaceDesc == null) {
			workspaceDesc = new WorkspaceDescriptionImpl();
		}

		// 1. Create workspace dir
		if (!this.file.exists()) {
			this.file.mkdirs();
		}

		// 2. Save description file and load description from working copy.
		File workspaceDescFile = getWorkspaceDescriptionFile(this.file);
		WorkspaceDescriptionResource workspaceDescResource = new WorkspaceDescriptionResource(workspaceDescFile.toURI());
		workspaceDescResource.getContents().add(workspaceDesc);
		try {
			workspaceDescResource.save(workspaceDescFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.workspaceDesc = loadWorkspaceDescription(this.file);
		assert (this.workspaceDesc != null) : "Cannot load workspace description.";

		// 3. Load natures and configure natures.
		// do nature.load()
		// do nature.configure()
		for (String natureId : this.workspaceDesc.getNatureIds()) {
			getNatureHandler().loadNature(natureId, WorkspaceNature.class);
			getNatureHandler().configureNature(natureId, WorkspaceNature.class);
		}
	}

	@Override
	public void load() throws IOException {
		this.workspaceDesc = loadWorkspaceDescription(this.file);

		if (this.workspaceDesc != null) {
			// do nature.load()
			for (String natureId : this.workspaceDesc.getNatureIds()) {
				getNatureHandler().loadNature(natureId, WorkspaceNature.class);
			}
		}
	}

	@Override
	public void save() throws IOException {
		if (this.workspaceDesc != null) {
			// do nature.save()
			for (String natureId : this.workspaceDesc.getNatureIds()) {
				getNatureHandler().saveNature(natureId, WorkspaceNature.class);
			}

			// save workspace description
			WorkingCopy<?> workingCopy = WorkingCopyUtil.getWorkingCopy(this.workspaceDesc);
			if (workingCopy != null) {
				workingCopy.save();
			}
		}
	}

	@Override
	public IWorkspaceDescription getDescription() {
		return this.workspaceDesc;
	}

	@Override
	public void setDescription(IWorkspaceDescription workspaceDesc) {
		assert (workspaceDesc != null) : "workspaceDesc is null";

		// 1. configure natures
		// (1) for added natures: nature.configure(); nature.load();
		// (2) for removed natures: nature.deconfigure();
		getNatureHandler().configureNatures(this.workspaceDesc, workspaceDesc, WorkspaceNature.class);

		// 2. keep reference to the new description
		this.workspaceDesc = workspaceDesc;

		// 3. save description file and all natures
		try {
			// do nature.save()
			for (String natureId : this.workspaceDesc.getNatureIds()) {
				getNatureHandler().saveNature(natureId, WorkspaceNature.class);
			}

			// save workspace description
			WorkingCopy<?> workingCopy = WorkingCopyUtil.getWorkingCopy(this.workspaceDesc);
			if (workingCopy != null) {
				workingCopy.save();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		File[] files = this.file.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					ProjectImpl project = new ProjectImpl(file);
					File projectDescFile = project.getDescriptionFile();
					if (projectDescFile != null && projectDescFile.exists()) {
						try {
							project.load();
							projects.add(project);
							this.projectHandlesMap.put(project.getName(), project);
						} catch (IOException e) {
							e.printStackTrace();
						}
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

			if (projectDir.isDirectory()) {
				project = new ProjectImpl(projectDir);
				File projectDescFile = project.getDescriptionFile();
				if (projectDescFile != null && projectDescFile.isFile()) {
					try {
						project.load();
						this.projectHandlesMap.put(projectName, project);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return project;
	}

}
