package org.orbit.component.connector.tier2.appstore;

import java.util.Map;

import org.orbit.component.api.tier2.appstore.AppStore;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class AppStoreConnector extends ServiceConnector<AppStore> implements ConnectorActivator {

	public static final String ID = "org.orbit.component.connector.AppStoreConnector";

	public AppStoreConnector() {
		super(AppStore.class);
	}

	@Override
	protected AppStore create(Map<String, Object> properties) {
		return new AppStoreImpl(this, properties);
	}

}
