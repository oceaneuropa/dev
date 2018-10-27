package org.orbit.component.runtime.lb.desc;

import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSApplicationDesc;
import org.origin.common.rest.server.WSMethodDesc;
import org.origin.common.rest.server.WSResourceDesc;
import org.origin.common.service.WebServiceAware;

public class UserRegistryWSApplicationDesc extends WSApplicationDesc {

	/**
	 * 
	 * @param webServiceAware
	 */
	public UserRegistryWSApplicationDesc(WebServiceAware webServiceAware) {
		super(webServiceAware);
		setRemoteFeature(FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.ECHO);

		// WSResourceDesc rootWSResource = new WSResourceDesc(this, "/");
		// new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "echo");
		// new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "name");

		WSResourceDesc userAccountsWSResource = new WSResourceDesc(this, "/useraccounts");
		new WSMethodDesc(userAccountsWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "");
		new WSMethodDesc(userAccountsWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "{accountId}");
		new WSMethodDesc(userAccountsWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "{accountId}/exists");
		new WSMethodDesc(userAccountsWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "");
		new WSMethodDesc(userAccountsWSResource, WSMethodDesc.PUT, WSMethodDesc.JSON, "");
		new WSMethodDesc(userAccountsWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "{accountId}/activated");
		new WSMethodDesc(userAccountsWSResource, WSMethodDesc.PUT, WSMethodDesc.JSON, "action");
		new WSMethodDesc(userAccountsWSResource, WSMethodDesc.DELETE, WSMethodDesc.JSON, "{accountId}");
	}

}