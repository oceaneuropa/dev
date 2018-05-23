package org.orbit.component.connector.tier3.nodecontrol;

import java.util.Map;

import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.platform.sdk.connector.IConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class NodeControlConnector extends ServiceConnector<NodeControlClient> implements IConnectorActivator {

	public static final String ID = "org.orbit.component.connector.NodeControlConnector";

	public NodeControlConnector() {
		super(NodeControlClient.class);
	}

	@Override
	protected NodeControlClient create(Map<String, Object> properties) {
		return new NodeControlClientImpl(this, properties);
	}

}
