package org.orbit.component.connector.tier3.nodecontrol;

import java.util.Map;

import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.origin.common.rest.client.ServiceConnector;

public class NodeControlConnector extends ServiceConnector<NodeControlClient> {

	public NodeControlConnector() {
		super(NodeControlClient.class);
	}

	@Override
	protected NodeControlClient create(Map<String, Object> properties) {
		return new NodeControlClientImpl(this, properties);
	}

}
