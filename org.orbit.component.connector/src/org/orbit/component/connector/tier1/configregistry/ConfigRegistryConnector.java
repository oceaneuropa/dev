package org.orbit.component.connector.tier1.configregistry;

import java.util.Map;

import org.orbit.component.api.tier1.configregistry.ConfigRegistryClient;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class ConfigRegistryConnector extends ServiceConnector<ConfigRegistryClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.component.connector.ConfigRegistryConnector";

	public ConfigRegistryConnector() {
		super(ConfigRegistryClient.class);
	}

	@Override
	protected ConfigRegistryClient create(Map<String, Object> properties) {
		return new ConfigRegistryClientImpl(this, properties);
	}

}
