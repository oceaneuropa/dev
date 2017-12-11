package org.orbit.component.runtime.tier3.domain.ws.other;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.runtime.Activator;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;

public class DomainMgmtServiceResolver implements ContextResolver<DomainManagementService> {

	@Override
	public DomainManagementService getContext(Class<?> clazz) {
		return Activator.getInstance().getDomainMgmtService();
	}

}
