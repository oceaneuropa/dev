package org.origin.core.workspace.internal.resource;

import java.util.ArrayList;
import java.util.List;

public class ProjectDescription {

	protected String projectId;
	protected List<ProjectHomeConfig> projectHomeConfigs = new ArrayList<ProjectHomeConfig>();

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * 
	 * @return
	 */
	public List<ProjectHomeConfig> getProjectHomeConfigs() {
		return this.projectHomeConfigs;
	}

	/**
	 * 
	 * @param projectHomeConfig
	 * @return
	 */
	public boolean addProjectHomeConfig(ProjectHomeConfig projectHomeConfig) {
		if (projectHomeConfig != null && !this.projectHomeConfigs.contains(projectHomeConfig)) {
			return this.projectHomeConfigs.add(projectHomeConfig);
		}
		return false;
	}

	/**
	 * 
	 * @param projectHomeConfig
	 * @return
	 */
	public boolean removeProjectHomeConfig(ProjectHomeConfig projectHomeConfig) {
		if (projectHomeConfig != null && this.projectHomeConfigs.contains(projectHomeConfig)) {
			return this.projectHomeConfigs.remove(projectHomeConfig);
		}
		return false;
	}

}
