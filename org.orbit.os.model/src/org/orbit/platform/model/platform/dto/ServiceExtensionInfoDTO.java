package org.orbit.platform.model.platform.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceExtensionInfoDTO {

	@XmlElement
	protected String extensionTypeId;

	@XmlElement
	protected String extensionId;

	@XmlElement
	protected String name;

	@XmlElement
	protected String description;

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
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
