package org.orbit.component.runtime.relay.other;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.WSRelayApplication;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.switcher.Switcher;

/**
 * @see https://stackoverflow.com/questions/8242719/jersey-url-forwarding
 * 
 * @see https://stackoverflow.com/questions/25330700/jersey-redirect-after-post-to-outside-url/25332038#25332038
 *
 * @see https://stackoverflow.com/questions/11116687/redirecting-to-a-page-using-restful-methods
 * 
 * @see https://www.mkyong.com/webservices/jax-rs/restful-java-client-with-apache-httpclient/
 * 
 */
public class TransferAgentWSApplicationRelay extends WSRelayApplication {

	/**
	 * 
	 * @param contextRoot
	 * @param switcher
	 * @param factory
	 */
	public TransferAgentWSApplicationRelay(String contextRoot, Switcher<URI> switcher, WSClientFactory factory) {
		super(contextRoot, FeatureConstants.PING, switcher);

		Resource.Builder wsResource = Resource.builder("/");
		new WSMethodInflector(wsResource, "echo", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(wsResource, "level/{level1}/{level2}", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(wsResource, "request", POST, JSON, factory.createClient(null), switcher);

		registerResources(wsResource.build());
	}

}
