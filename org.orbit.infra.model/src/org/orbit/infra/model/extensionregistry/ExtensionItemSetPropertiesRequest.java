package org.orbit.infra.model.extensionregistry;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExtensionItemSetPropertiesRequest {

	@XmlElement
	protected String platformId;
	@XmlElement
	protected String typeId;
	@XmlElement
	protected String extensionId;
	@XmlElement
	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();

	public ExtensionItemSetPropertiesRequest() {
	}

	@XmlElement
	public String getPlatformId() {
		return this.platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	@XmlElement
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	@XmlElement
	public String getExtensionId() {
		return extensionId;
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
