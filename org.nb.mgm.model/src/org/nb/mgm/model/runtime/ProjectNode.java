package org.nb.mgm.model.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.origin.common.rest.model.ModelObject;

public class ProjectNode extends ModelObject {

	protected Map<String, Object> properties = new HashMap<String, Object>();

	public ProjectNode() {
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
	// properties
	// ----------------------------------------------------------------------------------------------------------------
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties.putAll(properties);
	}

	public void removeProperties(List<String> propNames) {
		for (String propName : propNames) {
			this.properties.remove(propName);
		}
	}

}
