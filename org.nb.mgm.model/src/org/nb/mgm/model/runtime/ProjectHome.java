package org.nb.mgm.model.runtime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.origin.common.rest.model.ModelObject;

public class ProjectHome extends ModelObject {

	protected Home deploymentHome;
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

	public void setDeploymentHome(Home home) {
		this.deploymentHome = home;
	}

	public Home getDeploymentHome() {
		if (this.deploymentHome != null && this.deploymentHome.isProxy()) {
			String homeId = this.deploymentHome.getId();
			ClusterRoot root = getParent(ClusterRoot.class);
			if (homeId != null && root != null) {
				for (Iterator<Machine> machineItor = root.getMachines().iterator(); machineItor.hasNext();) {
					Machine currMachine = machineItor.next();
					for (Iterator<Home> homeItor = currMachine.getHomes().iterator(); homeItor.hasNext();) {
						Home currHome = homeItor.next();
						if (homeId.equals(currHome.getId())) {
							this.deploymentHome = currHome;
							return this.deploymentHome;
						}
					}
				}
			}
		}
		return this.deploymentHome;
	}

	@Override
	public boolean hasAttribute(String attributeName) {
		boolean result = super.hasAttribute(attributeName);
		if (result) {
			return true;
		}
		if ("homeId".equals(attributeName)) {
			Home home = getDeploymentHome();
			if (home != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object getAttribute(String attributeName) {
		Object value = super.getAttribute(attributeName);
		if (value == null) {
			if ("homeId".equals(attributeName)) {
				Home home = getDeploymentHome();
				if (home != null) {
					value = home.getId();
				}
			}
		}
		return value;
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
