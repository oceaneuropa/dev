package org.orbit.component.runtime.relay.desc;

import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSApplicationDesc;
import org.origin.common.rest.server.WSMethodDesc;
import org.origin.common.rest.server.WSResourceDesc;
import org.origin.common.service.WebServiceAware;

public class AuthWSApplicationDesc extends WSApplicationDesc {

	/**
	 * 
	 * @param webServiceAware
	 */
	public AuthWSApplicationDesc(WebServiceAware webServiceAware) {
		super(webServiceAware);
		setRemoteFeature(FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.ECHO);

		WSResourceDesc rootWSResource = new WSResourceDesc(this, "/");
		// new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "echo");
		// new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "name");

		new WSMethodDesc(rootWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "authorize");
		new WSMethodDesc(rootWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "token");
	}

}
