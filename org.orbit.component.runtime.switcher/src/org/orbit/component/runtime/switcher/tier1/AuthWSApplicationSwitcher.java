package org.orbit.component.runtime.switcher.tier1;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.AbstractJerseyWSApplicationSwitcher;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.switcher.Switcher;

public class AuthWSApplicationSwitcher extends AbstractJerseyWSApplicationSwitcher {

	/**
	 * 
	 * @param contextRoot
	 * @param switcher
	 * @param factory
	 */
	public AuthWSApplicationSwitcher(String contextRoot, Switcher<URI> switcher, WSClientFactory factory) {
		super(contextRoot, FeatureConstants.PING, switcher);

		Resource.Builder wsResource = Resource.builder("/");

		// Note:
		// - All access to this web service application are handled by one Switcher instance.
		// - Each API path has its own web service client instance.
		// - A web service client instance for a API path will call one of the target URLs for that API path.
		new WSMethodInflector(wsResource, "echo", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(wsResource, "authorize", POST, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(wsResource, "token", POST, JSON, factory.createClient(null), switcher);

		registerResources(wsResource.build());
	}

}
