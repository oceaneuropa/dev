package org.orbit.component.model.tier1.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class SetPropertiesDTO {

	@XmlElement
	protected String path;
	@XmlElement
	protected Map<String, String> properties = new LinkedHashMap<String, String>();

	@XmlElement
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@XmlElement
	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public void put(String key, String value) {
		this.properties.put(key, value);
	}

	@XmlTransient
	public String get(String key) {
		return this.properties.get(key);
	}

	@XmlTransient
	public String remove(String key) {
		return this.properties.remove(key);
	}

}
