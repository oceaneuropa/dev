package org.orbit.infra.connector.indexes;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class IndexProviderConnector extends ServiceConnector<IndexProvider> implements ConnectorActivator {

	public static final String ID = "org.orbit.infra.connector.IndexProviderConnector";

	public IndexProviderConnector() {
		super(IndexProvider.class);
	}

	@Override
	public IndexProvider create(Map<String, Object> properties) {
		return new IndexProviderImpl(properties);
	}

}
