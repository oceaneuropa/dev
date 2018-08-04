package org.orbit.component.connector.tier2.appstore;

import java.util.Map;

import org.orbit.component.api.tier2.appstore.AppStoreClient;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class AppStoreConnector extends ServiceConnector<AppStoreClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.component.appstore.AppStoreConnector";

	public AppStoreConnector() {
		super(AppStoreClient.class);
	}

	@Override
	protected AppStoreClient create(Map<String, Object> properties) {
		return new AppStoreClientImpl(this, properties);
	}

}
