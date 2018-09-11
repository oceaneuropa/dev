package org.orbit.infra.connector.indexes;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexProviderClient;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class IndexProviderConnector extends ServiceConnector<IndexProviderClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.infra.connector.IndexProviderConnector";

	public IndexProviderConnector() {
		super(IndexProviderClient.class);
	}

	@Override
	public IndexProviderClient create(Map<String, Object> properties) {
		// return new IndexProviderClientImpl(this, properties);
		return null;
	}

}
