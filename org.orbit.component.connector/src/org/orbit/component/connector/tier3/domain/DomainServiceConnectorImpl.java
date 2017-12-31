package org.orbit.component.connector.tier3.domain;

import java.util.Map;

import org.orbit.component.api.tier3.domain.DomainService;
import org.origin.common.rest.client.ServiceConnector;

public class DomainServiceConnectorImpl extends ServiceConnector<DomainService> {

	public DomainServiceConnectorImpl() {
		super(DomainService.class);
	}

	@Override
	protected DomainService create(Map<String, Object> properties) {
		return new DomainServiceImpl(this, properties);
	}

}
