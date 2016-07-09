package org.nb.mgm.handler;

import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_EXIST;
import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_ILLEGAL_PARAMETER;
import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_NOT_FOUND;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.nb.mgm.exception.MgmException;
import org.nb.mgm.model.runtime.ProjectHome;
import org.nb.mgm.model.runtime.ProjectNode;
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
	 * @throws MgmException
	 */
	public List<ProjectNode> getProjectNodes(String projectId, String projectHomeId) throws MgmException {
		// Throw exception - empty Id
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);

		// Container ProjectHome must exist
		ProjectHome projectHome = this.mgmService.getProjectHome(projectId, projectHomeId);
		if (projectHome == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "Container ProjectHome cannot be found.", null);
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
	 * @throws MgmException
	 */
	public ProjectNode getProjectNode(String projectId, String projectHomeId, String projectNodeId) throws MgmException {
		// Throw exception - empty Id
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);
		checkProjectNodeId(projectNodeId);

		// Container ProjectHome must exist
		ProjectHome projectHome = this.mgmService.getProjectHome(projectId, projectHomeId);
		if (projectHome == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "Container ProjectHome cannot be found.", null);
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
	 * @throws MgmException
	 */
	public ProjectNode addProjectNode(String projectId, String projectHomeId, ProjectNode newProjectNodeRequest) throws MgmException {
		// Throw exception - empty Id
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);
		checkProjectNode(newProjectNodeRequest);

		// Container ProjectHome must exist
		ProjectHome projectHome = this.mgmService.getProjectHome(projectId, projectHomeId);
		if (projectHome == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "Container ProjectHome cannot be found.", null);
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
				throw new MgmException(ERROR_CODE_ENTITY_EXIST, "ProjectNode with same Id already exists in the ProjectHome.", null);
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
	 * @throws MgmException
	 */
	public void updateProjectNode(String projectId, String projectHomeId, ProjectNode updateProjectNodeRequest) throws MgmException {
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
	 * @throws MgmException
	 */
	public boolean deleteProjectNode(String projectId, String projectHomeId, String projectNodeId) throws MgmException {
		// Throw exception - empty Id
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);
		checkProjectNodeId(projectNodeId);

		// Find ProjectNode
		ProjectNode projectNodeToDelete = getProjectNode(projectId, projectHomeId, projectNodeId);

		// Throw exception - ProjectNode not found
		if (projectNodeToDelete == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "ProjectNode cannot be found.", null);
		}

		// Delete ProjectHome from Project.
		ProjectHome projectHome = projectNodeToDelete.getProjectHome();
		if (projectHome != null) {
			return projectHome.deleteNode(projectNodeToDelete);
		}
		return false;
	}

	/**
	 * 
	 * @param projectId
	 * @throws MgmException
	 */
	protected void checkProjectId(String projectId) throws MgmException {
		if (projectId == null || projectId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "projectId cannot be empty.", null);
		}
	}

	/**
	 * 
	 * @param projectHomeId
	 * @throws MgmException
	 */
	protected void checkProjectHomeId(String projectHomeId) throws MgmException {
		if (projectHomeId == null || projectHomeId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "projectHomeId cannot be empty.", null);
		}
	}

	/**
	 * 
	 * @param projectNodeId
	 * @throws MgmException
	 */
	protected void checkProjectNodeId(String projectNodeId) throws MgmException {
		if (projectNodeId == null || projectNodeId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "projectNodeId cannot be empty.", null);
		}
	}

	/**
	 * 
	 * @param projectNode
	 * @throws MgmException
	 */
	protected void checkProjectNode(ProjectNode projectNode) throws MgmException {
		if (projectNode == null) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "ProjectNode cannot be empty.", null);
		}
	}

}
