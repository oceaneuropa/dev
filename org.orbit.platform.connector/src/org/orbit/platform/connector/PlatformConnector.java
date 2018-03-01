package org.orbit.platform.connector;

import java.util.Map;

import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.connector.impl.PlatformClientImpl;
import org.origin.common.rest.client.ServiceConnector;

public class PlatformConnector extends ServiceConnector<PlatformClient> {

	public PlatformConnector() {
		super(PlatformClient.class);
	}

	@Override
	protected PlatformClient create(Map<String, Object> properties) {
		return new PlatformClientImpl(this, properties);
	}

}
