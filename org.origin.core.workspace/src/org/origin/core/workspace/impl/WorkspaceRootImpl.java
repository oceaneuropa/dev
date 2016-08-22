package org.origin.core.workspace.impl;

import java.io.File;
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
import org.origin.core.workspace.Workspace;
import org.origin.core.workspace.WorkspaceConstants;
import org.origin.core.workspace.WorkspaceRoot;
import org.origin.core.workspace.internal.resource.ProjectDescription;

public class WorkspaceRootImpl extends ContainerImpl implements WorkspaceRoot {

	/**
	 * Store a table of project handles that have been requested from this root. This maps project id string to project handle.
	 */
	protected Map<String, IProject> projectMap = Collections.synchronizedMap(new LinkedHashMap<String, IProject>());

	@Override
	public int getType() {
		return IResource.ROOT;
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

	@Override
	public synchronized IProject[] getProjects() {
		if (this.file == null || !this.file.exists() || !this.file.isDirectory()) {
			return EMPTY_PROJECTS;
		}

		Workspace workspace = getWorkspace();
		checkWorkspace(workspace);

		this.projectMap.clear();

		List<IProject> projects = new ArrayList<IProject>();
		File[] memberFiles = this.file.listFiles();
		if (memberFiles != null) {
			for (File memberFile : memberFiles) {
				if (memberFile.isDirectory()) {
					ProjectDescription projectDesc = getProjectDescription(memberFile);
					if (projectDesc != null) {
						IResource resource = workspace.createResource(this, memberFile);
						if (resource instanceof IProject) {
							IProject project = (IProject) resource;
							projects.add(project);

							String projectId = projectDesc.getProjectId();
							this.projectMap.put(projectId, project);
						}
					}
				}
			}
		}

		return projects.toArray(new IProject[projects.size()]);
	}

	@Override
	public synchronized IProject getProject(String projectId) {
		if (this.file == null || !this.file.exists() || !this.file.isDirectory()) {
			return null;
		}

		Workspace workspace = getWorkspace();
		checkWorkspace(workspace);

		IProject project = this.projectMap.get(projectId);
		if (project == null) {
			File projectDir = new File(this.file, projectId);

			if (projectDir.exists() && projectDir.isDirectory()) {
				ProjectDescription projectDesc = getProjectDescription(projectDir);

				if (projectDesc != null && projectId.equals(projectDesc.getProjectId())) {
					IResource resource = workspace.createResource(this, projectDir);

					if (resource instanceof IProject) {
						project = (IProject) resource;

						this.projectMap.put(projectId, project);
					}
				}
			}
		}

		if (project == null) {
			getProjects();
			project = this.projectMap.get(projectId);
		}
		return project;
	}

	/**
	 * 
	 * @param projectDir
	 * @return
	 */
	protected ProjectDescription getProjectDescription(File projectDir) {
		ProjectDescription projectDesc = null;
		if (projectDir != null && projectDir.isDirectory()) {
			File mataInfFolder = new File(projectDir, WorkspaceConstants.META_INF_FOLDER);
			File projectConfigFile = new File(mataInfFolder, WorkspaceConstants.PROJECT_JSON);
			if (projectConfigFile.exists()) {
				WorkingCopy<?> workingCopy = WorkingCopyUtil.getWorkingCopy(projectConfigFile);
				if (workingCopy != null) {
					projectDesc = workingCopy.getRootElement(ProjectDescription.class);
				}
			}
		}
		return projectDesc;
	}

}
