package org.orbit.infra.connector.datatube;

import java.util.Map;

import org.orbit.infra.api.datatube.DataTubeClient;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class DataTubeConnector extends ServiceConnector<DataTubeClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.infra.connector.DataTubeConnector";

	public DataTubeConnector() {
		super(DataTubeClient.class);
	}

	@Override
	protected DataTubeClient create(Map<String, Object> properties) {
		return new DataTubeClientImpl(this, properties);
	}

}
