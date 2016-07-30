package org.nb.mgm.handler;

import static org.nb.mgm.service.ManagementConstants.ERROR_CODE_ENTITY_EXIST;
import static org.nb.mgm.service.ManagementConstants.ERROR_CODE_ENTITY_ILLEGAL_PARAMETER;
import static org.nb.mgm.service.ManagementConstants.ERROR_CODE_ENTITY_NOT_FOUND;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.nb.mgm.exception.ManagementException;
import org.nb.mgm.model.runtime.Project;
import org.nb.mgm.model.runtime.ProjectHome;
import org.nb.mgm.model.runtime.ProjectNode;
import org.nb.mgm.model.runtime.Software;
import org.nb.mgm.service.ManagementService;
import org.origin.common.util.Util;

public class ProjectNodeHandler {

	protected ManagementService mgmService;

	/**
	 * 
	 * @param mgmService
	 */
	public ProjectNodeHandler(ManagementService mgmService) {
		this.mgmService = mgmService;
	}

	/**
	 * Get ProjectNodes from a ProjectHome.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @return
	 * @throws ManagementException
	 */
	public List<ProjectNode> getProjectNodes(String projectId, String projectHomeId) throws ManagementException {
		// Throw exception - empty Id
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);

		// Container ProjectHome must exist
		ProjectHome projectHome = this.mgmService.getProjectHome(projectId, projectHomeId);
		if (projectHome == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "Container ProjectHome cannot be found.", null);
		}

		List<ProjectNode> projectNodes = projectHome.getNodes();

		List<ProjectNode> resultProjectNodes = new ArrayList<ProjectNode>();
		resultProjectNodes.addAll(projectNodes);

		return resultProjectNodes;
	}

	/**
	 * Get ProjectNode.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @return
	 * @throws ManagementException
	 */
	public ProjectNode getProjectNode(String projectId, String projectHomeId, String projectNodeId) throws ManagementException {
		// Throw exception - empty Id
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);
		checkProjectNodeId(projectNodeId);

		// Container ProjectHome must exist
		ProjectHome projectHome = this.mgmService.getProjectHome(projectId, projectHomeId);
		if (projectHome == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "Container ProjectHome cannot be found.", null);
		}

		ProjectNode resultProjectNode = null;
		for (Iterator<ProjectNode> projectNodeItor = projectHome.getNodes().iterator(); projectNodeItor.hasNext();) {
			ProjectNode currProjectNode = projectNodeItor.next();

			if (projectNodeId.equals(currProjectNode.getId())) {
				resultProjectNode = currProjectNode;
				break;
			}
		}
		return resultProjectNode;
	}

	/**
	 * Add ProjectNode to a ProjectHome.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param newProjectNodeRequest
	 * @throws ManagementException
	 */
	public ProjectNode addProjectNode(String projectId, String projectHomeId, ProjectNode newProjectNodeRequest) throws ManagementException {
		// Throw exception - empty Id
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);
		checkProjectNode(newProjectNodeRequest);

		// Container ProjectHome must exist
		ProjectHome projectHome = this.mgmService.getProjectHome(projectId, projectHomeId);
		if (projectHome == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "Container ProjectHome cannot be found.", null);
		}

		// Generate unique ProjectNode Id
		if (newProjectNodeRequest.getId() == null || newProjectNodeRequest.getId().isEmpty()) {
			newProjectNodeRequest.setId(UUID.randomUUID().toString());
		}

		// Throw exception - empty ProjectHome name
		// if (projectNode.getName() == null || projectNode.getName().isEmpty()) {
		// throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "ProjectNode name cannot be empty.", null);
		// }
		if (newProjectNodeRequest.getName() == null || newProjectNodeRequest.getName().isEmpty()) {
			newProjectNodeRequest.setName("Node");
		}

		// Throw exception - ProjectNode with same Id or name exists within the same ProjectHome
		for (Iterator<ProjectNode> projectNodeItor = projectHome.getNodes().iterator(); projectNodeItor.hasNext();) {
			ProjectNode currProjectNode = projectNodeItor.next();

			if (newProjectNodeRequest.getId().equals(currProjectNode.getId())) {
				throw new ManagementException(ERROR_CODE_ENTITY_EXIST, "ProjectNode with same Id already exists in the ProjectHome.", null);
			}
			// if (projectNode.getName().equals(currProjectNode.getName())) {
			// throw new MgmException(ERROR_CODE_ENTITY_EXIST, "ProjectNode with same name already exists.", null);
			// }
		}

		// Generate unique ProjectNode name
		String name = newProjectNodeRequest.getName();
		String uniqueName = name;
		int index = 1;
		while (true) {
			boolean nameExist = false;
			for (Iterator<ProjectNode> projectNodeItor = projectHome.getNodes().iterator(); projectNodeItor.hasNext();) {
				ProjectNode currProjectNode = projectNodeItor.next();
				if (uniqueName.equals(currProjectNode.getName())) {
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
			newProjectNodeRequest.setName(uniqueName);
		}

		projectHome.addNode(newProjectNodeRequest);

		return newProjectNodeRequest;
	}

	/**
	 * Update ProjectNode.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param updateProjectNodeRequest
	 * @throws ManagementException
	 */
	public void updateProjectNode(String projectId, String projectHomeId, ProjectNode updateProjectNodeRequest) throws ManagementException {
		// Throw exception - empty ProjectNode
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);
		checkProjectNode(updateProjectNodeRequest);

		// Find ProjectNode
		ProjectNode projectNodeToUpdate = getProjectNode(projectId, projectHomeId, updateProjectNodeRequest.getId());

		// No need to update when they are the same object.
		if (projectNodeToUpdate == updateProjectNodeRequest) {
			return;
		}

		// Update name
		if (Util.compare(projectNodeToUpdate.getName(), updateProjectNodeRequest.getName()) != 0) {
			projectNodeToUpdate.setName(updateProjectNodeRequest.getName());
		}
		// Update description
		if (Util.compare(projectNodeToUpdate.getDescription(), updateProjectNodeRequest.getDescription()) != 0) {
			projectNodeToUpdate.setDescription(updateProjectNodeRequest.getDescription());
		}
	}

	/**
	 * Delete ProjectNode from a ProjectHome.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @return
	 * @throws ManagementException
	 */
	public boolean deleteProjectNode(String projectId, String projectHomeId, String projectNodeId) throws ManagementException {
		// Throw exception - empty Id
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);
		checkProjectNodeId(projectNodeId);

		// Find ProjectNode
		ProjectNode projectNodeToDelete = getProjectNode(projectId, projectHomeId, projectNodeId);
		if (projectNodeToDelete == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "ProjectNode cannot be found.", null);
		}

		// Delete ProjectHome from Project.
		ProjectHome projectHome = projectNodeToDelete.getProjectHome();
		if (projectHome != null) {
			return projectHome.deleteNode(projectNodeToDelete);
		}
		return false;
	}

	/**
	 * Get a list of Software installed on a ProjectNode.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @return
	 * @throws ManagementException
	 */
	public List<Software> getInstalledSoftware(String projectId, String projectHomeId, String projectNodeId) throws ManagementException {
		// Find Project
		Project project = this.mgmService.getProject(projectId);
		if (project == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "Project cannot be found.", null);
		}
		// Find ProjectNode
		ProjectNode projectNode = getProjectNode(projectId, projectHomeId, projectNodeId);
		if (projectNode == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "ProjectNode cannot be found.", null);
		}
		return projectNode.getInstalledSoftware();
	}

	/**
	 * Install Software to ProjectNode.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @param softwareId
	 * @return
	 * @throws ManagementException
	 */
	public boolean installSoftware(String projectId, String projectHomeId, String projectNodeId, String softwareId) throws ManagementException {
		// Find Project
		Project project = this.mgmService.getProject(projectId);
		if (project == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "Project cannot be found.", null);
		}
		// Find Software
		Software software = project.getSoftware(softwareId);
		if (software == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "Software cannot be found.", null);
		}
		// Find ProjectNode
		ProjectNode projectNode = getProjectNode(projectId, projectHomeId, projectNodeId);
		if (projectNode == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "ProjectNode cannot be found.", null);
		}
		return projectNode.installSoftware(softwareId);
	}

	/**
	 * Uninstall Software from ProjectNode.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @param softwareId
	 * @return
	 * @throws ManagementException
	 */
	public boolean uninstallSoftware(String projectId, String projectHomeId, String projectNodeId, String softwareId) throws ManagementException {
		// Find Project
		Project project = this.mgmService.getProject(projectId);
		if (project == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "Project cannot be found.", null);
		}
		// Find Software
		Software software = project.getSoftware(softwareId);
		if (software == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "Software cannot be found.", null);
		}
		// Find ProjectNode
		ProjectNode projectNode = getProjectNode(projectId, projectHomeId, projectNodeId);
		if (projectNode == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "ProjectNode cannot be found.", null);
		}
		return projectNode.uninstallSoftware(softwareId);
	}

	/**
	 * 
	 * @param projectId
	 * @throws ManagementException
	 */
	protected void checkProjectId(String projectId) throws ManagementException {
		if (projectId == null || projectId.isEmpty()) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "projectId cannot be empty.", null);
		}
	}

	/**
	 * 
	 * @param projectHomeId
	 * @throws ManagementException
	 */
	protected void checkProjectHomeId(String projectHomeId) throws ManagementException {
		if (projectHomeId == null || projectHomeId.isEmpty()) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "projectHomeId cannot be empty.", null);
		}
	}

	/**
	 * 
	 * @param projectNodeId
	 * @throws ManagementException
	 */
	protected void checkProjectNodeId(String projectNodeId) throws ManagementException {
		if (projectNodeId == null || projectNodeId.isEmpty()) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "projectNodeId cannot be empty.", null);
		}
	}

	/**
	 * 
	 * @param projectNode
	 * @throws ManagementException
	 */
	protected void checkProjectNode(ProjectNode projectNode) throws ManagementException {
		if (projectNode == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "ProjectNode cannot be empty.", null);
		}
	}

}
