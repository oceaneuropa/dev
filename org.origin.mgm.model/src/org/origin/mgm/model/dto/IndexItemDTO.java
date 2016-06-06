package org.origin.mgm.model.dto;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.origin.common.json.JSONUtil;

@XmlRootElement
public class IndexItemDTO {

	@XmlElement
	protected String indexProviderId;
	@XmlElement
	protected String namespace;
	@XmlElement
	protected String name;
	@XmlElement
	protected Map<String, Object> properties;

	@XmlElement
	public String getIndexProviderId() {
		return indexProviderId;
	}

	public void setIndexProviderId(String indexProviderId) {
		this.indexProviderId = indexProviderId;
	}

	@XmlElement
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		String propertiesString = JSONUtil.toJsonString(this.properties);

		StringBuilder sb = new StringBuilder();
		sb.append("IndexItemDTO (");
		sb.append("indexProviderId='").append(this.indexProviderId).append("'");
		sb.append(", namespace=").append(this.namespace);
		sb.append(", name=").append(this.name);
		sb.append(", properties=").append(propertiesString);
		sb.append(")");
		return sb.toString();
	}

}
