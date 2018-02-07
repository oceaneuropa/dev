package org.orbit.component.connector.tier3.domain.other;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier3.domain.DomainServiceClient;
import org.orbit.component.api.tier3.domain.other.DomainServiceConnector;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.connector.tier3.domain.DomainServiceClientImpl;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.api.indexes.IndexBasedLoadBalancedServiceConnectorImpl;

public class DomainServiceConnectorImplV1 extends IndexBasedLoadBalancedServiceConnectorImpl<DomainServiceClient> implements DomainServiceConnector {

	/**
	 * 
	 * @param indexService
	 */
	public DomainServiceConnectorImplV1(IndexService indexService) {
		super(indexService, DomainServiceConnector.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.DOMAIN_SERVICE_INDEXER_ID, OrbitConstants.DOMAIN_SERVICE_TYPE);
	}

	@Override
	protected DomainServiceClient createService(Map<String, Object> properties) {
		return new DomainServiceClientImpl(null, properties);
	}

	@Override
	protected void updateService(DomainServiceClient domainService, Map<String, Object> properties) {
		// System.out.println(getClass().getSimpleName() + ".updateService()");
		domainService.update(properties);

		// System.out.println("domainManagement is: " + domainManagement.getName() + " (" + domainManagement.getURL() + ")");
		// System.out.println("New properties are:");
		// Printer.pl(properties);
	}

}
