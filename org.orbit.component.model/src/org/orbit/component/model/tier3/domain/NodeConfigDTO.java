package org.orbit.component.model.tier3.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Design time object for Node configuration.
 *
 */
@XmlRootElement
public class NodeConfigDTO {

	@XmlElement
	protected String id;
	@XmlElement
	protected String machineId;
	@XmlElement
	protected String transferAgentId;
	@XmlElement
	protected String name;
	@XmlElement
	protected String home;
	@XmlElement
	protected String hostURL;
	@XmlElement
	protected String contextRoot;

	@XmlElement
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement
	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	@XmlElement
	public String getTransferAgentId() {
		return transferAgentId;
	}

	public void setTransferAgentId(String transferAgentId) {
		this.transferAgentId = transferAgentId;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	@XmlElement
	public String getHostURL() {
		return hostURL;
	}

	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}

	@XmlElement
	public String getContextRoot() {
		return contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

}
