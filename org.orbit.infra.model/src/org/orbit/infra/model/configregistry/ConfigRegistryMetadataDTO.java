package org.orbit.infra.model.configregistry;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConfigRegistryMetadataDTO {

	@XmlElement
	protected String id;
	@XmlElement
	protected String type;
	@XmlElement
	protected String name;
	@XmlElement
	protected Map<String, Object> properties;
	@XmlElement
	protected long dateCreated;
	@XmlElement
	protected long dateModified;

	@XmlElement
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
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
