package org.orbit.component.server.tier1.auth.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier1.auth.service.AuthService;
import org.origin.common.thread.ServiceIndexTimerImplV2;
import org.origin.common.thread.ServiceIndexTimerV2;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

public class AuthServiceIndexTimer extends ServiceIndexTimerImplV2<IndexProvider, AuthService, IndexItem> implements ServiceIndexTimerV2<IndexProvider, AuthService, IndexItem> {

	protected AuthService service;

	/**
	 * 
	 * @param service
	 * @param indexProvider
	 */
	public AuthServiceIndexTimer(AuthService service, IndexProvider indexProvider) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public AuthService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, AuthService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OrbitConstants.AUTH_INDEXER_ID, OrbitConstants.AUTH_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, AuthService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.AUTH_NAME, name);
		props.put(OrbitConstants.AUTH_HOST_URL, hostURL);
		props.put(OrbitConstants.AUTH_CONTEXT_ROOT, contextRoot);
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(OrbitConstants.AUTH_INDEXER_ID, OrbitConstants.AUTH_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, AuthService service, IndexItem indexItem) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.AUTH_NAME, name);
		props.put(OrbitConstants.AUTH_HOST_URL, hostURL);
		props.put(OrbitConstants.AUTH_CONTEXT_ROOT, contextRoot);
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(OrbitConstants.AUTH_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.AUTH_INDEXER_ID, indexItemId);
	}

}
