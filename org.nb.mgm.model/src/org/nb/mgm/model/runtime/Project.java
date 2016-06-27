package org.nb.mgm.model.runtime;

import java.util.ArrayList;
import java.util.List;

import org.origin.common.rest.model.ModelObject;

public class Project extends ModelObject {

	protected List<ProjectHomeConfig> homeConfigs = new ArrayList<ProjectHomeConfig>();

	public Project() {
	}

	/**
	 * 
	 * @param parent
	 */
	public Project(Object parent) {
		super(parent);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// ProjectHomeConfig
	// ----------------------------------------------------------------------------------------------------------------
	public List<ProjectHomeConfig> getHomeConfigs() {
		return this.homeConfigs;
	}

	public boolean addHomeConfig(ProjectHomeConfig homeConfig) {
		if (homeConfig != null && !this.homeConfigs.contains(homeConfig)) {
			homeConfig.setParent(this);
			this.homeConfigs.add(homeConfig);
			return true;
		}
		return false;
	}

	public boolean removeHomeConfig(ProjectHomeConfig homeConfig) {
		if (homeConfig != null && this.homeConfigs.contains(homeConfig)) {
			this.homeConfigs.remove(homeConfig);
			homeConfig.setParent(null);
			return true;
		}
		return false;
	}

}
