package other.orbit.component.runtime.tier1.account.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.Activator;
import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.OrbitServices;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProviderClient;
import org.origin.common.thread.other.ServiceIndexTimerV1;
import org.origin.common.thread.other.ServiceIndexTimerImplV1;

/**
 * UserRegistry service timer to update index item for the service.
 *
 */
public class UserRegistryServiceIndexTimerV1 extends ServiceIndexTimerImplV1<IndexProviderClient, UserRegistryService> implements ServiceIndexTimerV1<IndexProviderClient, UserRegistryService> {

	/**
	 * 
	 * @param indexProvider
	 */
	public UserRegistryServiceIndexTimerV1(IndexProviderClient indexProvider) {
		super("UserRegistry Index Timer", indexProvider);
	}

	@Override
	public UserRegistryService getService() {
		return OrbitServices.getInstance().getUserRegistryService();
	}

	@Override
	public synchronized void updateIndex(IndexProviderClient indexProvider, UserRegistryService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		IndexItem indexItem = indexProvider.getIndexItem(ComponentConstants.USER_REGISTRY_INDEXER_ID, ComponentConstants.USER_REGISTRY_TYPE, name);
		if (indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(ComponentConstants.USER_REGISTRY_HOST_URL, hostURL);
			props.put(ComponentConstants.USER_REGISTRY_CONTEXT_ROOT, contextRoot);
			props.put(ComponentConstants.USER_REGISTRY_NAME, name);
			props.put(ComponentConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.addIndexItem(ComponentConstants.USER_REGISTRY_INDEXER_ID, ComponentConstants.USER_REGISTRY_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(ComponentConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(ComponentConstants.USER_REGISTRY_INDEXER_ID, indexItemId, props);
		}
	}

}
