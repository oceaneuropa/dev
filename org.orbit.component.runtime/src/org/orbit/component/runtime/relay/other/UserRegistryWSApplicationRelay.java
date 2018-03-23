package org.orbit.component.runtime.relay.other;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.WSRelayApplication;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.switcher.Switcher;

public class UserRegistryWSApplicationRelay extends WSRelayApplication {

	/**
	 * 
	 * @param contextRoot
	 * @param switcher
	 * @param factory
	 */
	public UserRegistryWSApplicationRelay(String contextRoot, Switcher<URI> switcher, WSClientFactory factory) {
		super(contextRoot, FeatureConstants.PING, switcher);

		Resource.Builder wsResource = Resource.builder("/");
		new WSMethodInflector(wsResource, "echo", GET, JSON, factory.createClient(null), switcher);

		Resource.Builder userAccountsWSResource = Resource.builder("/useraccounts");
		new WSMethodInflector(userAccountsWSResource, "", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(userAccountsWSResource, "{userId}", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(userAccountsWSResource, "{userId}/exists", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(userAccountsWSResource, "", POST, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(userAccountsWSResource, "", PUT, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(userAccountsWSResource, "{userId}/activated", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(userAccountsWSResource, "action", PUT, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(userAccountsWSResource, "{userId}", DELETE, JSON, factory.createClient(null), switcher);

		registerResources(wsResource.build());
		registerResources(userAccountsWSResource.build());
	}

}
