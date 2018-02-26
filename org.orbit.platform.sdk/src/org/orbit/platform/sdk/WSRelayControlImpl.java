package org.orbit.platform.sdk;

import java.util.HashMap;
import java.util.Map;

import org.orbit.platform.sdk.extension.util.ProgramExtension;
import org.origin.common.rest.server.WSRelayApplication;
import org.osgi.framework.BundleContext;

public abstract class WSRelayControlImpl implements WSRelayControl {

	protected Map<String, WSRelayApplication> wsAppMap = new HashMap<String, WSRelayApplication>();
	protected Map<String, ProgramExtension> extensionMap = new HashMap<String, ProgramExtension>();

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
