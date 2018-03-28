package org.orbit.component.connector.tier3.domainmanagement;

import org.orbit.component.api.tier3.domainmanagement.NodeConfig;

public class NodeConfigImpl implements NodeConfig {

	protected String id;
	protected String machineId;
	protected String platformId;
	protected String name;
	protected String home;
	protected String hostURL;
	protected String contextRoot;

	@Override
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getMachineId() {
		return this.machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	@Override
	public String getPlatformId() {
		return this.platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getHome() {
		return this.home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	@Override
	public String getHostURL() {
		return this.hostURL;
	}

	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}

	@Override
	public String getContextRoot() {
		return this.contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	@Override
	public String toString() {
		return "NodeConfigRTO [id=" + id + ", machineId=" + machineId + ", platformId=" + platformId + ", name=" + name + ", home=" + home + ", hostURL=" + hostURL + ", contextRoot=" + contextRoot + "]";
	}

}
