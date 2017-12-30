package org.orbit.component.runtime.switcher;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.AbstractJerseyWSApplication;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.switcher.Switcher;
import org.osgi.framework.BundleContext;

public class UserRegistryWSApplicationSwitcher extends AbstractJerseyWSApplication {

	protected Switcher<URI> switcher;

	public UserRegistryWSApplicationSwitcher(String contextRoot, WSClientFactory factory, Switcher<URI> switcher) {
		super(contextRoot);

		this.switcher = switcher;

		// Note:
		// - All access to this web service application are handled by one Switcher instance.
		// - Each API path has its own web service client instance
		Resource.Builder wsResource = Resource.builder("/");
		new WSMethodInflector(wsResource, "echo", GET, JSON, factory.createClient(null), this.switcher);

		Resource.Builder userAccountsWSResource = Resource.builder("/useraccounts");
		new WSMethodInflector(userAccountsWSResource, "", GET, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(userAccountsWSResource, "{userId}", GET, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(userAccountsWSResource, "{userId}/exists", GET, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(userAccountsWSResource, "", POST, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(userAccountsWSResource, "", PUT, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(userAccountsWSResource, "{userId}/activated", GET, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(userAccountsWSResource, "action", PUT, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(userAccountsWSResource, "{userId}", DELETE, JSON, factory.createClient(null), this.switcher);

		registerResources(wsResource.build());
		registerResources(userAccountsWSResource.build());
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
