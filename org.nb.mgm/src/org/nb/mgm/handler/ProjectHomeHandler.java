package org.nb.mgm.handler;

import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_EXIST;
import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_ILLEGAL_PARAMETER;
import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_NOT_FOUND;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.nb.mgm.exception.MgmException;
import org.nb.mgm.model.runtime.Project;
import org.nb.mgm.model.runtime.ProjectHome;
import org.nb.mgm.service.ManagementService;
import org.origin.common.util.Util;

public class ProjectHomeHandler {

	protected ManagementService mgmService;

	/**
	 * 
	 * @param mgmService
	 */
	public ProjectHomeHandler(ManagementService mgmService) {
		this.mgmService = mgmService;
	}

	/**
	 * Get ProjectHomes in a Project.
	 * 
	 * @param projectId
	 * @return
	 * @throws MgmException
	 */
	public List<ProjectHome> getProjectHomes(String projectId) throws MgmException {
		// Throw exception - empty Id
		checkProjectId(projectId);

		// Container Project must exist
		Project project = this.mgmService.getProject(projectId);
		checkContainerProject(project);

		List<ProjectHome> projectHomes = project.getHomes();

		List<ProjectHome> resultProjectHomes = new ArrayList<ProjectHome>();
		resultProjectHomes.addAll(projectHomes);

		return resultProjectHomes;
	}

	/**
	 * Get a ProjectHome.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @return
	 */
	public ProjectHome getProjectHome(String projectId, String projectHomeId) throws MgmException {
		// Throw exception - empty Id
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);

		// Container Project must exist
		Project project = this.mgmService.getProject(projectId);
		checkContainerProject(project);

		ProjectHome resultProjectHome = null;

		// Iterate though all ProjectHomes in a Project. Find the ProjectHome with matching Id.
		for (Iterator<ProjectHome> projectHomeItor = project.getHomes().iterator(); projectHomeItor.hasNext();) {
			ProjectHome currProjectHome = projectHomeItor.next();
			if (projectHomeId.equals(currProjectHome.getId())) {
				resultProjectHome = currProjectHome;
				break;
			}
		}

		return resultProjectHome;
	}

	/**
	 * Add a ProjectHome to a Project.
	 * 
	 * @param projectId
	 * @param newProjectHomeRequest
	 */
	public ProjectHome addProjectHome(String projectId, ProjectHome newProjectHomeRequest) throws MgmException {
		// Throw exception - empty Id
		checkProjectId(projectId);

		// Throw exception - empty ProjectHome
		checkProjectHome(newProjectHomeRequest);

		// Container Project must exist
		Project project = this.mgmService.getProject(projectId);
		checkContainerProject(project);

		// Generate unique ProjectHome Id
		if (newProjectHomeRequest.getId() == null || newProjectHomeRequest.getId().isEmpty()) {
			newProjectHomeRequest.setId(UUID.randomUUID().toString());
		}

		// Throw exception - empty ProjectHome name
		// if (projectHome.getName() == null || projectHome.getName().isEmpty()) {
		// throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "ProjectHome name cannot be empty.", null);
		// }
		if (newProjectHomeRequest.getName() == null || newProjectHomeRequest.getName().isEmpty()) {
			newProjectHomeRequest.setName("Home");
		}

		// Throw exception - ProjectHome with same Id or name exists
		for (Iterator<ProjectHome> projectHomeItor = project.getHomes().iterator(); projectHomeItor.hasNext();) {
			ProjectHome currProjectHome = projectHomeItor.next();

			if (newProjectHomeRequest.getId().equals(currProjectHome.getId())) {
				throw new MgmException(ERROR_CODE_ENTITY_EXIST, "ProjectHome with same Id already exists.", null);
			}
			// if (projectHome.getName().equals(currProjectHome.getName())) {
			// throw new MgmException(ERROR_CODE_ENTITY_EXIST, "ProjectHome with same name already exists.", null);
			// }
		}

		// Generate unique ProjectHome name
		String name = newProjectHomeRequest.getName();
		String uniqueName = name;
		int index = 1;
		while (true) {
			boolean nameExist = false;
			for (Iterator<ProjectHome> projectHomeItor = project.getHomes().iterator(); projectHomeItor.hasNext();) {
				ProjectHome currProjectHome = projectHomeItor.next();
				if (uniqueName.equals(currProjectHome.getName())) {
					nameExist = true;
					break;
				}
			}
			if (nameExist) {
				uniqueName = name + "(" + index + ")";
				index++;
			} else {
				break;
			}
		}
		if (!uniqueName.equals(name)) {
			newProjectHomeRequest.setName(uniqueName);
		}

		project.addHome(newProjectHomeRequest);

		return newProjectHomeRequest;
	}

	/**
	 * Update ProjectHome.
	 * 
	 * @param projectId
	 * @param updateProjectHomeRequest
	 */
	public void updateProjectHome(String projectId, ProjectHome updateProjectHomeRequest) throws MgmException {
		// Throw exception - empty Id
		checkProjectId(projectId);

		// Throw exception - empty ProjectHome
		checkProjectHome(updateProjectHomeRequest);

		// Find ProjectHome by Id
		ProjectHome projectHomeToUpdate = getProjectHome(projectId, updateProjectHomeRequest.getId());

		// Throw exception - ProjectHome not found
		checkProjectHomeNotFound(projectHomeToUpdate);

		// No need to update when they are the same object.
		if (projectHomeToUpdate == updateProjectHomeRequest) {
			return;
		}

		// Update name
		if (Util.compare(projectHomeToUpdate.getName(), updateProjectHomeRequest.getName()) != 0) {
			projectHomeToUpdate.setName(updateProjectHomeRequest.getName());
		}
		// Update description
		if (Util.compare(projectHomeToUpdate.getDescription(), updateProjectHomeRequest.getDescription()) != 0) {
			projectHomeToUpdate.setDescription(updateProjectHomeRequest.getDescription());
		}
	}

	/**
	 * Delete ProjectHome from Project.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 */
	public boolean deleteProjectHome(String projectId, String projectHomeId) throws MgmException {
		// Throw exception - empty Id
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);

		// Find Project
		Project project = this.mgmService.getProject(projectId);
		checkContainerProject(project);

		// Find ProjectHome
		ProjectHome projectHomeToDelete = getProjectHome(projectId, projectHomeId);
		checkProjectHomeNotFound(projectHomeToDelete);

		// Delete ProjectHome from Project.
		return project.deleteHome(projectHomeToDelete);
	}

	protected void checkProjectId(String projectId) throws MgmException {
		if (projectId == null || projectId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "projectId cannot be empty.", null);
		}
	}

	protected void checkProjectHomeId(String projectHomeId) throws MgmException {
		if (projectHomeId == null || projectHomeId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "projectHomeId cannot be empty.", null);
		}
	}

	protected void checkProjectHome(ProjectHome projectHome) throws MgmException {
		if (projectHome == null) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "ProjectHome cannot be empty.", null);
		}
	}

	protected void checkProjectHomeNotFound(ProjectHome projectHome) throws MgmException {
		if (projectHome == null) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "ProjectHome cannot be found.", null);
		}
	}

	protected void checkContainerProject(Project project) throws MgmException {
		if (project == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "Conatiner Project cannot be found.", null);
		}
	}

}
