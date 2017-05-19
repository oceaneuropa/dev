package org.orbit.component.connector.tier3.domain;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier3.domain.DomainMgmt;
import org.orbit.component.api.tier3.domain.DomainMgmtConnector;
import org.orbit.component.connector.OrbitConstants;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexService;
import org.origin.mgm.client.connector.ServiceConnectorImpl;

public class DomainMgmtConnectorImpl extends ServiceConnectorImpl<DomainMgmt> implements DomainMgmtConnector {

	/**
	 * 
	 * @param indexService
	 */
	public DomainMgmtConnectorImpl(IndexService indexService) {
		super(indexService, DomainMgmtConnector.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, OrbitConstants.DOMAIN_MANAGEMENT_TYPE);
	}

	@Override
	protected DomainMgmt createService(Map<String, Object> properties) {
		return new DomainMgmtImpl(properties);
	}

	@Override
	protected void updateService(DomainMgmt domainManagement, Map<String, Object> properties) {
		domainManagement.update(properties);
	}

}
