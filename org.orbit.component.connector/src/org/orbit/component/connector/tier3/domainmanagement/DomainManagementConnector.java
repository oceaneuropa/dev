package org.orbit.component.connector.tier3.domainmanagement;

import java.util.Map;

import org.orbit.component.api.tier3.domainmanagement.DomainManagementClient;
import org.orbit.platform.sdk.connector.IConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class DomainManagementConnector extends ServiceConnector<DomainManagementClient> implements IConnectorActivator {

	public static final String ID = "org.orbit.component.connector.DomainManagementConnector";

	public DomainManagementConnector() {
		super(DomainManagementClient.class);
	}

	@Override
	protected DomainManagementClient create(Map<String, Object> properties) {
		return new DomainManagementClientImpl(this, properties);
	}

}
