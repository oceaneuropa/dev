package org.orbit.component.runtime.lb.desc;

import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSApplicationDesc;
import org.origin.common.rest.server.WSMethodDesc;
import org.origin.common.rest.server.WSResourceDesc;
import org.origin.common.service.WebServiceAware;

public class IdentityWSApplicationDesc extends WSApplicationDesc {

	/**
	 * 
	 * @param webServiceAware
	 */
	public IdentityWSApplicationDesc(WebServiceAware webServiceAware) {
		super(webServiceAware);
		setRemoteFeature(FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.ECHO);

		WSResourceDesc rootWSResource = new WSResourceDesc(this, "/");
		new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "exists");
		new WSMethodDesc(rootWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "register");
		new WSMethodDesc(rootWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "login");
		new WSMethodDesc(rootWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "refreshToken");
		new WSMethodDesc(rootWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "logout");
	}

}

// new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "echo");
// new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "name");
