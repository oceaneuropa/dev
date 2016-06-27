package org.nb.mgm.model.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.origin.common.rest.model.ModelObject;

public class ProjectNodeConfig extends ModelObject {

	protected Map<String, Object> properties = new HashMap<String, Object>();

	public ProjectNodeConfig() {
	}

	/**
	 * 
	 * @param parent
	 */
	public ProjectNodeConfig(ProjectHomeConfig parent) {
		super(parent);
	}

	public Project getProject() {
		return this.getParent(Project.class);
	}

	public ProjectHomeConfig getProjectHomeConfig() {
		return this.getParent(ProjectHomeConfig.class);
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
