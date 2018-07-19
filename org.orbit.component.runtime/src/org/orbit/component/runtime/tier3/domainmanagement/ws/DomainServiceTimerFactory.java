package org.orbit.component.runtime.tier3.domainmanagement.ws;

import org.orbit.component.runtime.tier3.domainmanagement.service.DomainManagementService;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;

public class DomainServiceTimerFactory implements ServiceIndexTimerFactory<DomainManagementService> {

	@Override
	public DomainServiceTimer create(IndexProvider indexProvider, DomainManagementService service) {
		return new DomainServiceTimer(indexProvider, service);
	}

}