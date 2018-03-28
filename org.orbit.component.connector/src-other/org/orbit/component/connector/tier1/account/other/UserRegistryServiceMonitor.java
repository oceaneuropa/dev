package org.orbit.component.connector.tier1.account.other;

import java.io.IOException;
import java.util.List;

import org.orbit.component.connector.OrbitConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.api.indexes.ServiceMonitor;

public class UserRegistryServiceMonitor extends ServiceMonitor {

	public UserRegistryServiceMonitor(IndexService indexService) {
		super(indexService);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.USER_REGISTRY_INDEXER_ID, OrbitConstants.USER_REGISTRY_TYPE);
	}

}
