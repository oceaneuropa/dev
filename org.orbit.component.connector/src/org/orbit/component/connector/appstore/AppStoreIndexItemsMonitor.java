package org.orbit.component.connector.appstore;

import java.io.IOException;
import java.util.List;

import org.orbit.component.connector.OrbitConstants;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexItemsMonitor;
import org.origin.mgm.client.api.IndexService;

public class AppStoreIndexItemsMonitor extends IndexItemsMonitor {

	public AppStoreIndexItemsMonitor() {
		super("AppStore Index Items Monitor");
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.APP_STORE_INDEXER_ID, OrbitConstants.APP_STORE_TYPE);
	}

	@Override
	protected void indexItemsUpdated(List<IndexItem> indexItems) {

	}

}
