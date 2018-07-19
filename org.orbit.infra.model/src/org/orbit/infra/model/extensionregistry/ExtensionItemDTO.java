package org.orbit.infra.model.extensionregistry;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExtensionItemDTO {

	@XmlElement
	protected Integer id;
	@XmlElement
	protected String platformId;
	@XmlElement
	protected String typeId;
	@XmlElement
	protected String extensionId;
	@XmlElement
	protected String name;
	@XmlElement
	protected String description;
	@XmlElement
	protected Map<String, Object> properties;

	public ExtensionItemDTO() {
	}

	@XmlElement
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
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

	@XmlElement
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}
