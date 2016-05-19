package org.origin.mgm.model.dto;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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

}
