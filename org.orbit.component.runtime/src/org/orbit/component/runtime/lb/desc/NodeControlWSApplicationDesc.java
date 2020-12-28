package org.orbit.component.runtime.lb.desc;

import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSApplicationDesc;
import org.origin.common.rest.server.WSMethodDesc;
import org.origin.common.rest.server.WSResourceDesc;
import org.origin.common.service.IWebService;

public class NodeControlWSApplicationDesc extends WSApplicationDesc {

	/**
	 * 
	 * @param webService
	 */
	public NodeControlWSApplicationDesc(IWebService webService) {
		super(webService);
		setRemoteFeature(FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.ECHO);

		WSResourceDesc rootWSResource = new WSResourceDesc(this, "/");
		// new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "echo");
		// new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "name");

		new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "level/{level1}/{level2}");
		new WSMethodDesc(rootWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "request");
	}

}
