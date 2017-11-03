package org.orbit.component.server.tier2.appstore.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier2.appstore.service.AppStoreService;
import org.origin.common.thread.ServiceIndexTimerImplV2;
import org.origin.common.thread.ServiceIndexTimerV2;
import org.origin.common.util.DateUtil;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

/**
 * AppStore service indexing timer.
 *
 */
public class AppStoreServiceIndexTimerV2 extends ServiceIndexTimerImplV2<IndexProvider, AppStoreService, IndexItem> implements ServiceIndexTimerV2<IndexProvider, AppStoreService, IndexItem> {

	protected AppStoreService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public AppStoreServiceIndexTimerV2(IndexProvider indexProvider, AppStoreService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public AppStoreService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, AppStoreService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OrbitConstants.APP_STORE_INDEXER_ID, OrbitConstants.APP_STORE_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, AppStoreService service) throws IOException {
		String namespace = service.getNamespace();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.APPSTORE_NAMESPACE, namespace);
		props.put(OrbitConstants.APPSTORE_NAME, name);
		props.put(OrbitConstants.APPSTORE_HOST_URL, hostURL);
		props.put(OrbitConstants.APPSTORE_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		return indexProvider.addIndexItem(OrbitConstants.APP_STORE_INDEXER_ID, OrbitConstants.APP_STORE_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, AppStoreService service, IndexItem indexItem) throws IOException {
		String namespace = service.getNamespace();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.APPSTORE_NAMESPACE, namespace);
		props.put(OrbitConstants.APPSTORE_NAME, name);
		props.put(OrbitConstants.APPSTORE_HOST_URL, hostURL);
		props.put(OrbitConstants.APPSTORE_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		indexProvider.setProperties(OrbitConstants.APP_STORE_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.APP_STORE_INDEXER_ID, indexItemId);
	}

}
