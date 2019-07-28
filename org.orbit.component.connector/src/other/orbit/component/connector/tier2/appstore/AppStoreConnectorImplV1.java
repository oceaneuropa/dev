package other.orbit.component.connector.tier2.appstore;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier2.appstore.AppStoreClient;
import org.orbit.component.connector.tier2.appstore.AppStoreClientImpl;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;

import other.orbit.component.api.tier2.appstore.AppStoreConnectorV1;
import other.orbit.component.connector.ComponentConstantsV1;
import other.orbit.component.connector.IndexBasedLoadBalancedServiceConnectorImpl;

public class AppStoreConnectorImplV1 extends IndexBasedLoadBalancedServiceConnectorImpl<AppStoreClient> implements AppStoreConnectorV1 {

	/**
	 * 
	 * @param indexService
	 */
	public AppStoreConnectorImplV1(IndexServiceClient indexService) {
		super(indexService, AppStoreConnectorV1.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexServiceClient indexService) throws IOException {
		return indexService.getIndexItems(ComponentConstantsV1.APP_STORE_INDEXER_ID, ComponentConstantsV1.APP_STORE_TYPE);
	}

	@Override
	protected AppStoreClient createService(Map<String, Object> properties) {
		return new AppStoreClientImpl(null, properties);
	}

	@Override
	protected void updateService(AppStoreClient appStore, Map<String, Object> properties) {
		appStore.update(properties);
	}

	@Override
	protected void removeService(AppStoreClient appStore) {
		// index items for appStore gets removed
		// the client AppStore is removed
	}

}
