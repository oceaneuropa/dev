package org.orbit.platform.model.platform.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceRequestDTO {

	@XmlElement
	protected String action;

	@XmlElement
	protected String extensionTypeId;

	@XmlElement
	protected String extensionId;

	@XmlElement
	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();

	public ServiceRequestDTO() {
	}

	/**
	 * 
	 * @param action
	 * @param extensionTypeId
	 * @param extensionId
	 * @param properties
	 */
	public ServiceRequestDTO(String action, String extensionTypeId, String extensionId, Map<String, Object> properties) {
		this.action = action;
		this.extensionTypeId = extensionTypeId;
		this.extensionId = extensionId;
		this.properties = properties;
	}

	@XmlElement
	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@XmlElement
	public String getExtensionTypeId() {
		return this.extensionTypeId;
	}

	public void setExtensionTypeId(String extensionTypeId) {
		this.extensionTypeId = extensionTypeId;
	}

	@XmlElement
	public String getExtensionId() {
		return this.extensionId;
	}

	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
	}

	@XmlElement
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}

// @XmlElement
// public Object getProperty(String propName) {
// return this.properties.get(propName);
// }
//
// public void setProperty(String propName, Object propValue) {
// if (propValue == null) {
// this.properties.remove(propName);
// } else {
// this.properties.put(propName, propValue);
// }
// }