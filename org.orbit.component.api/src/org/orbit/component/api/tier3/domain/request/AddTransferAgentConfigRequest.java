package org.orbit.component.api.tier3.domain.request;

public class AddTransferAgentConfigRequest {

	protected String transferAgentId;
	protected String name;
	protected String home;
	protected String hostURL;
	protected String contextRoot;

	public String getTransferAgentId() {
		return transferAgentId;
	}

	public void setTransferAgentId(String transferAgentId) {
		this.transferAgentId = transferAgentId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHome() {
		return this.home;
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

}
