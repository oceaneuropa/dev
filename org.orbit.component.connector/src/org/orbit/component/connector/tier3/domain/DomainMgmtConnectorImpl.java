package org.orbit.component.connector.tier3.domain;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier3.domain.DomainManagement;
import org.orbit.component.api.tier3.domain.DomainManagementConnector;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.api.indexes.IndexBasedLoadBalancedServiceConnectorImpl;

public class DomainMgmtConnectorImpl extends IndexBasedLoadBalancedServiceConnectorImpl<DomainManagement> implements DomainManagementConnector {

	/**
	 * 
	 * @param indexService
	 */
	public DomainMgmtConnectorImpl(IndexService indexService) {
		super(indexService, DomainManagementConnector.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, OrbitConstants.DOMAIN_MANAGEMENT_TYPE);
	}

	@Override
	protected DomainManagement createService(Map<String, Object> properties) {
		return new DomainManagementImpl(properties);
	}

	@Override
	protected void updateService(DomainManagement domainManagement, Map<String, Object> properties) {
		// System.out.println(getClass().getSimpleName() + ".updateService()");
		domainManagement.update(properties);

		// System.out.println("domainManagement is: " + domainManagement.getName() + " (" + domainManagement.getURL() + ")");
		// System.out.println("New properties are:");
		// Printer.pl(properties);
	}

}
