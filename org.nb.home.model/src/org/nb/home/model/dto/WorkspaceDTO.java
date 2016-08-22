package org.nb.home.model.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.origin.common.json.JSONUtil;

/**
 * DTO for workspace
 *
 */
@XmlRootElement
public class WorkspaceDTO {

	@XmlElement
	protected String name;
	@XmlElement
	protected String managementId;
	@XmlElement
	protected String propertiesString;

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getManagementId() {
		return managementId;
	}

	public void setManagementId(String managementId) {
		this.managementId = managementId;
	}

	@XmlElement
	public String getPropertiesString() {
		return propertiesString;
	}

	public void setPropertiesString(String propertiesString) {
		this.propertiesString = propertiesString;
	}

	@XmlTransient
	public Map<String, Object> getProperties() {
		Map<String, Object> properties = null;
		if (this.propertiesString != null) {
			properties = JSONUtil.toProperties(this.propertiesString);
		}
		if (properties == null) {
			properties = new LinkedHashMap<String, Object>();
		}
		return properties;
	}

}
