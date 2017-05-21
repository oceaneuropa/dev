package org.orbit.component.server.tier3.domain.service;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.Activator;
import org.orbit.component.server.OrbitConstants;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

public class DomainMgmtServiceTimer extends ServiceIndexTimerImpl<IndexProvider, DomainMgmtService> implements ServiceIndexTimer<IndexProvider, DomainMgmtService> {

	public DomainMgmtServiceTimer(IndexProvider indexProvider) {
		super("Domain Management Service Index Timer", indexProvider);
	}

	@Override
	public DomainMgmtService getService() {
		return Activator.getDomainMgmtService();
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, DomainMgmtService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		IndexItem indexItem = indexProvider.getIndexItem(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, OrbitConstants.DOMAIN_MANAGEMENT_TYPE, name);
		if (indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.DOMAIN_MANAGEMENT_HOST_URL, hostURL);
			props.put(OrbitConstants.DOMAIN_MANAGEMENT_CONTEXT_ROOT, contextRoot);
			props.put(OrbitConstants.DOMAIN_MANAGEMENT_NAME, name);
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date());

			indexProvider.addIndexItem(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, OrbitConstants.DOMAIN_MANAGEMENT_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date());

			indexProvider.setProperties(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, indexItemId, props);
		}
	}

}
