package other.orbit.component.runtime.relay;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.server.WSRelayApplication;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.service.WebServiceImpl;

public class MissionControlWSApplicationRelay extends WSRelayApplication {

	/**
	 * 
	 * @param contextRoot
	 * @param switcher
	 * @param factory
	 */
	public MissionControlWSApplicationRelay(String contextRoot, Switcher<URI> switcher, WSClientFactory factory) {
		super(new WebServiceImpl(null, null, contextRoot), FeatureConstants.PING, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.ECHO, switcher);

		Resource.Builder wsResource = Resource.builder("/");
		// new WSMethodInflector(wsResource, "echo", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(wsResource, "request", POST, JSON, factory.createClient(null), switcher);
		registerResources(wsResource.build());
	}

}
