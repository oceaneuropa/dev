package org.orbit.infra.connector.indexes;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class IndexServiceConnector extends ServiceConnector<IndexServiceClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.infra.connector.IndexServiceConnector";

	public IndexServiceConnector() {
		super(IndexServiceClient.class);
	}

	@Override
	protected IndexServiceClient create(Map<String, Object> properties) {
		return new IndexServiceClientImpl(this, properties);
	}

}
