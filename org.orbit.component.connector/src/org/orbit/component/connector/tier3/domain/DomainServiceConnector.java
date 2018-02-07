package org.orbit.component.connector.tier3.domain;

import java.util.Map;

import org.orbit.component.api.tier3.domain.DomainServiceClient;
import org.origin.common.rest.client.ServiceConnector;

public class DomainServiceConnector extends ServiceConnector<DomainServiceClient> {

	public DomainServiceConnector() {
		super(DomainServiceClient.class);
	}

	@Override
	protected DomainServiceClient create(Map<String, Object> properties) {
		return new DomainServiceClientImpl(this, properties);
	}

}
