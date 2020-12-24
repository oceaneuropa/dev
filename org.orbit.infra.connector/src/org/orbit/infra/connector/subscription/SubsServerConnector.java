package org.orbit.infra.connector.subscription;

import java.util.Map;

import org.orbit.infra.api.subscription.SubsServerAPI;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class SubsServerConnector extends ServiceConnector<SubsServerAPI> implements ConnectorActivator {

	public static final String ID = "org.orbit.infra.connector.SubsServerConnector";

	public SubsServerConnector() {
		super(SubsServerAPI.class);
	}

	@Override
	protected SubsServerAPI create(Map<String, Object> properties) {
		return new SubsServerAPIImpl(this, properties);
	}

}
