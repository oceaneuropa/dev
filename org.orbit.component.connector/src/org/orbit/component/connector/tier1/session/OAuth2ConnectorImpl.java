package org.orbit.component.connector.tier1.session;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.session.OAuth2;
import org.orbit.component.api.tier1.session.OAuth2Connector;
import org.orbit.component.connector.OrbitConstants;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexService;
import org.origin.mgm.client.connector.ServiceConnectorImpl;

public class OAuth2ConnectorImpl extends ServiceConnectorImpl<OAuth2> implements OAuth2Connector {

	/**
	 * 
	 * @param indexService
	 */
	public OAuth2ConnectorImpl(IndexService indexService) {
		super(indexService, OAuth2Connector.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.OAUTH2_INDEXER_ID, OrbitConstants.OAUTH2_TYPE);
	}

	@Override
	protected OAuth2 createService(Map<String, Object> properties) {
		return new OAuth2WSImpl(properties);
	}

	@Override
	protected void updateService(OAuth2 oauth2, Map<String, Object> properties) {
		oauth2.update(properties);
	}

}
