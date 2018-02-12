package org.orbit.platform.connector.platform;

import java.util.Map;

import org.orbit.platform.api.platform.PlatformClient;
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
