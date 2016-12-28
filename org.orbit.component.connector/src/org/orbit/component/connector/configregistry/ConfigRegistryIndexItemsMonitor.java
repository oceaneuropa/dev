package org.orbit.component.connector.configregistry;

import java.io.IOException;
import java.util.List;

import org.orbit.component.connector.OrbitConstants;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexItemsMonitor;
import org.origin.mgm.client.api.IndexService;

public class ConfigRegistryIndexItemsMonitor extends IndexItemsMonitor {

	public ConfigRegistryIndexItemsMonitor() {
		super("ConfigRegistry Index Items Monitor");
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, OrbitConstants.CONFIG_REGISTRY_TYPE);
	}

	@Override
	protected void indexItemsUpdated(List<IndexItem> indexItems) {

	}

}
