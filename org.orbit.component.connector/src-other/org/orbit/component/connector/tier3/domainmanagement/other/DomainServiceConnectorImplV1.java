package org.orbit.component.connector.tier3.domainmanagement.other;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier3.domainmanagement.DomainManagementClient;
import org.orbit.component.api.tier3.domainmanagement.other.DomainServiceConnector;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.connector.tier3.domainmanagement.DomainManagementClientImpl;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.api.indexes.IndexBasedLoadBalancedServiceConnectorImpl;

public class DomainServiceConnectorImplV1 extends IndexBasedLoadBalancedServiceConnectorImpl<DomainManagementClient> implements DomainServiceConnector {

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
	protected DomainManagementClient createService(Map<String, Object> properties) {
		return new DomainManagementClientImpl(null, properties);
	}

	@Override
	protected void updateService(DomainManagementClient domainService, Map<String, Object> properties) {
		// System.out.println(getClass().getSimpleName() + ".updateService()");
		domainService.update(properties);

		// System.out.println("domainManagement is: " + domainManagement.getName() + " (" + domainManagement.getURL() + ")");
		// System.out.println("New properties are:");
		// Printer.pl(properties);
	}

}
