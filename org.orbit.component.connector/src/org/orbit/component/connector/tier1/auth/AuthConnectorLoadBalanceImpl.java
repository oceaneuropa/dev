package org.orbit.component.connector.tier1.auth;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.auth.Auth;
import org.orbit.component.api.tier1.auth.AuthConnector;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.api.indexes.IndexBasedLoadBalancedServiceConnectorImpl;

public class AuthConnectorLoadBalanceImpl extends IndexBasedLoadBalancedServiceConnectorImpl<Auth> implements AuthConnector {

	/**
	 * 
	 * @param indexService
	 */
	public AuthConnectorLoadBalanceImpl(IndexService indexService) {
		super(indexService, AuthConnector.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.AUTH_INDEXER_ID, OrbitConstants.AUTH_TYPE);
	}

	@Override
	protected Auth createService(Map<String, Object> properties) {
		return new AuthImpl(properties);
	}

	@Override
	protected void updateService(Auth service, Map<String, Object> properties) {
		service.update(properties);
	}

}
