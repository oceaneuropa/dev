package org.orbit.infra.model.subs.dto;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.origin.common.json.JSONUtil;

@XmlRootElement
public class SubsSourceDTO {

	@XmlElement
	protected Integer id;
	@XmlElement
	protected String type;
	@XmlElement
	protected String typeId;
	@XmlElement
	protected String name;
	@XmlElement
	protected String properties;
	@XmlElement
	protected long dateCreated;
	@XmlElement
	protected long dateModified;

	public SubsSourceDTO() {
	}

	@XmlElement
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement
	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	@XmlElement
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getPropertiesString() {
		return this.properties;
	}

	public void setProperties(String propertiesString) {
		this.properties = propertiesString;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = JSONUtil.toJsonString(properties);
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
		return dateModified;
	}

	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

}
