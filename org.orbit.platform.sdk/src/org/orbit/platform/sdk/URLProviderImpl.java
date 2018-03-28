package org.orbit.platform.sdk;

import org.origin.common.service.WebServiceAware;

public class URLProviderImpl implements URLProvider {

	protected WebServiceAware service;

	/**
	 * 
	 * @param service
	 */
	public URLProviderImpl(WebServiceAware service) {
		this.service = service;
	}

	/**
	 * 
	 * @param name
	 * @param hostURL
	 * @param contextRootl
	 */
	public URLProviderImpl(final String name, final String hostURL, final String contextRootl) {
		this.service = new WebServiceAware() {
			@Override
			public String getName() {
				return name;
			}

			@Override
			public String getHostURL() {
				return hostURL;
			}

			@Override
			public String getContextRoot() {
				return contextRootl;
			}
		};
	}

	@Override
	public String getURL() {
		String hostURL = this.service.getHostURL();
		String contextRoot = this.service.getContextRoot();
		if (hostURL == null || hostURL.isEmpty()) {
			return null;
		}
		String urlString = hostURL;
		if (contextRoot != null && !contextRoot.isEmpty()) {
			if (!hostURL.endsWith("/") && !contextRoot.startsWith("/")) {
				urlString += "/";
			}
			urlString += contextRoot;
		}
		return urlString;
	}

}
