package org.orbit.component.api.tier3.domain.request;

public class UpdateTransferAgentRequest {

	protected String agentId;
	protected String name;
	protected String TAHome;
	protected String hostURL;
	protected String contextRoot;

	public String getAgentId() {
		return this.agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTAHome() {
		return this.TAHome;
	}

	public void setTAHome(String tAHome) {
		this.TAHome = tAHome;
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

}
