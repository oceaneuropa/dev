package org.orbit.platform.model.platform.dto;

import java.util.HashMap;
import java.util.Map;

public class ServiceInstanceInfoImpl implements ServiceInstanceInfo {

	protected String extensionTypeId;
	protected String extensionId;
	protected Map<String, Object> properties;

	public ServiceInstanceInfoImpl() {
	}

	/**
	 * 
	 * @param extensionTypeId
	 * @param extensionId
	 * @param properties
	 */
	public ServiceInstanceInfoImpl(String extensionTypeId, String extensionId, Map<String, Object> properties) {
		this.extensionTypeId = extensionTypeId;
		this.extensionId = extensionId;
		this.properties = properties;
	}

	@Override
	public String getExtensionTypeId() {
		return this.extensionTypeId;
	}

	public void setExtensionTypeId(String extensionTypeId) {
		this.extensionTypeId = extensionTypeId;
	}

	@Override
	public String getExtensionId() {
		return this.extensionId;
	}

	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
	}

	@Override
	public Map<String, Object> getProperties() {
		if (this.properties == null) {
			this.properties = new HashMap<String, Object>();
		}
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}
