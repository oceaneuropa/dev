package org.nb.home.model.runtime.config;

import java.util.ArrayList;
import java.util.List;

public class ProjectHomeConfig {

	protected String projectHomeId;
	protected List<ProjectNodeConfig> projectNodeConfigs = new ArrayList<ProjectNodeConfig>();

	public String getProjectHomeId() {
		return projectHomeId;
	}

	public void setProjectHomeId(String projectHomeId) {
		this.projectHomeId = projectHomeId;
	}

	public List<ProjectNodeConfig> getProjectNodeConfigs() {
		return this.projectNodeConfigs;
	}

	/**
	 * 
	 * @param projectNodeConfig
	 * @return
	 */
	public boolean addProjectNodeConfig(ProjectNodeConfig projectNodeConfig) {
		if (projectNodeConfig != null && !this.projectNodeConfigs.contains(projectNodeConfig)) {
			return this.projectNodeConfigs.add(projectNodeConfig);
		}
		return false;
	}

	/**
	 * 
	 * @param projectNodeConfig
	 * @return
	 */
	public boolean removeProjectNodeConfig(ProjectNodeConfig projectNodeConfig) {
		if (projectNodeConfig != null && this.projectNodeConfigs.contains(projectNodeConfig)) {
			return this.projectNodeConfigs.remove(projectNodeConfig);
		}
		return false;
	}

}
