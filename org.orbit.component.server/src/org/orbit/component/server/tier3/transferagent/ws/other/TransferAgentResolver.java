package org.orbit.component.server.tier3.transferagent.ws.other;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.server.Activator;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;

public class TransferAgentResolver implements ContextResolver<TransferAgentService> {

	@Override
	public TransferAgentService getContext(Class<?> clazz) {
		return Activator.getTransferAgentService();
	}

}
