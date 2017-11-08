package org.orbit.component.connector.tier1.auth;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.auth.Auth;
import org.orbit.component.api.tier1.auth.AuthConnector;
import org.orbit.component.connector.OrbitConstants;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexService;
import org.origin.mgm.client.connector.ServiceConnectorImpl;

public class AuthConnectorImpl extends ServiceConnectorImpl<Auth> implements AuthConnector {

	/**
	 * 
	 * @param indexService
	 */
	public AuthConnectorImpl(IndexService indexService) {
		super(indexService, AuthConnector.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.AUTH_INDEXER_ID, OrbitConstants.AUTH_TYPE);
	}

	@Override
	protected Auth createService(Map<String, Object> properties) {
		return new AuthWSImpl(properties);
	}

	@Override
	protected void updateService(Auth service, Map<String, Object> properties) {
		service.update(properties);
	}

}
