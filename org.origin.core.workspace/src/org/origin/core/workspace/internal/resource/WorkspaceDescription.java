package org.origin.core.workspace.internal.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import org.origin.common.resource.AbstractResourceObject;

public class WorkspaceDescription extends AbstractResourceObject {

	protected String name;
	protected String managementId;
	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManagementId() {
		return managementId;
	}

	public void setManagementId(String managementId) {
		this.managementId = managementId;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public void setProperty(String key, Object value) {
		this.properties.put(key, value);
	}

	public void removeProperty(String key) {
		this.properties.remove(key);
	}

}
