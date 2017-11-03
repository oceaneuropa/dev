package org.orbit.component.server.tier3.domain.timer;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.Activator;
import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.origin.common.thread.ServiceIndexTimerImplV2;
import org.origin.common.thread.ServiceIndexTimerV2;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

public class DomainMgmtServiceTimerV2 extends ServiceIndexTimerImplV2<IndexProvider, DomainManagementService, IndexItem> implements ServiceIndexTimerV2<IndexProvider, DomainManagementService, IndexItem> {

	/**
	 * 
	 * @param service
	 * @param indexProvider
	 */
	public DomainMgmtServiceTimerV2(DomainManagementService service, IndexProvider indexProvider) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		setDebug(true);
	}

	@Override
	public synchronized DomainManagementService getService() {
		return Activator.getDomainMgmtService();
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, DomainManagementService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, OrbitConstants.DOMAIN_MANAGEMENT_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, DomainManagementService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.DOMAIN_MANAGEMENT_NAME, name);
		props.put(OrbitConstants.DOMAIN_MANAGEMENT_HOST_URL, hostURL);
		props.put(OrbitConstants.DOMAIN_MANAGEMENT_CONTEXT_ROOT, contextRoot);
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, OrbitConstants.DOMAIN_MANAGEMENT_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, DomainManagementService service, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, indexItemId);
	}

}
