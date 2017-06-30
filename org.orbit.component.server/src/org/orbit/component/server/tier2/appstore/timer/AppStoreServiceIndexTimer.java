package org.orbit.component.server.tier2.appstore.timer;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.Activator;
import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier2.appstore.service.AppStoreService;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

/**
 * AppStore service timer to update index item for the service.
 *
 */
public class AppStoreServiceIndexTimer extends ServiceIndexTimerImpl<IndexProvider, AppStoreService> implements ServiceIndexTimer<IndexProvider, AppStoreService> {

	/**
	 * 
	 * @param indexProvider
	 */
	public AppStoreServiceIndexTimer(IndexProvider indexProvider) {
		super("AppStore Service Index Timer", indexProvider);
	}

	@Override
	public AppStoreService getService() {
		return Activator.getAppStoreService();
	}

	@Override
	public synchronized void updateIndex(IndexProvider indexProvider, AppStoreService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		IndexItem indexItem = indexProvider.getIndexItem(OrbitConstants.APP_STORE_INDEXER_ID, OrbitConstants.APP_STORE_TYPE, name);
		if (indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.APPSTORE_HOST_URL, hostURL);
			props.put(OrbitConstants.APPSTORE_CONTEXT_ROOT, contextRoot);
			props.put(OrbitConstants.APPSTORE_NAME, name);
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.addIndexItem(OrbitConstants.APP_STORE_INDEXER_ID, OrbitConstants.APP_STORE_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(OrbitConstants.APP_STORE_INDEXER_ID, indexItemId, props);
		}
	}

}
