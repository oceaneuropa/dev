package org.orbit.platform.model.platform.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceInstanceInfoDTO {

	@XmlElement
	protected String extensionTypeId;

	@XmlElement
	protected String extensionId;

	@XmlElement
	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();

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

	@XmlElement
	public Object getProperty(String propName) {
		return this.properties.get(propName);
	}

	public void setProperty(String propName, Object propValue) {
		if (propValue == null) {
			this.properties.remove(propName);
		} else {
			this.properties.put(propName, propValue);
		}
	}

}
