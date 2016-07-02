package org.nb.mgm.handler;

import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_EXIST;
import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_ILLEGAL_PARAMETER;
import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_ILLEGAL_STATES;
import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_NOT_FOUND;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.nb.mgm.exception.MgmException;
import org.nb.mgm.model.runtime.ClusterRoot;
import org.nb.mgm.model.runtime.Project;
import org.nb.mgm.service.ManagementService;
import org.origin.common.util.Util;

public class ProjectHandler {

	protected ManagementService mgmService;

	/**
	 * 
	 * @param mgmService
	 */
	public ProjectHandler(ManagementService mgmService) {
		this.mgmService = mgmService;
	}

	protected ClusterRoot getRoot() {
		return this.mgmService.getRoot();
	}

	/**
	 * Get Projects.
	 * 
	 * @return
	 */
	public List<Project> getProjects() throws MgmException {
		List<Project> resultProjects = new ArrayList<Project>();

		ClusterRoot root = getRoot();
		resultProjects.addAll(root.getProjects());

		return resultProjects;
	}

	/**
	 * Get Project by Id.
	 * 
	 * @param projectId
	 * @return
	 * @throws MgmException
	 */
	public Project getProject(String projectId) throws MgmException {
		// Throw exception - empty Project id
		if (projectId == null || projectId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Project Id cannot be empty.", null);
		}

		Project resultProject = null;
		ClusterRoot root = getRoot();
		for (Iterator<Project> projectItor = root.getProjects().iterator(); projectItor.hasNext();) {
			Project currProject = projectItor.next();
			if (projectId.equals(currProject.getId())) {
				resultProject = currProject;
				break;
			}
		}

		return resultProject;
	}

	/**
	 * Add a Project to the cluster.
	 * 
	 * @param project
	 * @throws MgmException
	 */
	public void addProject(Project project) throws MgmException {
		// Throw exception - empty Project
		if (project == null) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Project cannot be empty.", null);
		}

		// Throw exception - empty Project id
		if (project.getId() == null || project.getId().isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Project id cannot be empty.", null);
		}

		// Throw exception - empty Project name
		if (project.getName() == null || project.getName().isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Project name cannot be empty.", null);
		}

		ClusterRoot root = getRoot();
		for (Iterator<Project> projectItor = root.getProjects().iterator(); projectItor.hasNext();) {
			Project currProject = projectItor.next();
			if (project.getId().equals(currProject.getId())) {
				throw new MgmException(ERROR_CODE_ENTITY_EXIST, "Project with same Id already exists.", null);
			}
		}

		root.addProject(project);
	}

	/**
	 * Update Project information.
	 * 
	 * @param project
	 * @throws MgmException
	 */
	public void updateProject(Project project) throws MgmException {
		// Throw exception - empty Project
		if (project == null) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Project cannot be empty.", null);
		}

		// Find Project by Id
		Project projectToUpdate = getProject(project.getId());

		// Throw exception - Project not found
		if (projectToUpdate == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "Project cannot be found.", null);
		}

		// No need to update when they are the same object.
		if (projectToUpdate == project) {
			return;
		}

		// Project name is changed - Update Project name
		if (Util.compare(projectToUpdate.getName(), project.getName()) != 0) {
			projectToUpdate.setName(project.getName());
		}
		// Project description is changed - Project Machine description
		if (Util.compare(projectToUpdate.getDescription(), project.getDescription()) != 0) {
			projectToUpdate.setDescription(project.getDescription());
		}
	}

	/**
	 * Delete a Project from the cluster.
	 * 
	 * @param projectId
	 * @throws MgmException
	 */
	public boolean deleteProject(String projectId) throws MgmException {
		// Throw exception - empty Id
		if (projectId == null || projectId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Project Id cannot be empty.", null);
		}

		// Find Project by Id
		Project projectToDelete = getProject(projectId);

		// Throw exception - Project not found
		if (projectToDelete == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "Project cannot be found.", null);
		}

		// Throw exception - Project contains HomeConfigs
		if (!projectToDelete.getHomes().isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_STATES, "Project cannot be deleted. Please delete Home configurations in the Project first.", null);
		}

		// Delete Project from cluster root.
		ClusterRoot root = getRoot();
		return root.deleteProject(projectToDelete);
	}

}
