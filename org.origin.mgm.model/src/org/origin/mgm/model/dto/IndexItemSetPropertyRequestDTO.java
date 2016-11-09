package org.origin.mgm.model.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IndexItemSetPropertyRequestDTO {

	@XmlElement
	protected Integer indexItemId;
	@XmlElement
	protected String propName;
	@XmlElement
	protected Object propValue;
	@XmlElement
	protected String propType;

	public IndexItemSetPropertyRequestDTO() {
	}

	public IndexItemSetPropertyRequestDTO(Integer indexItemId, String propName, Object propValue, String propType) {
		this.indexItemId = indexItemId;
		this.propName = propName;
		this.propValue = propValue;
		this.propType = propType;
	}

	@XmlElement
	public Integer getIndexItemId() {
		return indexItemId;
	}

	public void setIndexItemId(Integer indexItemId) {
		this.indexItemId = indexItemId;
	}

	@XmlElement
	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	@XmlElement
	public Object getPropValue() {
		return propValue;
	}

	public void setPropValue(Object propValue) {
		this.propValue = propValue;
	}

	@XmlElement
	public String getPropType() {
		return propType;
	}

	public void setPropType(String propType) {
		this.propType = propType;
	}

}

// @Override
// public String toString() {
// StringBuilder sb = new StringBuilder();
// sb.append("SetIndexItemPropertyRequestDTO (");
// sb.append("indexItemId").append(this.indexItemId);
// sb.append(", propName").append(this.propName);
// sb.append(", propValue").append(this.propValue);
// sb.append(", propType").append(this.propType);
// sb.append(")");
// return sb.toString();
// }