package other.orbit.component.runtime.tier2.appstore.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.OrbitServices;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.origin.common.thread.other.ServiceIndexTimerImplV1;
import org.origin.common.thread.other.ServiceIndexTimerV1;

/**
 * AppStore service timer to update index item for the service.
 *
 */
public class AppStoreServiceIndexTimerV1 extends ServiceIndexTimerImplV1<IndexProvider, AppStoreService> implements ServiceIndexTimerV1<IndexProvider, AppStoreService> {

	/**
	 * 
	 * @param indexProvider
	 */
	public AppStoreServiceIndexTimerV1(IndexProvider indexProvider) {
		super("AppStore Service Index Timer", indexProvider);
	}

	@Override
	public AppStoreService getService() {
		return OrbitServices.getInstance().getAppStoreService();
	}

	@Override
	public synchronized void updateIndex(IndexProvider indexProvider, AppStoreService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		IndexItem indexItem = indexProvider.getIndexItem(ComponentConstants.APP_STORE_INDEXER_ID, ComponentConstants.APP_STORE_TYPE, name);
		if (indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(ComponentConstants.APPSTORE_HOST_URL, hostURL);
			props.put(ComponentConstants.APPSTORE_CONTEXT_ROOT, contextRoot);
			props.put(ComponentConstants.APPSTORE_NAME, name);
			props.put(ComponentConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.addIndexItem(ComponentConstants.APP_STORE_INDEXER_ID, ComponentConstants.APP_STORE_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(ComponentConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(ComponentConstants.APP_STORE_INDEXER_ID, indexItemId, props);
		}
	}

}
