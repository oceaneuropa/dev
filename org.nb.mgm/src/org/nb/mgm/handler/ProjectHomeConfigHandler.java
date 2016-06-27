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
import org.nb.mgm.service.ManagementService;
import org.origin.common.util.Util;

public class ProjectHomeConfigHandler {

	protected ManagementService mgmService;

	/**
	 * 
	 * @param mgmService
	 */
	public ProjectHomeConfigHandler(ManagementService mgmService) {
		this.mgmService = mgmService;
	}

	protected ClusterRoot getRoot() {
		return this.mgmService.getRoot();
	}

	/**
	 * Get ProjectHomeConfigs in a Project.
	 * 
	 * @param projectId
	 * @return
	 * @throws MgmException
	 */
	public List<ProjectHomeConfig> getHomeConfigs(String projectId) throws MgmException {
		// Container Project must exist
		Project project = this.mgmService.getProject(projectId);
		if (project == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "Container Project cannot be found.", null);
		}

		List<ProjectHomeConfig> homeConfigs = project.getHomeConfigs();

		List<ProjectHomeConfig> resultHomeConfigs = new ArrayList<ProjectHomeConfig>();
		resultHomeConfigs.addAll(homeConfigs);

		return resultHomeConfigs;
	}

	/**
	 * Get ProjectHomeConfig information by Id.
	 * 
	 * @param homeConfigId
	 * @return
	 */
	public ProjectHomeConfig getHomeConfig(String homeConfigId) throws MgmException {
		ProjectHomeConfig resultHomeConfig = null;

		// Throw exception - empty Id
		if (homeConfigId == null || homeConfigId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "ProjectHomeConfig Id cannot be empty.", null);
		}

		// Iterate though all Projects and then all ProjectHomeConfigs in each Project. Find the ProjectHomeConfig with matching Id.
		for (Iterator<Project> projectItor = this.mgmService.getProjects().iterator(); projectItor.hasNext();) {
			Project currProject = projectItor.next();

			for (Iterator<ProjectHomeConfig> homeConfigItor = currProject.getHomeConfigs().iterator(); homeConfigItor.hasNext();) {
				ProjectHomeConfig currHomeConfig = homeConfigItor.next();
				if (homeConfigId.equals(currHomeConfig.getId())) {
					resultHomeConfig = currHomeConfig;
					break;
				}
			}

			if (resultHomeConfig != null) {
				break;
			}
		}
		return resultHomeConfig;
	}

	/**
	 * Add a ProjectHomeConfig to a Project.
	 * 
	 * @param projectId
	 * @param homeConfig
	 */
	public void addHomeConfig(String projectId, ProjectHomeConfig homeConfig) throws MgmException {
		// Container Project must exist
		Project project = this.mgmService.getProject(projectId);
		if (project == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "Conatiner Project cannot be found.", null);
		}

		// Generate unique ProjectHomeConfig Id
		if (homeConfig.getId() == null || homeConfig.getId().isEmpty()) {
			homeConfig.setId(UUID.randomUUID().toString());
		}

		// Throw exception - empty ProjectHomeConfig name
		// if (homeConfig.getName() == null || homeConfig.getName().isEmpty()) {
		// throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "ProjectHomeConfig name cannot be empty.", null);
		// }
		if (homeConfig.getName() == null || homeConfig.getName().isEmpty()) {
			homeConfig.setName("HomeConfig");
		}

		// Throw exception - ProjectHomeConfig with same Id or name exists
		for (Iterator<ProjectHomeConfig> homeConfigItor = project.getHomeConfigs().iterator(); homeConfigItor.hasNext();) {
			ProjectHomeConfig currHomeConfig = homeConfigItor.next();

			if (homeConfig.getId().equals(currHomeConfig.getId())) {
				throw new MgmException(ERROR_CODE_ENTITY_EXIST, "ProjectHomeConfig with same Id already exists.", null);
			}
			// if (homeConfig.getName().equals(currHomeConfig.getName())) {
			// throw new MgmException(ERROR_CODE_ENTITY_EXIST, "ProjectHomeConfig with same name already exists.", null);
			// }
		}

		// Generate unique ProjectHomeConfig name
		String name = homeConfig.getName();
		String uniqueName = name;
		int index = 1;
		while (true) {
			boolean nameExist = false;
			for (Iterator<ProjectHomeConfig> homeConfigItor = project.getHomeConfigs().iterator(); homeConfigItor.hasNext();) {
				ProjectHomeConfig currHomeConfig = homeConfigItor.next();
				if (uniqueName.equals(currHomeConfig.getName())) {
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
			homeConfig.setName(uniqueName);
		}

		project.addHomeConfig(homeConfig);
	}

	/**
	 * Update ProjectHomeConfig information.
	 * 
	 * @param homeConfig
	 */
	public void updateHomeConfig(ProjectHomeConfig homeConfig) throws MgmException {
		// Throw exception - empty ProjectHomeConfig
		if (homeConfig == null) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "ProjectHomeConfig cannot be empty.", null);
		}

		// Find ProjectHomeConfig by Id
		ProjectHomeConfig homeConfigToUpdate = getHomeConfig(homeConfig.getId());

		// No need to update when they are the same object.
		if (homeConfigToUpdate == homeConfig) {
			return;
		}

		// ProjectHomeConfig name is changed - Update ProjectHomeConfig name
		if (Util.compare(homeConfigToUpdate.getName(), homeConfig.getName()) != 0) {
			homeConfigToUpdate.setName(homeConfig.getName());
		}
		// ProjectHomeConfig description is changed - Update ProjectHomeConfig description
		if (Util.compare(homeConfigToUpdate.getDescription(), homeConfig.getDescription()) != 0) {
			homeConfigToUpdate.setDescription(homeConfig.getDescription());
		}
	}

	/**
	 * Delete ProjectHomeConfig by id.
	 * 
	 * @param homeConfigId
	 */
	public boolean deleteHomeConfig(String homeConfigId) throws MgmException {
		// Throw exception - empty Id
		if (homeConfigId == null || homeConfigId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "ProjectHomeConfig Id cannot be empty.", null);
		}

		// Find ProjectHomeConfig by Id
		ProjectHomeConfig homeConfigToDelete = getHomeConfig(homeConfigId);

		// Throw exception - ProjectHomeConfig not found
		if (homeConfigToDelete == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "ProjectHomeConfig cannot be found.", null);
		}

		// Delete ProjectHomeConfig from Project.
		Project project = homeConfigToDelete.getProject();
		if (project != null) {
			return project.removeHomeConfig(homeConfigToDelete);
		}
		return false;
	}

}
