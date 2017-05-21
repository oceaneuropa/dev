package org.orbit.component.server.tier3.domain.ws;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.server.Activator;
import org.orbit.component.server.tier3.domain.service.DomainMgmtService;

public class DomainMgmtServiceResolver implements ContextResolver<DomainMgmtService> {

	@Override
	public DomainMgmtService getContext(Class<?> clazz) {
		return Activator.getDomainMgmtService();
	}

}
