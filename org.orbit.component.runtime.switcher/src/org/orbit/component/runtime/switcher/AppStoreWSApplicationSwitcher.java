package org.orbit.component.runtime.switcher;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.AbstractJerseyWSApplication;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.switcher.Switcher;
import org.osgi.framework.BundleContext;

public class AppStoreWSApplicationSwitcher extends AbstractJerseyWSApplication {

	protected Switcher<URI> switcher;

	/**
	 * 
	 * @param contextRoot
	 * @param factory
	 * @param switcher
	 */
	public AppStoreWSApplicationSwitcher(String contextRoot, WSClientFactory factory, Switcher<URI> switcher) {
		super(contextRoot, FeatureConstants.PING);
		this.switcher = switcher;

		// Note:
		// - All access to this web service application are handled by one Switcher instance.
		// - Each API path has its own web service client instance
		Resource.Builder wsResource = Resource.builder("/");
		new WSMethodInflector(wsResource, "echo", GET, JSON, factory.createClient(null), this.switcher);

		Resource.Builder appsWSResource = Resource.builder("/apps");
		new WSMethodInflector(appsWSResource, "", GET, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(appsWSResource, "query", POST, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(appsWSResource, "{appId}/{appVersion}", GET, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(appsWSResource, "{appId}/{appVersion}/exists", GET, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(appsWSResource, "", POST, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(appsWSResource, "", PUT, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(appsWSResource, "{appId}/{appVersion}", DELETE, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(appsWSResource, "{appId}/{appVersion}/content", POST, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(appsWSResource, "{appId}/{appVersion}/content", GET, JSON, factory.createClient(null), this.switcher);

		registerResources(wsResource.build());
		registerResources(appsWSResource.build());
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
