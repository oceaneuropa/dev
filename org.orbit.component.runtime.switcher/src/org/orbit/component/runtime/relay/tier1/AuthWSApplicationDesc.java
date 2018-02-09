package org.orbit.component.runtime.relay.tier1;

import org.origin.common.rest.server.WSApplicationDesc;
import org.origin.common.rest.server.WSMethodDesc;
import org.origin.common.rest.server.WSResourceDesc;

public class AuthWSApplicationDesc extends WSApplicationDesc {

	/**
	 * 
	 * @param contextRoot
	 */
	public AuthWSApplicationDesc(String contextRoot) {
		super(contextRoot);

		WSResourceDesc rootWSResource = new WSResourceDesc(this, "/");
		new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "echo");
		new WSMethodDesc(rootWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "authorize");
		new WSMethodDesc(rootWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "token");
	}

}
