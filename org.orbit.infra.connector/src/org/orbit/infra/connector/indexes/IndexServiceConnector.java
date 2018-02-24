package org.orbit.infra.connector.indexes;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexService;
import org.origin.common.rest.client.ServiceConnector;

public class IndexServiceConnector extends ServiceConnector<IndexService> {

	public IndexServiceConnector() {
		super(IndexService.class);
	}

	@Override
	protected IndexService create(Map<String, Object> properties) {
		return new IndexServiceImpl(properties);
	}

}