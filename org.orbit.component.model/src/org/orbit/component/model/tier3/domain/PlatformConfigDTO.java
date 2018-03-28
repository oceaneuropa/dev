package org.orbit.component.model.tier3.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Design time object for TransferAgent configuration.
 *
 */
@XmlRootElement
public class PlatformConfigDTO {

	@XmlElement
	protected String machineId;
	@XmlElement
	protected String id;
	@XmlElement
	protected String name;
	@XmlElement
	protected String home;
	@XmlElement
	protected String hostURL;
	@XmlElement
	protected String contextRoot;

	@XmlElement
	protected List<String> fieldsToUpdate;

	@XmlElement
	public String getMachineId() {
		return this.machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	@XmlElement
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getHome() {
		return this.home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	@XmlElement
	public String getHostURL() {
		return this.hostURL;
	}

	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}

	@XmlElement
	public String getContextRoot() {
		return this.contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	@XmlElement
	public List<String> getFieldsToUpdate() {
		return this.fieldsToUpdate;
	}

	public void setFieldsToUpdate(List<String> fieldsToUpdate) {
		this.fieldsToUpdate = fieldsToUpdate;
	}

}
