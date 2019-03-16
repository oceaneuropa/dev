package org.orbit.component.webconsole.util;

import java.io.IOException;

import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClientResolver;

public class DefaultNodeControlClientResolver implements NodeControlClientResolver {

	@Override
	public NodeControlClient resolve(String accessToken, String platformId) throws IOException {
		NodeControlClient nodeControlClient = OrbitClientHelper.INSTANCE.getNodeControlClient(accessToken, platformId);
		return nodeControlClient;
	}

}
