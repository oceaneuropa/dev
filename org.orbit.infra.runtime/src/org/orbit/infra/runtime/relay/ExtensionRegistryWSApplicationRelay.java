package org.orbit.infra.runtime.relay;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.server.WSRelayApplication;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.service.WebServiceAware;

public class ExtensionRegistryWSApplicationRelay extends WSRelayApplication {

	/**
	 * 
	 * @param webServiceAware
	 * @param switcher
	 */
	public ExtensionRegistryWSApplicationRelay(WebServiceAware webServiceAware, Switcher<URI> switcher) {
		super(webServiceAware, FeatureConstants.PING, switcher);
		adapt(WebServiceAware.class, webServiceAware);

		// Note:
		// - All access to this web service application are handled by one Switcher instance.
		// - Each API path has its own web service client instance.
		// - A web service client instance for a API path will call one of the target URLs for that API path.
		Resource.Builder wsResource = Resource.builder("/");
		new WSMethodInflector(wsResource, "echo", GET, JSON, createClient(), switcher);

		Resource.Builder extensionItemsWSResource = Resource.builder("/extensionitems");
		new WSMethodInflector(extensionItemsWSResource, "", GET, JSON, createClient(), switcher);
		new WSMethodInflector(extensionItemsWSResource, "", POST, JSON, createClient(), switcher);
		new WSMethodInflector(extensionItemsWSResource, "", PUT, JSON, createClient(), switcher);
		new WSMethodInflector(extensionItemsWSResource, "", DELETE, JSON, createClient(), switcher);

		Resource.Builder extensionItemWSResource = Resource.builder("/extensionitem");
		new WSMethodInflector(extensionItemWSResource, "", GET, JSON, createClient(), switcher);
		new WSMethodInflector(extensionItemWSResource, "", POST, JSON, createClient(), switcher);
		new WSMethodInflector(extensionItemWSResource, "", DELETE, JSON, createClient(), switcher);
		new WSMethodInflector(extensionItemWSResource, "properties", GET, JSON, createClient(), switcher);
		new WSMethodInflector(extensionItemWSResource, "properties", POST, JSON, createClient(), switcher);
		new WSMethodInflector(extensionItemWSResource, "properties", DELETE, JSON, createClient(), switcher);

		registerResources(wsResource.build());
		registerResources(extensionItemsWSResource.build());
		registerResources(extensionItemWSResource.build());
	}

}
