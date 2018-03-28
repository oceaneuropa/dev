package org.orbit.component.connector.tier3.domainmanagement;

import java.util.Map;

import org.orbit.component.api.tier3.domainmanagement.DomainManagementClient;
import org.origin.common.rest.client.ServiceConnector;

public class DomainManagementConnector extends ServiceConnector<DomainManagementClient> {

	public DomainManagementConnector() {
		super(DomainManagementClient.class);
	}

	@Override
	protected DomainManagementClient create(Map<String, Object> properties) {
		return new DomainManagementClientImpl(this, properties);
	}

}
