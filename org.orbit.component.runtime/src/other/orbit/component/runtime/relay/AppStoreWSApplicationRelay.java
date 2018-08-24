package other.orbit.component.runtime.relay;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.server.WSRelayApplication;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.service.WebServiceAwareImpl;

public class AppStoreWSApplicationRelay extends WSRelayApplication {

	/**
	 * 
	 * @param contextRoot
	 * @param switcher
	 * @param factory
	 */
	public AppStoreWSApplicationRelay(String contextRoot, Switcher<URI> switcher, WSClientFactory factory) {
		super(new WebServiceAwareImpl(null, null, contextRoot), FeatureConstants.PING, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.ECHO, switcher);

		Resource.Builder appsWSResource = Resource.builder("/apps");
		new WSMethodInflector(appsWSResource, "", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(appsWSResource, "query", POST, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(appsWSResource, "{appId}/{appVersion}", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(appsWSResource, "{appId}/{appVersion}/exists", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(appsWSResource, "", POST, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(appsWSResource, "", PUT, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(appsWSResource, "{appId}/{appVersion}", DELETE, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(appsWSResource, "{appId}/{appVersion}/content", POST, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(appsWSResource, "{appId}/{appVersion}/content", GET, JSON, factory.createClient(null), switcher);

		registerResources(appsWSResource.build());
	}

}

// Resource.Builder wsResource = Resource.builder("/");
// new WSMethodInflector(wsResource, "echo", GET, JSON, factory.createClient(null), switcher);
// registerResources(wsResource.build());
