package org.orbit.component.runtime.switcher.tier4;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.AbstractJerseyWSApplicationSwitcher;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.switcher.Switcher;

public class MissionControlWSApplicationSwitcher extends AbstractJerseyWSApplicationSwitcher {

	/**
	 * 
	 * @param contextRoot
	 * @param switcher
	 * @param factory
	 */
	public MissionControlWSApplicationSwitcher(String contextRoot, Switcher<URI> switcher, WSClientFactory factory) {
		super(contextRoot, FeatureConstants.PING, switcher);

		Resource.Builder wsResource = Resource.builder("/");
		new WSMethodInflector(wsResource, "echo", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(wsResource, "request", POST, JSON, factory.createClient(null), switcher);

		registerResources(wsResource.build());
	}

}
