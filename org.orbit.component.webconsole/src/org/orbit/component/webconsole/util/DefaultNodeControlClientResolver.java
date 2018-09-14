package org.orbit.component.webconsole.util;

import java.io.IOException;

import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClientResolver;

public class DefaultNodeControlClientResolver implements NodeControlClientResolver {

	protected String indexServiceUrl;

	public DefaultNodeControlClientResolver(String indexServiceUrl) {
		this.indexServiceUrl = indexServiceUrl;
	}

	@Override
	public NodeControlClient resolve(String accessToken, String platformId) throws IOException {
		NodeControlClient nodeControlClient = OrbitClientHelper.INSTANCE.getNodeControlClient(this.indexServiceUrl, accessToken, platformId);
		return nodeControlClient;
	}

}
