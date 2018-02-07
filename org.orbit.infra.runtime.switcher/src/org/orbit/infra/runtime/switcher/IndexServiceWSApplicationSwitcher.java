package org.orbit.infra.runtime.switcher;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.WSApplicationSwitcher;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.switcher.Switcher;

public class IndexServiceWSApplicationSwitcher extends WSApplicationSwitcher {

	/**
	 * 
	 * @param contextRoot
	 * @param switcher
	 * @param factory
	 */
	public IndexServiceWSApplicationSwitcher(String contextRoot, Switcher<URI> switcher, WSClientFactory factory) {
		super(contextRoot, FeatureConstants.PING, switcher);

		// Note:
		// - All access to this web service application are handled by one Switcher instance.
		// - Each API path has its own web service client instance.
		// - A web service client instance for a API path will call one of the target URLs for that API path.
		Resource.Builder wsResource = Resource.builder("/");
		new WSMethodInflector(wsResource, "echo", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(wsResource, "commandrequest", POST, JSON, factory.createClient(null), switcher);

		Resource.Builder indexItemsWSResource = Resource.builder("/indexitems/{indexproviderid}");
		new WSMethodInflector(indexItemsWSResource, "", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(indexItemsWSResource, "", POST, JSON, factory.createClient(null), switcher);

		Resource.Builder indexItemWSResource = Resource.builder("/indexitems/{indexproviderid}/{indexitemid}");
		new WSMethodInflector(indexItemWSResource, "", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(indexItemWSResource, "", DELETE, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(indexItemWSResource, "properties", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(indexItemWSResource, "properties", POST, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(indexItemWSResource, "property", POST, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(indexItemWSResource, "properties", DELETE, JSON, factory.createClient(null), switcher);

		registerResources(wsResource.build());
		registerResources(indexItemsWSResource.build());
		registerResources(indexItemWSResource.build());
	}

}
