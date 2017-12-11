package org.orbit.component.connector.tier1.account;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.account.UserRegistry;
import org.orbit.component.api.tier1.account.UserRegistryConnector;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.api.indexes.ServiceConnectorImpl;

public class UserRegistryConnectorImpl extends ServiceConnectorImpl<UserRegistry> implements UserRegistryConnector {

	/**
	 * 
	 * @param indexService
	 */
	public UserRegistryConnectorImpl(IndexService indexService) {
		super(indexService, UserRegistryConnector.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.USER_REGISTRY_INDEXER_ID, OrbitConstants.USER_REGISTRY_TYPE);
	}

	@Override
	protected UserRegistry createService(Map<String, Object> properties) {
		return new UserRegistryWSImpl(properties);
	}

	@Override
	protected void updateService(UserRegistry userRegistry, Map<String, Object> properties) {
		userRegistry.update(properties);
	}

}
