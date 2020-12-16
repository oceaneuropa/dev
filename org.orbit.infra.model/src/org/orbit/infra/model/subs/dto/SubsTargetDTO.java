package org.orbit.infra.model.subs.dto;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.origin.common.json.JSONUtil;

@XmlRootElement
public class SubsTargetDTO {

	@XmlElement
	protected Long id;
	@XmlElement
	protected String name;
	@XmlElement
	protected String type;
	@XmlElement
	protected String typeId;
	@XmlElement
	protected String propertiesString;
	@XmlElement
	protected long createdTime;
	@XmlElement
	protected long modifiedTime;

	public SubsTargetDTO() {
	}

	@XmlElement
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
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
	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	@XmlElement
	public String getPropertiesString() {
		return this.propertiesString;
	}

	public void setPropertiesString(String propertiesString) {
		this.propertiesString = propertiesString;
	}

	public void setProperties(Map<String, Object> properties) {
		this.propertiesString = JSONUtil.toJsonString(properties);
	}

	@XmlElement
	public long getCreatedTime() {
		return this.createdTime;
	}

	public void setCreateTime(long createdTime) {
		this.createdTime = createdTime;
	}

	@XmlElement
	public long getModifiedTime() {
		return this.modifiedTime;
	}

	public void setModifiedTime(long modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

}
