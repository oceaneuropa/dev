package org.orbit.infra.model.indexes;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.origin.common.json.JSONUtil;

@XmlRootElement
public class IndexItemDTO {

	@XmlElement
	protected Integer indexItemId;
	@XmlElement
	protected String indexProviderId;
	@XmlElement
	protected String type;
	@XmlElement
	protected String name;
	// @XmlElement
	// protected Map<String, Object> properties;
	@XmlElement
	protected String propertiesString;

	public IndexItemDTO() {
	}

	@XmlElement
	public Integer getIndexItemId() {
		return indexItemId;
	}

	public void setIndexItemId(Integer indexItemId) {
		this.indexItemId = indexItemId;
	}

	@XmlElement
	public String getIndexProviderId() {
		return indexProviderId;
	}

	public void setIndexProviderId(String indexProviderId) {
		this.indexProviderId = indexProviderId;
	}

	@XmlElement
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getPropertiesString() {
		return propertiesString;
	}

	public void setPropertiesString(String propertiesString) {
		this.propertiesString = propertiesString;
	}

	public void setProperties(Map<String, Object> properties) {
		this.propertiesString = JSONUtil.toJsonString(properties);
	}

	// @XmlElement
	// public Map<String, Object> getProperties() {
	// return properties;
	// }
	//
	// public void setProperties(Map<String, Object> properties) {
	// this.properties = properties;
	// }

	// @Override
	// public String toString() {
	// String propertiesString = JSONUtil.toJsonString(this.properties);
	//
	// StringBuilder sb = new StringBuilder();
	// sb.append("IndexItemDTO (");
	// sb.append("indexProviderId='").append(this.indexProviderId).append("'");
	// sb.append(", type=").append(this.type);
	// sb.append(", name=").append(this.name);
	// sb.append(", properties=").append(propertiesString);
	// sb.append(")");
	// return sb.toString();
	// }

}
