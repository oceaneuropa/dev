package org.orbit.component.connector.tier3.domain;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier3.domain.DomainService;
import org.orbit.component.api.tier3.domain.DomainServiceConnector;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.api.indexes.IndexBasedLoadBalancedServiceConnectorImpl;

public class DomainServiceConnectorImpl extends IndexBasedLoadBalancedServiceConnectorImpl<DomainService> implements DomainServiceConnector {

	/**
	 * 
	 * @param indexService
	 */
	public DomainServiceConnectorImpl(IndexService indexService) {
		super(indexService, DomainServiceConnector.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.DOMAIN_SERVICE_INDEXER_ID, OrbitConstants.DOMAIN_SERVICE_TYPE);
	}

	@Override
	protected DomainService createService(Map<String, Object> properties) {
		return new DomainServiceImpl(properties);
	}

	@Override
	protected void updateService(DomainService domainService, Map<String, Object> properties) {
		// System.out.println(getClass().getSimpleName() + ".updateService()");
		domainService.update(properties);

		// System.out.println("domainManagement is: " + domainManagement.getName() + " (" + domainManagement.getURL() + ")");
		// System.out.println("New properties are:");
		// Printer.pl(properties);
	}

}
