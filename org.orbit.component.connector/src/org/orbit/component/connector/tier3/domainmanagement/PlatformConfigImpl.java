package org.orbit.component.connector.tier3.domainmanagement;

import org.orbit.component.api.tier3.domainmanagement.PlatformConfig;

public class PlatformConfigImpl implements PlatformConfig {

	protected String machineId;
	protected String id;
	protected String name;
	protected String home;
	protected String hostURL;
	protected String contextRoot;

	public PlatformConfigImpl() {
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getHostURL() {
		return hostURL;
	}

	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}

	public String getContextRoot() {
		return contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	@Override
	public String toString() {
		return "TransferAgentConfigImpl [id=" + id + ", name=" + name + ", home=" + home + ", hostURL=" + hostURL + ", contextRoot=" + contextRoot + "]";
	}

}
