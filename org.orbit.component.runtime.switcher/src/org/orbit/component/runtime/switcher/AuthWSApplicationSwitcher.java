package org.orbit.component.runtime.switcher;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.AbstractJerseyWSApplication;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.switcher.Switcher;
import org.osgi.framework.BundleContext;

public class AuthWSApplicationSwitcher extends AbstractJerseyWSApplication {

	protected Switcher<URI> switcher;

	/**
	 * 
	 * @param contextRoot
	 * @param factory
	 * @param switcher
	 */
	public AuthWSApplicationSwitcher(String contextRoot, WSClientFactory factory, Switcher<URI> switcher) {
		super(contextRoot, FeatureConstants.PING);
		this.switcher = switcher;

		Resource.Builder wsResource = Resource.builder("/");

		// Note:
		// - All access to this web service application are handled by one Switcher instance.
		// - Each API path has its own web service client instance
		new WSMethodInflector(wsResource, "echo", GET, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(wsResource, "authorize", POST, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(wsResource, "token", POST, JSON, factory.createClient(null), this.switcher);

		registerResources(wsResource.build());
	}

	@Override
	public void start(BundleContext bundleContext) {
		this.switcher.start();

		super.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) {
		super.stop(bundleContext);

		this.switcher.stop();
	}

}
