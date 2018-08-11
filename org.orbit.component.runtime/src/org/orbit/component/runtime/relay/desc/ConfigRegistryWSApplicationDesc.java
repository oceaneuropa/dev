package org.orbit.component.runtime.relay.desc;

import org.origin.common.rest.server.WSApplicationDesc;
import org.origin.common.rest.server.WSMethodDesc;
import org.origin.common.rest.server.WSResourceDesc;
import org.origin.common.service.WebServiceAware;

public class ConfigRegistryWSApplicationDesc extends WSApplicationDesc {

	/**
	 * 
	 * @param webServiceAware
	 */
	public ConfigRegistryWSApplicationDesc(WebServiceAware webServiceAware) {
		super(webServiceAware);

		WSResourceDesc rootWSResource = new WSResourceDesc(this, "/");
		new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "echo");
		new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "name");
	}

}
