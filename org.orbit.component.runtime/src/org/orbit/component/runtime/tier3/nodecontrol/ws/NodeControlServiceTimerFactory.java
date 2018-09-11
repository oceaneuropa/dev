package org.orbit.component.runtime.tier3.nodecontrol.ws;

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;

public class NodeControlServiceTimerFactory implements ServiceIndexTimerFactory<NodeControlService> {

	@Override
	public NodeControlServiceTimer create(IndexServiceClient indexProvider, NodeControlService service) {
		return new NodeControlServiceTimer(indexProvider, service);
	}

}
