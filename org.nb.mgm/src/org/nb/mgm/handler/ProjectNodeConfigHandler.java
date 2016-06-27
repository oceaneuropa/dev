package org.nb.mgm.handler;

import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_EXIST;
import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_ILLEGAL_PARAMETER;
import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_NOT_FOUND;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.nb.mgm.exception.MgmException;
import org.nb.mgm.model.runtime.ClusterRoot;
import org.nb.mgm.model.runtime.Project;
import org.nb.mgm.model.runtime.ProjectHomeConfig;
import org.nb.mgm.model.runtime.ProjectNodeConfig;
import org.nb.mgm.service.ManagementService;
import org.origin.common.util.Util;

public class ProjectNodeConfigHandler {

	protected ManagementService mgmService;

	/**
	 * 
	 * @param mgmService
	 */
	public ProjectNodeConfigHandler(ManagementService mgmService) {
		this.mgmService = mgmService;
	}

	protected ClusterRoot getRoot() {
		return this.mgmService.getRoot();
	}

	/**
	 * Get ProjectNodeConfigs in a ProjectHomeConfig.
	 * 
	 * @param homeConfigId
	 * @return
	 * @throws MgmException
	 */
	public List<ProjectNodeConfig> getNodeConfigs(String homeConfigId) throws MgmException {
		// Container ProjectHomeConfig must exist
		ProjectHomeConfig homeConfig = this.mgmService.getProjectHomeConfig(homeConfigId);
		if (homeConfig == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "Container ProjectHomeConfig cannot be found.", null);
		}

		List<ProjectNodeConfig> nodeConfigs = homeConfig.getNodeConfigs();

		List<ProjectNodeConfig> resultNodeConfigs = new ArrayList<ProjectNodeConfig>();
		resultNodeConfigs.addAll(nodeConfigs);

		return resultNodeConfigs;
	}

	/**
	 * Get ProjectNodeConfig information by Id.
	 * 
	 * @param nodeConfigId
	 * @return
	 * @throws MgmException
	 */
	public ProjectNodeConfig getNodeConfig(String nodeConfigId) throws MgmException {
		ProjectNodeConfig resultNodeConfig = null;

		// Throw exception - empty Id
		if (nodeConfigId == null || nodeConfigId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "ProjectNodeConfig Id cannot be empty.", null);
		}

		// Iterate though all Projects and then all ProjectHomeConfigs in each Project. Find the ProjectHomeConfig with matching Id.
		for (Iterator<Project> projectItor = this.mgmService.getProjects().iterator(); projectItor.hasNext();) {
			Project currProject = projectItor.next();

			for (Iterator<ProjectHomeConfig> homeConfigItor = currProject.getHomeConfigs().iterator(); homeConfigItor.hasNext();) {
				ProjectHomeConfig currHomeConfig = homeConfigItor.next();

				for (Iterator<ProjectNodeConfig> nodeConfigItor = currHomeConfig.getNodeConfigs().iterator(); nodeConfigItor.hasNext();) {
					ProjectNodeConfig currNodeConfig = nodeConfigItor.next();

					if (nodeConfigId.equals(currNodeConfig.getId())) {
						resultNodeConfig = currNodeConfig;
						break;
					}
				}

				if (resultNodeConfig != null) {
					break;
				}
			}

			if (resultNodeConfig != null) {
				break;
			}
		}
		return resultNodeConfig;
	}

	/**
	 * Added ProjectNodeConfig to ProjectHomeConfig.
	 * 
	 * @param homeConfigId
	 * @param nodeConfig
	 * @throws MgmException
	 */
	public void addNodeConfig(String homeConfigId, ProjectNodeConfig nodeConfig) throws MgmException {
		// Container ProjectHomeConfig must exist
		ProjectHomeConfig homeConfig = this.mgmService.getProjectHomeConfig(homeConfigId);
		if (homeConfig == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "Container ProjectHomeConfig cannot be found.", null);
		}

		// Generate unique ProjectHomeConfig Id
		if (nodeConfig.getId() == null || nodeConfig.getId().isEmpty()) {
			nodeConfig.setId(UUID.randomUUID().toString());
		}

		// Throw exception - empty ProjectHomeConfig name
		// if (nodeConfig.getName() == null || nodeConfig.getName().isEmpty()) {
		// throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "ProjectNodeConfig name cannot be empty.", null);
		// }
		if (nodeConfig.getName() == null || nodeConfig.getName().isEmpty()) {
			nodeConfig.setName("Node");
		}

		// Throw exception - ProjectNodeConfig with same Id or name exists
		for (Iterator<ProjectNodeConfig> nodeConfigItor = homeConfig.getNodeConfigs().iterator(); nodeConfigItor.hasNext();) {
			ProjectNodeConfig currNodeConfig = nodeConfigItor.next();

			if (nodeConfig.getId().equals(currNodeConfig.getId())) {
				throw new MgmException(ERROR_CODE_ENTITY_EXIST, "ProjectNodeConfig with same Id already exists.", null);
			}
			// if (nodeConfig.getName().equals(currNodeConfig.getName())) {
			// throw new MgmException(ERROR_CODE_ENTITY_EXIST, "ProjectNodeConfig with same name already exists.", null);
			// }
		}

		// Generate unique ProjectNodeConfig name
		String name = nodeConfig.getName();
		String uniqueName = name;
		int index = 1;
		while (true) {
			boolean nameExist = false;
			for (Iterator<ProjectNodeConfig> nodeConfigItor = homeConfig.getNodeConfigs().iterator(); nodeConfigItor.hasNext();) {
				ProjectNodeConfig currNodeConfig = nodeConfigItor.next();
				if (uniqueName.equals(currNodeConfig.getName())) {
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
			nodeConfig.setName(uniqueName);
		}

		homeConfig.addNodeConfig(nodeConfig);
	}

	/**
	 * Update ProjectNodeConfig information.
	 * 
	 * @param nodeConfig
	 * @throws MgmException
	 */
	public void updateNodeConfig(ProjectNodeConfig nodeConfig) throws MgmException {
		// Throw exception - empty ProjectNodeConfig
		if (nodeConfig == null) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "ProjectNodeConfig cannot be empty.", null);
		}

		// Find ProjectNodeConfig by Id
		ProjectNodeConfig nodeConfigToUpdate = getNodeConfig(nodeConfig.getId());

		// No need to update when they are the same object.
		if (nodeConfigToUpdate == nodeConfig) {
			return;
		}

		// ProjectNodeConfig name is changed - Update ProjectNodeConfig name
		if (Util.compare(nodeConfigToUpdate.getName(), nodeConfig.getName()) != 0) {
			nodeConfigToUpdate.setName(nodeConfig.getName());
		}
		// ProjectNodeConfig description is changed - Update ProjectNodeConfig description
		if (Util.compare(nodeConfigToUpdate.getDescription(), nodeConfig.getDescription()) != 0) {
			nodeConfigToUpdate.setDescription(nodeConfig.getDescription());
		}
	}

	/**
	 * Delete ProjectNodeConfig by Id.
	 * 
	 * @param nodeConfigId
	 * @throws MgmException
	 */
	public boolean deleteNodeConfig(String nodeConfigId) throws MgmException {
		// Throw exception - empty Id
		if (nodeConfigId == null || nodeConfigId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "ProjectNodeConfig Id cannot be empty.", null);
		}

		// Find ProjectNodeConfig by Id
		ProjectNodeConfig nodeConfigToDelete = getNodeConfig(nodeConfigId);

		// Throw exception - ProjectNodeConfig not found
		if (nodeConfigToDelete == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "ProjectNodeConfig cannot be found.", null);
		}

		// Delete ProjectHomeConfig from Project.
		ProjectHomeConfig homeConfig = nodeConfigToDelete.getProjectHomeConfig();
		if (homeConfig != null) {
			return homeConfig.removeNodeConfig(nodeConfigToDelete);
		}
		return false;
	}

}
