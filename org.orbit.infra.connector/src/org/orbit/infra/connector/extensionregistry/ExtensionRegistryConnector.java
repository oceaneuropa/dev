package org.orbit.infra.connector.extensionregistry;

import java.util.Map;

import org.orbit.infra.api.extensionregistry.ExtensionRegistryClient;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class ExtensionRegistryConnector extends ServiceConnector<ExtensionRegistryClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.infra.connector.ExtensionRegistryConnector";

	public ExtensionRegistryConnector() {
		super(ExtensionRegistryClient.class);
	}

	@Override
	public ExtensionRegistryClient create(Map<String, Object> properties) {
		return new ExtensionRegistryClientImpl(properties);
	}

}
