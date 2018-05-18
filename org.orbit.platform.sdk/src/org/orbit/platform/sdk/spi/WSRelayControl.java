package org.orbit.platform.sdk.spi;

import java.net.URI;
import java.util.List;

import org.origin.common.rest.client.WSClientFactory;
import org.osgi.framework.BundleContext;

public interface WSRelayControl {

	public static final String EXTENSION_TYPE_ID = "platform.relay.control";

	/**
	 * Whether the web service relay should be started automatically.
	 * 
	 * @param bundleContext
	 * @return
	 */
	boolean isAutoStart(BundleContext bundleContext);

	/**
	 * Start web service to relay requests to target URLs.
	 * 
	 * @param bundleContext
	 * @param factory
	 * @param hostURL
	 * @param contextRoot
	 * @param targetURIs
	 */
	void start(BundleContext bundleContext, WSClientFactory factory, String hostURL, String contextRoot, List<URI> targetURIs);

	/**
	 * Stop web service.
	 * 
	 * @param bundleContext
	 * @param hostURL
	 * @param contextRoot
	 */
	void stop(BundleContext bundleContext, String hostURL, String contextRoot);

}
