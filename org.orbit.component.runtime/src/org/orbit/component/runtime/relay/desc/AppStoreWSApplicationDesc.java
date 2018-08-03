package org.orbit.component.runtime.relay.desc;

import javax.ws.rs.core.MediaType;

import org.origin.common.rest.server.WSApplicationDesc;
import org.origin.common.rest.server.WSMethodDesc;
import org.origin.common.rest.server.WSResourceDesc;

public class AppStoreWSApplicationDesc extends WSApplicationDesc {

	/**
	 * 
	 * @param contextRoot
	 */
	public AppStoreWSApplicationDesc(String contextRoot) {
		super(contextRoot);

		WSResourceDesc rootWSResource = new WSResourceDesc(this, "/");
		new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "echo");

		WSResourceDesc appsWSResource = new WSResourceDesc(this, "/apps");
		new WSMethodDesc(appsWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "");
		new WSMethodDesc(appsWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "query");
		new WSMethodDesc(appsWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "");
		new WSMethodDesc(appsWSResource, WSMethodDesc.PUT, WSMethodDesc.JSON, "");
		new WSMethodDesc(appsWSResource, WSMethodDesc.DELETE, WSMethodDesc.JSON, "");

		WSResourceDesc appWSResource = new WSResourceDesc(this, "/app");
		new WSMethodDesc(appWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "");
		new WSMethodDesc(appWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "/exists");
		new WSMethodDesc(appWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "/content");
		new WSMethodDesc(appWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "/content", MediaType.MULTIPART_FORM_DATA_TYPE);
		// new WSMethodDesc(appWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "{appId}/{appVersion}/content");
	}

}

// new WSMethodDesc(appsWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "{appId}/{appVersion}");
// new WSMethodDesc(appsWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "{appId}/{appVersion}/exists");
// new WSMethodDesc(appsWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "{appId}/{appVersion}/content");
// new WSMethodDesc(appsWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "{appId}/{appVersion}/content");
// new WSMethodDesc(appsWSResource, WSMethodDesc.DELETE, WSMethodDesc.JSON, "{appId}/{appVersion}");
