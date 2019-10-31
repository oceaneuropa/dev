package org.orbit.infra.model.indexes;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;

import org.origin.common.json.JSONUtil;
import org.origin.common.util.DateUtil;

public class IndexItem {

	public static final String DEFAULT_TYPE = XMLConstants.DEFAULT_NS_PREFIX;

	protected Integer indexItemId;
	protected String indexProviderId;
	protected String type;
	protected String name;
	protected Map<String, Object> properties = new HashMap<String, Object>(); // properties that are persisted
	protected Map<String, Object> runtimeProperties = new HashMap<String, Object>(); // properties that exists only at runtime and not persisted
	protected Date dateCreated;
	protected Date dateModified;

	public IndexItem() {
	}

	public IndexItem clone() {
		IndexItem clone = new IndexItem(this.indexItemId, this.indexProviderId, this.type, this.name, this.properties, this.dateCreated, this.dateModified);
		clone.setRuntimeProperties(this.runtimeProperties);
		return clone;
	}

	/**
	 * 
	 * @param indexItemId
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param properties
	 * @param createTime
	 * @param lastUpdateTime
	 */
	public IndexItem(Integer indexItemId, String indexProviderId, String type, String name, Map<String, Object> properties, Date createTime, Date lastUpdateTime) {
		if (indexItemId == null) {
			throw new IllegalArgumentException("indexItemId is null.");
		}
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("name is null.");
		}
		this.indexItemId = indexItemId;
		this.indexProviderId = indexProviderId;
		this.type = type != null ? type : DEFAULT_TYPE;
		this.name = name;
		this.properties = properties;
		this.dateCreated = createTime;
		this.dateModified = lastUpdateTime;
	}

	public Integer getIndexItemId() {
		return indexItemId;
	}

	public void setIndexItemId(Integer indexItemId) {
		this.indexItemId = indexItemId;
	}

	public String getIndexProviderId() {
		return indexProviderId;
	}

	public void setIndexProviderId(String indexProviderId) {
		this.indexProviderId = indexProviderId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("name is null.");
		}
		this.name = name;
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

	public boolean hasRuntimeProperty(String name) {
		return this.runtimeProperties.containsKey(name);
	}

	public Object getRuntimeProperty(String name) {
		return this.runtimeProperties.get(name);
	}

	public void setRuntimeProperty(String name, Object value) {
		this.runtimeProperties.put(name, value);
	}

	public Map<String, Object> getRuntimeProperties() {
		return this.runtimeProperties;
	}

	public void setRuntimeProperties(Map<String, Object> runtimeProperties) {
		this.runtimeProperties = runtimeProperties;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	@Override
	public String toString() {
		String createTimeString = this.dateCreated != null ? DateUtil.toString(this.dateCreated, DateUtil.getJdbcDateFormat()) : null;
		String lastUpdateTimeString = this.dateModified != null ? DateUtil.toString(this.dateModified, DateUtil.getJdbcDateFormat()) : null;
		String propertiesString = JSONUtil.toJsonString(this.properties);
		String runtimePropertiesString = JSONUtil.toJsonString(this.runtimeProperties);

		StringBuilder sb = new StringBuilder();
		sb.append("IndexItem(");
		sb.append("indexItemId=").append(this.indexItemId);
		sb.append(", indexProviderId=").append(this.indexProviderId);
		sb.append(", type=").append(this.type);
		sb.append(", name=").append(this.name);
		sb.append(", properties=").append(propertiesString);
		sb.append(", runtimeProperties=").append(runtimePropertiesString);
		sb.append(", createTime=").append(createTimeString);
		sb.append(", lastUpdateTime=").append(lastUpdateTimeString);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return this.indexItemId.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof IndexItem)) {
			return false;
		}
		IndexItem other = (IndexItem) object;
		if (this.indexItemId.equals(other.getIndexItemId())) {
			return true;
		}
		return false;
	}

}
