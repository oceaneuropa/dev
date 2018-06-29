package org.orbit.component.connector.tier1.config;

import java.util.Map;

import org.orbit.component.api.tier1.registry.Registry;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class ConfigRegistryConnector extends ServiceConnector<Registry> implements ConnectorActivator {

	public static final String ID = "org.orbit.component.connector.ConfigRegistryConnector";

	public ConfigRegistryConnector() {
		super(Registry.class);
	}

	@Override
	protected Registry create(Map<String, Object> properties) {
		return new ConfigRegistryImpl(this, properties);
	}

}
