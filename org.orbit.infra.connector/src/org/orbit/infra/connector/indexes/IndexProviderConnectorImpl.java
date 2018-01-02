package org.orbit.infra.connector.indexes;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexProvider;
import org.origin.common.rest.client.ServiceConnector;

public class IndexProviderConnectorImpl extends ServiceConnector<IndexProvider> {

	public IndexProviderConnectorImpl() {
		super(IndexProvider.class);
	}

	@Override
	public IndexProvider create(Map<String, Object> properties) {
		return new IndexProviderImpl(properties);
	}

}
