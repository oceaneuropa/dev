package org.origin.common.service;

public class WebServiceAwareHelper {

	public static WebServiceAwareHelper INSTANCE = new WebServiceAwareHelper();

	/**
	 * 
	 * @param webServiceAware
	 * @return
	 */
	public String getURL(WebServiceAware webServiceAware) {
		String hostURL = webServiceAware.getHostURL();
		String contextRoot = webServiceAware.getContextRoot();

		if (hostURL == null) {
			hostURL = "";
		}
		if (contextRoot == null) {
			contextRoot = "";
		}

		String url = hostURL + contextRoot;
		if (!hostURL.endsWith("/") && !contextRoot.startsWith("/")) {
			url = hostURL + "/" + contextRoot;
		}
		return url;
	}

}
