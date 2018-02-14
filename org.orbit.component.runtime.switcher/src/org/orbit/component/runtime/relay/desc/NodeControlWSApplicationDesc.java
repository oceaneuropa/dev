package org.orbit.component.runtime.relay.desc;

import org.origin.common.rest.server.WSApplicationDesc;
import org.origin.common.rest.server.WSMethodDesc;
import org.origin.common.rest.server.WSResourceDesc;

public class NodeControlWSApplicationDesc extends WSApplicationDesc {

	/**
	 * 
	 * @param contextRoot
	 */
	public NodeControlWSApplicationDesc(String contextRoot) {
		super(contextRoot);

		WSResourceDesc rootWSResource = new WSResourceDesc(this, "/");
		new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "echo");
		new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "level/{level1}/{level2}");
		new WSMethodDesc(rootWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "request");
	}

}
