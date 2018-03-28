package org.origin.common.service;

public class WebServiceAwareImpl implements WebServiceAware {

	protected String name;
	protected String hostURL;
	protected String contextRoot;

	/**
	 * 
	 * @param name
	 * @param hostURL
	 * @param contextRoot
	 */
	public WebServiceAwareImpl(String name, String hostURL, String contextRoot) {
		this.name = name;
		this.hostURL = hostURL;
		this.contextRoot = contextRoot;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

}
