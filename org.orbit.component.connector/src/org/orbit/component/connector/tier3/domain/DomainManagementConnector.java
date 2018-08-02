package org.orbit.component.connector.tier3.domain;

import java.util.Map;

import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class DomainManagementConnector extends ServiceConnector<DomainManagementClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.component.connector.DomainManagementConnector";

	public DomainManagementConnector() {
		super(DomainManagementClient.class);
	}

	@Override
	protected DomainManagementClient create(Map<String, Object> properties) {
		return new DomainManagementClientImpl(this, properties);
	}

}
