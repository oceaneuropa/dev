package org.orbit.infra.model.extensionregistry;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExtensionItemUpdateRequest {

	@XmlElement
	protected String platformId;
	@XmlElement
	protected String typeId;
	@XmlElement
	protected String extensionId;
	@XmlElement
	protected String newTypeId;
	@XmlElement
	protected String newExtensionId;
	@XmlElement
	protected String newName;
	@XmlElement
	protected String newDescription;
	@XmlElement
	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();

	public ExtensionItemUpdateRequest() {
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
	public String getNewTypeId() {
		return this.newTypeId;
	}

	public void setNewTypeId(String newTypeId) {
		this.newTypeId = newTypeId;
	}

	@XmlElement
	public String getNewExtensionId() {
		return this.newExtensionId;
	}

	public void setNewExtensionId(String newExtensionId) {
		this.newExtensionId = newExtensionId;
	}

	@XmlElement
	public String getNewName() {
		return this.newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	@XmlElement
	public String getNewDescription() {
		return this.newDescription;
	}

	public void setNewDescription(String newDescription) {
		this.newDescription = newDescription;
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
// protected String id;
// @XmlElement
// public String getId() {
// return this.id;
// }
//
// public void setId(String id) {
// this.id = id;
// }

// @XmlElement
// protected String name;
// @XmlElement
// protected String description;

// @XmlElement
// public String getName() {
// return this.name;
// }
//
// public void setName(String name) {
// this.name = name;
// }
//
// @XmlElement
// public String getDescription() {
// return this.description;
// }
//
// public void setDescription(String description) {
// this.description = description;
// }
