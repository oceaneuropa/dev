package org.orbit.component.runtime.tier3.domain.ws.other;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.runtime.OrbitServices;
import org.orbit.component.runtime.tier3.domain.service.DomainService;

public class DomainMgmtServiceResolver implements ContextResolver<DomainService> {

	@Override
	public DomainService getContext(Class<?> clazz) {
		return OrbitServices.getInstance().getDomainService();
	}

}
