package org.nb.mgm.model.runtime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.origin.common.rest.model.ModelObject;

public class ProjectHomeConfig extends ModelObject {

	protected Home home;
	protected List<ProjectNodeConfig> nodeConfigs = new ArrayList<ProjectNodeConfig>();

	public ProjectHomeConfig() {
	}

	/**
	 * 
	 * @param parent
	 */
	public ProjectHomeConfig(Project parent) {
		super(parent);
	}

	public Project getProject() {
		return this.getParent(Project.class);
	}

	public void setHome(Home home) {
		this.home = home;
	}

	public Home getHome() {
		if (this.home != null && this.home.isProxy()) {
			String homeId = this.home.getId();
			ClusterRoot root = this.getParent(ClusterRoot.class);
			if (homeId != null && root != null) {
				for (Iterator<Machine> machineItor = root.getMachines().iterator(); machineItor.hasNext();) {
					Machine currMachine = machineItor.next();
					for (Iterator<Home> homeItor = currMachine.getHomes().iterator(); homeItor.hasNext();) {
						Home currHome = homeItor.next();
						if (homeId.equals(currHome.getId())) {
							this.home = currHome;
							return this.home;
						}
					}
				}
			}
		}
		return this.home;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// ProjectNodeConfig
	// ----------------------------------------------------------------------------------------------------------------
	public List<ProjectNodeConfig> getNodeConfigs() {
		return this.nodeConfigs;
	}

	public boolean addNodeConfig(ProjectNodeConfig nodeConfig) {
		if (nodeConfig != null && !this.nodeConfigs.contains(nodeConfig)) {
			nodeConfig.setParent(this);
			this.nodeConfigs.add(nodeConfig);
			return true;
		}
		return false;
	}

	public boolean removeNodeConfig(ProjectNodeConfig nodeConfig) {
		if (nodeConfig != null && this.nodeConfigs.contains(nodeConfig)) {
			this.nodeConfigs.remove(nodeConfig);
			nodeConfig.setParent(null);
			return true;
		}
		return false;
	}

}
