package org.orbit.infra.runtime.extensionregistry.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.origin.common.json.JSONUtil;
import org.origin.common.util.DateUtil;

public class ExtensionItem {

	protected Integer id;
	protected String platformId;
	protected String typeId;
	protected String extensionId;
	protected String name;
	protected String description;
	protected Map<String, Object> properties = new HashMap<String, Object>(); // properties that are persisted
	protected Date createTime;
	protected Date lastUpdateTime;

	public ExtensionItem() {
	}

	/**
	 * 
	 * @param id
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @param name
	 * @param description
	 * @param properties
	 * @param createTime
	 * @param lastUpdateTime
	 */
	public ExtensionItem(Integer id, String platformId, String typeId, String extensionId, String name, String description, Map<String, Object> properties, Date createTime, Date lastUpdateTime) {
		checkId(id);
		checkPlatformId(platformId);
		checkTypeId(typeId);
		checkExtensionId(extensionId);

		this.id = id;
		this.platformId = platformId;
		this.typeId = typeId;
		this.extensionId = extensionId;
		this.name = name;
		this.description = description;
		this.properties = properties;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
	}

	protected void checkId(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("id is null.");
		}
	}

	protected void checkPlatformId(String platformId) {
		if (platformId == null || platformId.trim().isEmpty()) {
			throw new IllegalArgumentException("platformId is empty.");
		}
	}

	protected void checkTypeId(String typeId) {
		if (typeId == null || typeId.trim().isEmpty()) {
			throw new IllegalArgumentException("typeId is empty.");
		}
	}

	protected void checkExtensionId(String extensionId) {
		if (extensionId == null || extensionId.trim().isEmpty()) {
			throw new IllegalArgumentException("extensionId is empty.");
		}
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		checkId(id);
		this.id = id;
	}

	public String getPlatformId() {
		return this.platformId;
	}

	public void setPlatformId(String platformId) {
		checkPlatformId(platformId);
		this.platformId = platformId;
	}

	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		checkTypeId(typeId);
		this.typeId = typeId;
	}

	public String getExtensionId() {
		return this.extensionId;
	}

	public void setExtensionId(String extensionId) {
		checkExtensionId(extensionId);
		this.extensionId = extensionId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean hasProperty(String name) {
		return this.properties.containsKey(name);
	}

	public Object getProperty(String name) {
		return this.properties.get(name);
	}

	public void setProperty(String name, Object value) {
		this.properties.put(name, value);
	}

	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public ExtensionItem clone() {
		ExtensionItem clone = new ExtensionItem(this.id, this.platformId, this.typeId, this.extensionId, this.name, this.description, this.properties, this.createTime, this.lastUpdateTime);
		return clone;
	}

	@Override
	public String toString() {
		String createTimeString = this.createTime != null ? DateUtil.toString(this.createTime, DateUtil.getJdbcDateFormat()) : null;
		String lastUpdateTimeString = this.lastUpdateTime != null ? DateUtil.toString(this.lastUpdateTime, DateUtil.getJdbcDateFormat()) : null;
		String propertiesString = JSONUtil.toJsonString(this.properties);

		StringBuilder sb = new StringBuilder();
		sb.append("ExtensionItem(");
		sb.append("id=").append(this.id);
		sb.append(", platformId=").append(this.platformId);
		sb.append(", typeId=").append(this.typeId);
		sb.append(", extensionId=").append(this.extensionId);
		sb.append(", name=").append(this.name);
		sb.append(", description=").append(this.description);
		sb.append(", properties=").append(propertiesString);
		sb.append(", createTime=").append(createTimeString);
		sb.append(", lastUpdateTime=").append(lastUpdateTimeString);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.platformId == null) ? 0 : this.platformId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof ExtensionItem)) {
			return false;
		}
		ExtensionItem other = (ExtensionItem) object;
		if (this.platformId.equals(other.getPlatformId())) {
			if (this.id.equals(other.getId())) {
				return true;
			}
		}
		return false;
	}

}
