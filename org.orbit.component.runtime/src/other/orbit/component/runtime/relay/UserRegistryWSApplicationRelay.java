package other.orbit.component.runtime.relay;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.server.WSRelayApplication;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.service.WebServiceImpl;

public class UserRegistryWSApplicationRelay extends WSRelayApplication {

	/**
	 * 
	 * @param contextRoot
	 * @param switcher
	 * @param factory
	 */
	public UserRegistryWSApplicationRelay(String contextRoot, Switcher<URI> switcher, WSClientFactory factory) {
		super(new WebServiceImpl(null, null, contextRoot), FeatureConstants.PING, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.ECHO, switcher);

		Resource.Builder userAccountsWSResource = Resource.builder("/useraccounts");
		new WSMethodInflector(userAccountsWSResource, "", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(userAccountsWSResource, "{accountId}", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(userAccountsWSResource, "{accountId}/exists", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(userAccountsWSResource, "", POST, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(userAccountsWSResource, "", PUT, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(userAccountsWSResource, "{accountId}/activated", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(userAccountsWSResource, "action", PUT, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(userAccountsWSResource, "{accountId}", DELETE, JSON, factory.createClient(null), switcher);

		registerResources(userAccountsWSResource.build());
	}

}

// Resource.Builder wsResource = Resource.builder("/");
// new WSMethodInflector(wsResource, "echo", GET, JSON, factory.createClient(null), switcher);
// registerResources(wsResource.build());
