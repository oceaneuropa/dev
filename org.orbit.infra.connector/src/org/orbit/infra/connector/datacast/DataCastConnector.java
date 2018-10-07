package org.orbit.infra.connector.datacast;

import java.util.Map;

import org.orbit.infra.api.datacast.DataCastClient;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class DataCastConnector extends ServiceConnector<DataCastClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.infra.connector.DataCastConnector";

	public DataCastConnector() {
		super(DataCastClient.class);
	}

	@Override
	protected DataCastClient create(Map<String, Object> properties) {
		return new DataCastClientImpl(this, properties);
	}

}
