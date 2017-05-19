package org.orbit.component.model.tier3.domain;

public class TransferAgentConfigRTO {

	protected String id;
	protected String name;
	protected String TAHome;
	protected String hostURL;
	protected String contextRoot;

	public TransferAgentConfigRTO() {
	}

	public TransferAgentConfigRTO(String id, String name, String tAHome, String hostURL, String contextRoot) {
		this.id = id;
		this.name = name;
		this.TAHome = tAHome;
		this.hostURL = hostURL;
		this.contextRoot = contextRoot;
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

	public void setTAHome(String TAHome) {
		this.TAHome = TAHome;
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
		TransferAgentConfigRTO other = (TransferAgentConfigRTO) obj;
		if (TAHome == null) {
			if (other.TAHome != null)
				return false;
		} else if (!TAHome.equals(other.TAHome))
			return false;
		if (contextRoot == null) {
			if (other.contextRoot != null)
				return false;
		} else if (!contextRoot.equals(other.contextRoot))
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TransferAgentConfigRTO [id=" + id + ", name=" + name + ", TAHome=" + TAHome + ", hostURL=" + hostURL + ", contextRoot=" + contextRoot + "]";
	}

}
