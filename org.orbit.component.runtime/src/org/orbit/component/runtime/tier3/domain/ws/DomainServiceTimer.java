package org.orbit.component.runtime.tier3.domain.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentsConstants;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.origin.common.util.DateUtil;

public class DomainServiceTimer extends ServiceIndexTimer<DomainManagementService> {

	protected DomainManagementService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public DomainServiceTimer(IndexProvider indexProvider, DomainManagementService service) {
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

		return indexProvider.getIndexItem(ComponentsConstants.DOMAIN_SERVICE_INDEXER_ID, ComponentsConstants.DOMAIN_SERVICE_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, DomainManagementService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(ComponentsConstants.DOMAIN_SERVICE_NAME, name);
		props.put(ComponentsConstants.DOMAIN_SERVICE_HOST_URL, hostURL);
		props.put(ComponentsConstants.DOMAIN_SERVICE_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(ComponentsConstants.LAST_HEARTBEAT_TIME, now);
		props.put(ComponentsConstants.HEARTBEAT_EXPIRE_TIME, expire);

		return indexProvider.addIndexItem(ComponentsConstants.DOMAIN_SERVICE_INDEXER_ID, ComponentsConstants.DOMAIN_SERVICE_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, DomainManagementService service, IndexItem indexItem) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Integer indexItemId = indexItem.getIndexItemId();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(ComponentsConstants.DOMAIN_SERVICE_NAME, name);
		props.put(ComponentsConstants.DOMAIN_SERVICE_HOST_URL, hostURL);
		props.put(ComponentsConstants.DOMAIN_SERVICE_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(ComponentsConstants.LAST_HEARTBEAT_TIME, now);
		props.put(ComponentsConstants.HEARTBEAT_EXPIRE_TIME, expire);

		indexProvider.setProperties(ComponentsConstants.DOMAIN_SERVICE_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(ComponentsConstants.DOMAIN_SERVICE_INDEXER_ID, indexItemId);
	}

}
