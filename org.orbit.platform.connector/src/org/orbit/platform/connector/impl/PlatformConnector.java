package org.orbit.platform.connector.impl;

import java.util.Map;

import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.sdk.connector.IConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class PlatformConnector extends ServiceConnector<PlatformClient> implements IConnectorActivator {

	public static final String ID = "org.orbit.platform.connector.PlatformConnector";

	public PlatformConnector() {
		super(PlatformClient.class);
	}

	@Override
	protected PlatformClient create(Map<String, Object> properties) {
		return new PlatformClientImpl(this, properties);
	}

}
