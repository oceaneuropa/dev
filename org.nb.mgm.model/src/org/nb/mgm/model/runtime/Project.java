package org.nb.mgm.model.runtime;

import java.util.ArrayList;
import java.util.List;

import org.origin.common.rest.model.ModelObject;

public class Project extends ModelObject {

	protected List<ProjectHome> projectHomes = new ArrayList<ProjectHome>();

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
	// ProjectHome
	// ----------------------------------------------------------------------------------------------------------------
	public List<ProjectHome> getHomes() {
		return this.projectHomes;
	}

	public boolean addHome(ProjectHome projectHome) {
		if (projectHome != null && !this.projectHomes.contains(projectHome)) {
			projectHome.setParent(this);
			this.projectHomes.add(projectHome);
			return true;
		}
		return false;
	}

	public boolean deleteHome(ProjectHome projectHome) {
		if (projectHome != null && this.projectHomes.contains(projectHome)) {
			this.projectHomes.remove(projectHome);
			projectHome.setParent(null);
			return true;
		}
		return false;
	}

}
