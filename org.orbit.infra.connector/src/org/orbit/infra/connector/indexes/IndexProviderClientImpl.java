package org.orbit.infra.connector.indexes;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexProviderClient;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.origin.common.rest.client.ServiceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexProviderClientImpl extends IndexServiceClientImpl implements IndexProviderClient {

	protected static Logger LOG = LoggerFactory.getLogger(IndexServiceClientImpl.class);

	/**
	 * 
	 * @param properties
	 */
	public IndexProviderClientImpl(ServiceConnector<IndexServiceClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

}
