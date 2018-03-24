package org.orbit.infra.model.indexes;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @see https://stackoverflow.com/questions/4486787/jackson-with-json-unrecognized-field-not-marked-as-ignorable
 *
 */
@XmlRootElement
public class IndexItemSetPropertiesRequestDTO {

	@XmlElement
	protected Integer indexItemId;
	@XmlElement
	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();

	public IndexItemSetPropertiesRequestDTO() {
	}

	public IndexItemSetPropertiesRequestDTO(Integer indexItemId, Map<String, Object> properties) {
		this.indexItemId = indexItemId;
		this.properties = properties;
	}

	@XmlElement
	public Integer getIndexItemId() {
		return indexItemId;
	}

	public void setIndexItemId(Integer indexItemId) {
		this.indexItemId = indexItemId;
	}

	@XmlElement
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}

// import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
// @JsonIgnoreProperties(ignoreUnknown = true)

// import org.codehaus.jackson.annotate.JsonIgnoreProperties;
// @JsonIgnoreProperties(ignoreUnknown = true)

// @XmlElement
// protected String propertiesString;

// public IndexItemSetPropertiesRequestDTO(Integer indexItemId, String propertiesString) {
// this.indexItemId = indexItemId;
// this.propertiesString = propertiesString;
// }

// @XmlElement
// public String getPropertiesString() {
// return propertiesString;
// }
//
// public void setPropertiesString(String propertiesString) {
// this.propertiesString = propertiesString;
// }

// @XmlElement
// protected Map<String, Object> properties;

// public IndexItemSetPropertiesRequestDTO(Integer indexItemId, Map<String, Object> properties) {
// this.indexItemId = indexItemId;
// this.properties = properties;
// }

// @XmlElement
// public Map<String, Object> getProperties() {
// return this.properties;
// }
//
// public void setProperties(Map<String, Object> properties) {
// this.properties = properties;
// }

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
