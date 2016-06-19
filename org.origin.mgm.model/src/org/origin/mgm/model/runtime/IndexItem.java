package org.origin.mgm.model.runtime;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;

import org.origin.common.json.JSONUtil;
import org.origin.common.util.DateUtil;

public class IndexItem {

	public static final String DEFAULT_TYPE = XMLConstants.DEFAULT_NS_PREFIX;

	// /**
	// *
	// * @param namespace
	// * @param name
	// * @return
	// */
	// public static QName getQName(String namespace, String name) {
	// if (name == null || name.trim().isEmpty()) {
	// throw new IllegalArgumentException("name is null.");
	// }
	// namespace = namespace != null ? namespace : DEFAULT_TYPE;
	// return new QName(namespace, name);
	// }
	//
	// /**
	// *
	// * @param namespace
	// * @param name
	// * @return
	// */
	// public static String getFullName(String namespace, String name) {
	// return getQName(namespace, name).toString();
	// }

	protected Integer indexItemId;
	protected String indexProviderId;
	protected String namespace;
	protected String name;
	protected Map<String, Object> properties = new HashMap<String, Object>(); // properties that are persisted
	protected Map<String, Object> runtimeProperties = new HashMap<String, Object>(); // properties that exists only at runtime and not persisted
	protected Date createTime;
	protected Date lastUpdateTime;

	public IndexItem() {
	}

	public IndexItem clone() {
		IndexItem clone = new IndexItem(this.indexItemId, this.indexProviderId, this.namespace, this.name, this.properties, this.createTime, this.lastUpdateTime);
		clone.setRuntimeProperties(this.runtimeProperties);
		return clone;
	}

	/**
	 * 
	 * @param indexItemId
	 * @param indexProviderId
	 * @param namespace
	 * @param name
	 * @param properties
	 * @param createTime
	 * @param lastUpdateTime
	 */
	public IndexItem(Integer indexItemId, String indexProviderId, String namespace, String name, Map<String, Object> properties, Date createTime, Date lastUpdateTime) {
		if (indexItemId == null) {
			throw new IllegalArgumentException("indexItemId is null.");
		}
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("name is null.");
		}
		this.indexItemId = indexItemId;
		this.indexProviderId = indexProviderId;
		this.namespace = namespace != null ? namespace : DEFAULT_TYPE;
		this.name = name;
		this.properties = properties;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
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

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
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

	@Override
	public String toString() {
		String createTimeString = this.createTime != null ? DateUtil.toString(this.createTime, DateUtil.getJdbcDateFormat()) : null;
		String lastUpdateTimeString = this.lastUpdateTime != null ? DateUtil.toString(this.lastUpdateTime, DateUtil.getJdbcDateFormat()) : null;
		String propertiesString = JSONUtil.toJsonString(this.properties);
		String runtimePropertiesString = JSONUtil.toJsonString(this.runtimeProperties);

		StringBuilder sb = new StringBuilder();
		sb.append("IndexItem(");
		sb.append("indexItemId=").append(this.indexItemId);
		sb.append(", indexProviderId=").append(this.indexProviderId);
		sb.append(", namespace=").append(this.namespace);
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
