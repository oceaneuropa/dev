package org.orbit.component.server.tier3.domain.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.origin.common.thread.ServiceIndexTimerImplV2;
import org.origin.common.thread.ServiceIndexTimerV2;
import org.origin.common.util.DateUtil;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

public class DomainMgmtServiceTimer extends ServiceIndexTimerImplV2<IndexProvider, DomainManagementService, IndexItem> implements ServiceIndexTimerV2<IndexProvider, DomainManagementService, IndexItem> {

	protected DomainManagementService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public DomainMgmtServiceTimer(IndexProvider indexProvider, DomainManagementService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public synchronized DomainManagementService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, DomainManagementService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, OrbitConstants.DOMAIN_MANAGEMENT_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, DomainManagementService service) throws IOException {
		String namespace = service.getNamespace();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.DOMAIN_MANAGEMENT_NAMESPACE, namespace);
		props.put(OrbitConstants.DOMAIN_MANAGEMENT_NAME, name);
		props.put(OrbitConstants.DOMAIN_MANAGEMENT_HOST_URL, hostURL);
		props.put(OrbitConstants.DOMAIN_MANAGEMENT_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		return indexProvider.addIndexItem(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, OrbitConstants.DOMAIN_MANAGEMENT_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, DomainManagementService service, IndexItem indexItem) throws IOException {
		String namespace = service.getNamespace();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Integer indexItemId = indexItem.getIndexItemId();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.DOMAIN_MANAGEMENT_NAMESPACE, namespace);
		props.put(OrbitConstants.DOMAIN_MANAGEMENT_NAME, name);
		props.put(OrbitConstants.DOMAIN_MANAGEMENT_HOST_URL, hostURL);
		props.put(OrbitConstants.DOMAIN_MANAGEMENT_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		indexProvider.setProperties(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, indexItemId);
	}

}
