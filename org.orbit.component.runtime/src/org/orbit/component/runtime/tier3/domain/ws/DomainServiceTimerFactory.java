package org.orbit.component.runtime.tier3.domain.ws;

import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;

public class DomainServiceTimerFactory implements ServiceIndexTimerFactory<DomainManagementService> {

	@Override
	public DomainServiceTimer create(DomainManagementService service) {
		return new DomainServiceTimer(service);
	}

}
