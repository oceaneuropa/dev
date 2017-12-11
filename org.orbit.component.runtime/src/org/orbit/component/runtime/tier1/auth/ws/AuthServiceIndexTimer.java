package org.orbit.component.runtime.tier1.auth.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.OrbitConstants;
import org.orbit.component.runtime.tier1.auth.service.AuthService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.util.DateUtil;

public class AuthServiceIndexTimer extends ServiceIndexTimerImpl<IndexProvider, AuthService, IndexItem> implements ServiceIndexTimer<IndexProvider, AuthService, IndexItem> {

	protected AuthService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public AuthServiceIndexTimer(IndexProvider indexProvider, AuthService service) {
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
		String namespace = service.getNamespace();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.AUTH_NAMESPACE, namespace);
		props.put(OrbitConstants.AUTH_NAME, name);
		props.put(OrbitConstants.AUTH_HOST_URL, hostURL);
		props.put(OrbitConstants.AUTH_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		return indexProvider.addIndexItem(OrbitConstants.AUTH_INDEXER_ID, OrbitConstants.AUTH_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, AuthService service, IndexItem indexItem) throws IOException {
		String namespace = service.getNamespace();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Integer indexItemId = indexItem.getIndexItemId();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.AUTH_NAMESPACE, namespace);
		props.put(OrbitConstants.AUTH_NAME, name);
		props.put(OrbitConstants.AUTH_HOST_URL, hostURL);
		props.put(OrbitConstants.AUTH_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		indexProvider.setProperties(OrbitConstants.AUTH_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.AUTH_INDEXER_ID, indexItemId);
	}

}
