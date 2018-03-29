package org.orbit.platform.sdk.extensions.impl;

import java.util.HashMap;
import java.util.Map;

import org.orbit.platform.sdk.extensions.WSRelayControl;
import org.origin.common.extensions.Extension;
import org.origin.common.rest.server.WSRelayApplication;
import org.osgi.framework.BundleContext;

public abstract class WSRelayControlImpl implements WSRelayControl {

	protected Map<String, WSRelayApplication> wsAppMap = new HashMap<String, WSRelayApplication>();
	protected Map<String, Extension> extensionMap = new HashMap<String, Extension>();

	@Override
	public boolean isAutoStart(BundleContext bundleContext) {
		return false;
	}

	protected String getURL(String hostURL, String contextRoot) {
		if (hostURL == null) {
			hostURL = "";
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
