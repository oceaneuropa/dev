package org.orbit.infra.runtime.lb;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.server.WSRelayApplication;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.service.WebServiceAware;

public class ChannelWSApplicationRelay extends WSRelayApplication {

	/**
	 * 
	 * @param webServiceAware
	 * @param switcher
	 */
	public ChannelWSApplicationRelay(WebServiceAware webServiceAware, Switcher<URI> switcher) {
		super(webServiceAware, FeatureConstants.PING, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.ECHO, switcher);

		adapt(WebServiceAware.class, webServiceAware);

		Resource.Builder wsResource = Resource.builder("/");
		// new WSMethodInflector(wsResource, "echo", GET, JSON, createClient(), switcher);
		new WSMethodInflector(wsResource, "inbound", POST, JSON, createClient(), switcher);

		registerResources(wsResource.build());
	}

}
