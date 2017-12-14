package org.orbit.component.connector.tier2.appstore;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier2.appstore.AppStore;
import org.orbit.component.api.tier2.appstore.AppStoreConnector;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.api.indexes.IndexBasedLoadBalancedServiceConnectorImpl;

public class AppStoreConnectorImpl extends IndexBasedLoadBalancedServiceConnectorImpl<AppStore> implements AppStoreConnector {

	/**
	 * 
	 * @param indexService
	 */
	public AppStoreConnectorImpl(IndexService indexService) {
		super(indexService, AppStoreConnector.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.APP_STORE_INDEXER_ID, OrbitConstants.APP_STORE_TYPE);
	}

	@Override
	protected AppStore createService(Map<String, Object> properties) {
		return new AppStoreWSImpl(properties);
	}

	@Override
	protected void updateService(AppStore appStore, Map<String, Object> properties) {
		appStore.update(properties);
	}

	@Override
	protected void removeService(AppStore appStore) {
		// index items for appStore gets removed
		// the client AppStore is removed
	}

}
