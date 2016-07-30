package org.nb.mgm.model.runtime;

import java.util.ArrayList;
import java.util.List;

import org.origin.common.rest.model.ModelObject;

public class Project extends ModelObject {

	protected List<ProjectHome> projectHomes = new ArrayList<ProjectHome>();
	protected List<Software> projectSoftwareList = new ArrayList<Software>();

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

	// ----------------------------------------------------------------------------------------------------------------
	// ProjectSoftware
	// ----------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @return
	 */
	public List<Software> getSoftware() {
		return this.projectSoftwareList;
	}

	/**
	 * 
	 * @param softwareId
	 * @return
	 */
	public Software getSoftware(String softwareId) {
		Software software = null;
		if (softwareId != null) {
			for (Software currSoftware : this.projectSoftwareList) {
				String currSoftwareId = currSoftware.getId();
				if (softwareId.equals(currSoftwareId)) {
					software = currSoftware;
					break;
				}
			}
		}
		return software;
	}

	/**
	 * 
	 * @param software
	 * @return
	 */
	public boolean addSoftware(Software software) {
		if (software != null && !this.projectSoftwareList.contains(software)) {
			software.setParent(this);
			this.projectSoftwareList.add(software);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param software
	 * @return
	 */
	public boolean deleteSoftware(Software software) {
		if (software != null && this.projectSoftwareList.contains(software)) {
			this.projectSoftwareList.remove(software);
			software.setParent(null);
			return true;
		}
		return false;
	}

}
