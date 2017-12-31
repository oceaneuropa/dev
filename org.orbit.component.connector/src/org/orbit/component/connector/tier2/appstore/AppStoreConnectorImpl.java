package org.orbit.component.connector.tier2.appstore;

import java.util.Map;

import org.orbit.component.api.tier2.appstore.AppStore;
import org.origin.common.rest.client.ServiceConnector;

public class AppStoreConnectorImpl extends ServiceConnector<AppStore> {

	public AppStoreConnectorImpl() {
		super(AppStore.class);
	}

	@Override
	protected AppStore create(Map<String, Object> properties) {
		return new AppStoreImpl(this, properties);
	}

}
