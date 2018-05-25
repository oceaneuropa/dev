package org.orbit.component.connector.tier1.config;

import java.util.Map;

import org.orbit.component.api.tier1.config.ConfigRegistry;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class ConfigRegistryConnector extends ServiceConnector<ConfigRegistry> implements ConnectorActivator {

	public static final String ID = "org.orbit.component.connector.ConfigRegistryConnector";

	public ConfigRegistryConnector() {
		super(ConfigRegistry.class);
	}

	@Override
	protected ConfigRegistry create(Map<String, Object> properties) {
		return new ConfigRegistryImpl(this, properties);
	}

}
