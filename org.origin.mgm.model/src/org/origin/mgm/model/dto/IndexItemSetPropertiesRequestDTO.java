package org.origin.mgm.model.dto;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IndexItemSetPropertiesRequestDTO {

	@XmlElement
	protected Integer indexItemId;
//	@XmlElement
//	protected Map<String, Object> properties;
	@XmlElement
	protected String propertiesString;

	public IndexItemSetPropertiesRequestDTO() {
	}

//	public IndexItemSetPropertiesRequestDTO(Integer indexItemId, Map<String, Object> properties) {
//		this.indexItemId = indexItemId;
//		this.properties = properties;
//	}

	public IndexItemSetPropertiesRequestDTO(Integer indexItemId, String propertiesString) {
		this.indexItemId = indexItemId;
		this.propertiesString = propertiesString;
	}

	@XmlElement
	public Integer getIndexItemId() {
		return indexItemId;
	}

	public void setIndexItemId(Integer indexItemId) {
		this.indexItemId = indexItemId;
	}

//	@XmlElement
//	public Map<String, Object> getProperties() {
//		return this.properties;
//	}
//
//	public void setProperties(Map<String, Object> properties) {
//		this.properties = properties;
//	}

	@XmlElement
	public String getPropertiesString() {
		return propertiesString;
	}

	public void setPropertiesString(String propertiesString) {
		this.propertiesString = propertiesString;
	}

	// @XmlTransient
	// @Override
	// public String toString() {
	// String propertiesString = JSONUtil.toJsonString((Map<String, Object>) this.properties);
	// StringBuilder sb = new StringBuilder();
	// sb.append("SetIndexItemPropertiesRequestDTO (");
	// sb.append("indexItemId=").append(this.indexItemId);
	// sb.append(", properties=").append(propertiesString);
	// sb.append(")");
	// return sb.toString();
	// }

}
