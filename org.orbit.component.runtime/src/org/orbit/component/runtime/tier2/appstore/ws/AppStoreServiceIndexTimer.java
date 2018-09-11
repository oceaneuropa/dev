package org.orbit.component.runtime.tier2.appstore.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.origin.common.util.DateUtil;

/**
 * AppStore service indexing timer.
 *
 */
public class AppStoreServiceIndexTimer extends ServiceIndexTimer<AppStoreService> {

	protected AppStoreService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public AppStoreServiceIndexTimer(IndexServiceClient indexProvider, AppStoreService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public AppStoreService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider, AppStoreService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(ComponentConstants.APP_STORE_INDEXER_ID, ComponentConstants.APP_STORE_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider, AppStoreService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(ComponentConstants.APPSTORE_NAME, name);
		props.put(ComponentConstants.APPSTORE_HOST_URL, hostURL);
		props.put(ComponentConstants.APPSTORE_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(ComponentConstants.LAST_HEARTBEAT_TIME, now);
		props.put(ComponentConstants.HEARTBEAT_EXPIRE_TIME, expire);

		return indexProvider.addIndexItem(ComponentConstants.APP_STORE_INDEXER_ID, ComponentConstants.APP_STORE_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, AppStoreService service, IndexItem indexItem) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(ComponentConstants.APPSTORE_NAME, name);
		props.put(ComponentConstants.APPSTORE_HOST_URL, hostURL);
		props.put(ComponentConstants.APPSTORE_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(ComponentConstants.LAST_HEARTBEAT_TIME, now);
		props.put(ComponentConstants.HEARTBEAT_EXPIRE_TIME, expire);

		indexProvider.setProperties(ComponentConstants.APP_STORE_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.deleteIndexItem(ComponentConstants.APP_STORE_INDEXER_ID, indexItemId);
	}

}
