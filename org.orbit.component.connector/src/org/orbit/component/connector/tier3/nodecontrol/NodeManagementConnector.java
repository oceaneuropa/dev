package org.orbit.component.connector.tier3.nodecontrol;

import java.util.Map;

import org.orbit.component.api.tier3.nodecontrol.NodeManagementClient;
import org.origin.common.rest.client.ServiceConnector;

public class NodeManagementConnector extends ServiceConnector<NodeManagementClient> {

	public NodeManagementConnector() {
		super(NodeManagementClient.class);
	}

	@Override
	protected NodeManagementClient create(Map<String, Object> properties) {
		return new NodeManagementClientImpl(this, properties);
	}

}
