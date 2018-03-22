package org.orbit.component.runtime.tier3.nodemanagement.ws.other;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.runtime.OrbitServices;
import org.orbit.component.runtime.tier3.nodemanagement.service.NodeManagementService;

public class TransferAgentResolver implements ContextResolver<NodeManagementService> {

	@Override
	public NodeManagementService getContext(Class<?> clazz) {
		return OrbitServices.getInstance().getTransferAgentService();
	}

}
