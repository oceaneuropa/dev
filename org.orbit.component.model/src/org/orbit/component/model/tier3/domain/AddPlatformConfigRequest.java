package org.orbit.component.model.tier3.domain;

public class AddPlatformConfigRequest {

	protected String platformId;
	protected String name;
	protected String home;
	protected String hostURL;
	protected String contextRoot;

	public String getPlatformId() {
		return this.platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
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
		return this.hostURL;
	}

	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}

	public String getContextRoot() {
		return this.contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

}
