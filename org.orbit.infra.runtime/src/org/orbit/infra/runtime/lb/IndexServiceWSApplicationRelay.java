package org.orbit.infra.runtime.lb;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.server.WSRelayApplication;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.service.WebServiceAware;

public class IndexServiceWSApplicationRelay extends WSRelayApplication {

	/**
	 * 
	 * @param webServiceAware
	 * @param switcher
	 */
	public IndexServiceWSApplicationRelay(WebServiceAware webServiceAware, Switcher<URI> switcher) {
		super(webServiceAware, FeatureConstants.PING, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.ECHO, switcher);
		adapt(WebServiceAware.class, webServiceAware);

		// Note:
		// - All access to this web service application are handled by one Switcher instance.
		// - Each API path has its own web service client instance.
		// - A web service client instance for a API path will call one of the target URLs for that API path.
		Resource.Builder rootWSResource = Resource.builder("/");
		new WSMethodInflector(rootWSResource, "metadata", GET, JSON, createClient(), switcher);
		new WSMethodInflector(rootWSResource, "request", POST, JSON, createClient(), switcher);
		new WSMethodInflector(rootWSResource, "commandrequest", POST, JSON, createClient(), switcher);

		Resource.Builder indexItemsWSResource = Resource.builder("/indexitems/{indexproviderid}");
		new WSMethodInflector(indexItemsWSResource, "", GET, JSON, createClient(), switcher);
		new WSMethodInflector(indexItemsWSResource, "", POST, JSON, createClient(), switcher);
		new WSMethodInflector(indexItemsWSResource, "", DELETE, JSON, createClient(), switcher);

		Resource.Builder indexItemWSResource = Resource.builder("/indexitems/{indexproviderid}/{indexitemid}");
		new WSMethodInflector(indexItemWSResource, "", GET, JSON, createClient(), switcher);
		new WSMethodInflector(indexItemWSResource, "", DELETE, JSON, createClient(), switcher);
		new WSMethodInflector(indexItemWSResource, "properties", GET, JSON, createClient(), switcher);
		new WSMethodInflector(indexItemWSResource, "properties", POST, JSON, createClient(), switcher);
		new WSMethodInflector(indexItemWSResource, "property", POST, JSON, createClient(), switcher);
		new WSMethodInflector(indexItemWSResource, "properties", DELETE, JSON, createClient(), switcher);

		registerResources(rootWSResource.build());
		registerResources(indexItemsWSResource.build());
		registerResources(indexItemWSResource.build());
	}

}

// new WSMethodInflector(wsResource, "echo", GET, JSON, createClient(), switcher);
