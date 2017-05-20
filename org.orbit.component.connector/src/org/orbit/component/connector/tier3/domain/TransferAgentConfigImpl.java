package org.orbit.component.connector.tier3.domain;

import org.orbit.component.api.tier3.domain.TransferAgentConfig;

public class TransferAgentConfigImpl implements TransferAgentConfig {

	protected String machineId;
	protected String id;
	protected String name;
	protected String TAHome;
	protected String hostURL;
	protected String contextRoot;

	public TransferAgentConfigImpl() {
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

	public String getTAHome() {
		return TAHome;
	}

	public void setTAHome(String tAHome) {
		TAHome = tAHome;
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
		return "TransferAgentConfigImpl [id=" + id + ", name=" + name + ", TAHome=" + TAHome + ", hostURL=" + hostURL + ", contextRoot=" + contextRoot + "]";
	}

}
