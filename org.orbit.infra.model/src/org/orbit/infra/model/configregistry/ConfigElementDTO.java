package org.orbit.infra.model.configregistry;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConfigElementDTO {

	@XmlElement
	protected String configRegistryId;
	@XmlElement
	protected String parentElementId;
	@XmlElement
	protected String elementId;
	@XmlElement
	protected String path;
	@XmlElement
	protected Map<String, Object> attributes;
	@XmlElement
	protected long dateCreated;
	@XmlElement
	protected long dateModified;

	@XmlElement
	public String getConfigRegistryId() {
		return this.configRegistryId;
	}

	public void setConfigRegistryId(String configRegistryId) {
		this.configRegistryId = configRegistryId;
	}

	@XmlElement
	public String getParentElementId() {
		return this.parentElementId;
	}

	public void setParentElementId(String parentElementId) {
		this.parentElementId = parentElementId;
	}

	@XmlElement
	public String getElementId() {
		return this.elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	@XmlElement
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@XmlElement
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@XmlElement
	public long getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	@XmlElement
	public long getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

}
