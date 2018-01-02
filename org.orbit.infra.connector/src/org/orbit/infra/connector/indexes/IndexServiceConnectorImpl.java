package org.orbit.infra.connector.indexes;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexService;
import org.origin.common.rest.client.ServiceConnector;

public class IndexServiceConnectorImpl extends ServiceConnector<IndexService> {

	public IndexServiceConnectorImpl() {
		super(IndexService.class);
	}

	@Override
	protected IndexService create(Map<String, Object> properties) {
		return new IndexServiceImpl(properties);
	}

}
