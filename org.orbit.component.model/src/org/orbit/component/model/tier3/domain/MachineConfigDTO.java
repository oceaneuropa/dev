package org.orbit.component.model.tier3.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Design time object for Machine configuration.
 *
 */
@XmlRootElement
public class MachineConfigDTO {

	@XmlElement
	protected String id;
	@XmlElement
	protected String name;
	@XmlElement
	protected String ipAddress;

	@XmlElement
	protected List<String> fieldsToUpdate;

	@XmlElement
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public List<String> getFieldsToUpdate() {
		return fieldsToUpdate;
	}

	public void setFieldsToUpdate(List<String> fieldsToUpdate) {
		this.fieldsToUpdate = fieldsToUpdate;
	}

}
