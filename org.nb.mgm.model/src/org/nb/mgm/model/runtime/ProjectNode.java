package org.nb.mgm.model.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nb.mgm.model.runtime.Software.SoftwareProxy;
import org.origin.common.rest.model.ModelObject;

public class ProjectNode extends ModelObject {

	protected Map<String, Object> properties = new HashMap<String, Object>();
	protected List<String> installedSoftwareIds = new ArrayList<String>();

	public ProjectNode() {
	}

	/**
	 * 
	 * @param installedSoftwareIds
	 */
	public ProjectNode(List<String> installedSoftwareIds) {
		this.installedSoftwareIds = installedSoftwareIds;
		if (this.installedSoftwareIds == null) {
			this.installedSoftwareIds = new ArrayList<String>();
		}
	}

	/**
	 * 
	 * @param parent
	 */
	public ProjectNode(ProjectHome parent) {
		super(parent);
	}

	/**
	 * Get parent Project
	 * 
	 * @return
	 */
	public Project getProject() {
		return this.getParent(Project.class);
	}

	/**
	 * Get parent ProjectHome
	 * 
	 * @return
	 */
	public ProjectHome getProjectHome() {
		return this.getParent(ProjectHome.class);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Properties
	// ----------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @return
	 */
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	/**
	 * 
	 * @param properties
	 */
	public void setProperties(Map<String, Object> properties) {
		this.properties.putAll(properties);
	}

	/**
	 * 
	 * @param propNames
	 */
	public void removeProperties(List<String> propNames) {
		for (String propName : propNames) {
			this.properties.remove(propName);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Software
	// ----------------------------------------------------------------------------------------------------------------
	/**
	 * Get a list of Software installed to the ProjectNode.
	 * 
	 * @return
	 */
	public List<Software> getInstalledSoftware() {
		List<Software> softwareList = new ArrayList<Software>();
		Project project = getProject();
		for (String currSoftwareId : installedSoftwareIds) {
			Software resolvedSoftware = resolve(project, currSoftwareId);
			softwareList.add(resolvedSoftware);
		}
		return softwareList;
	}

	/**
	 * Resolve Software from Project.
	 * 
	 * @param softwareId
	 * @return
	 */
	protected Software resolve(Project project, String softwareId) {
		Software software = null;
		if (project != null) {
			software = project.getSoftware(softwareId);
		}
		if (software == null) {
			software = new SoftwareProxy(softwareId);
		}
		return software;
	}

	/**
	 * Install Software to ProjectNode.
	 * 
	 * @param softwareId
	 * @return
	 */
	public boolean installSoftware(String softwareId) {
		if (addSoftwareId(softwareId)) {
			// TODO
			// send out notification event
			return true;
		}
		return false;
	}

	/**
	 * Uninstall Software from ProjectNode.
	 * 
	 * @param softwareId
	 * @return
	 */
	public boolean uninstallSoftware(String softwareId) {
		if (removeSoftwareId(softwareId)) {
			// TODO
			// send out notification event
			return true;
		}
		return false;
	}

	public boolean addSoftwareId(String softwareId) {
		if (softwareId != null && !installedSoftwareIds.contains(softwareId)) {
			installedSoftwareIds.add(softwareId);
			return true;
		}
		return false;
	}

	public boolean removeSoftwareId(String softwareId) {
		if (softwareId != null && installedSoftwareIds.contains(softwareId)) {
			installedSoftwareIds.remove(softwareId);
			return true;
		}
		return false;
	}

}
