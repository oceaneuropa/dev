package org.orbit.component.runtime.model.domain;

public class NodeConfig {

	protected String id;
	protected String machineId;
	protected String platformId;
	protected String name;
	protected String home;
	protected String hostURL;
	protected String contextRoot;

	public NodeConfig() {
	}

	public NodeConfig(String id, String machineId, String platformId, String name, String home, String hostURL, String contextRoot) {
		this.id = id;
		this.machineId = machineId;
		this.platformId = platformId;
		this.name = name;
		this.home = home;
		this.hostURL = hostURL;
		this.contextRoot = contextRoot;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMachineId() {
		return this.machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeConfig other = (NodeConfig) obj;
		if (contextRoot == null) {
			if (other.contextRoot != null)
				return false;
		} else if (!contextRoot.equals(other.contextRoot))
			return false;
		if (home == null) {
			if (other.home != null)
				return false;
		} else if (!home.equals(other.home))
			return false;
		if (hostURL == null) {
			if (other.hostURL != null)
				return false;
		} else if (!hostURL.equals(other.hostURL))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (machineId == null) {
			if (other.machineId != null)
				return false;
		} else if (!machineId.equals(other.machineId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (platformId == null) {
			if (other.platformId != null)
				return false;
		} else if (!platformId.equals(other.platformId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NodeConfigRTO [id=" + id + ", machineId=" + machineId + ", platformId=" + platformId + ", name=" + name + ", home=" + home + ", hostURL=" + hostURL + ", contextRoot=" + contextRoot + "]";
	}

}
