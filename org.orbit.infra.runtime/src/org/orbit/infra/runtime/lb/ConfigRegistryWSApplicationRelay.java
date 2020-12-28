package org.orbit.infra.runtime.lb;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.server.WSRelayApplication;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.service.IWebService;

public class ConfigRegistryWSApplicationRelay extends WSRelayApplication {

	/**
	 * 
	 * @param webService
	 * @param switcher
	 */
	public ConfigRegistryWSApplicationRelay(IWebService webService, Switcher<URI> switcher) {
		super(webService, FeatureConstants.PING, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.ECHO, switcher);

		adapt(IWebService.class, webService);

		Resource.Builder rootWSResource = Resource.builder("/");
		new WSMethodInflector(rootWSResource, "metadata", GET, JSON, createClient(), switcher);
		new WSMethodInflector(rootWSResource, "request", POST, JSON, createClient(), switcher);

		registerResources(rootWSResource.build());
	}

}
