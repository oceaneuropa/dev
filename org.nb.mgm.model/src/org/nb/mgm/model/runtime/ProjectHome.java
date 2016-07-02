package org.nb.mgm.model.runtime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.origin.common.rest.model.ModelObject;

public class ProjectHome extends ModelObject {

	protected Home remoteHome;
	protected List<ProjectNode> projectNodes = new ArrayList<ProjectNode>();

	public ProjectHome() {
	}

	/**
	 * 
	 * @param parent
	 */
	public ProjectHome(Project parent) {
		super(parent);
	}

	public Project getProject() {
		return this.getParent(Project.class);
	}

	public void setRemoteHome(Home home) {
		this.remoteHome = home;
	}

	public Home getRemoteHome() {
		if (this.remoteHome != null && this.remoteHome.isProxy()) {
			String remoteHomeId = this.remoteHome.getId();
			ClusterRoot root = getParent(ClusterRoot.class);
			if (remoteHomeId != null && root != null) {
				for (Iterator<Machine> machineItor = root.getMachines().iterator(); machineItor.hasNext();) {
					Machine currMachine = machineItor.next();
					for (Iterator<Home> homeItor = currMachine.getHomes().iterator(); homeItor.hasNext();) {
						Home currHome = homeItor.next();
						if (remoteHomeId.equals(currHome.getId())) {
							this.remoteHome = currHome;
							return this.remoteHome;
						}
					}
				}
			}
		}
		return this.remoteHome;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// ProjectNode
	// ----------------------------------------------------------------------------------------------------------------
	public List<ProjectNode> getNodes() {
		return this.projectNodes;
	}

	public boolean addNode(ProjectNode projectNode) {
		if (projectNode != null && !this.projectNodes.contains(projectNode)) {
			projectNode.setParent(this);
			this.projectNodes.add(projectNode);
			return true;
		}
		return false;
	}

	public boolean deleteNode(ProjectNode projectNode) {
		if (projectNode != null && this.projectNodes.contains(projectNode)) {
			this.projectNodes.remove(projectNode);
			projectNode.setParent(null);
			return true;
		}
		return false;
	}

}
