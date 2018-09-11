package other.orbit.component.runtime.tier1.config.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.OrbitServices;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProviderClient;
import org.origin.common.thread.other.ServiceIndexTimerImplV1;
import org.origin.common.thread.other.ServiceIndexTimerV1;

/**
 * ConfigRegistry service timer to update index item for the service.
 *
 */
public class ConfigRegistryServiceIndexTimerV1 extends ServiceIndexTimerImplV1<IndexProviderClient, ConfigRegistryService> implements ServiceIndexTimerV1<IndexProviderClient, ConfigRegistryService> {

	/**
	 * 
	 * @param indexProvider
	 */
	public ConfigRegistryServiceIndexTimerV1(IndexProviderClient indexProvider) {
		super("ConfigRegistry Index Timer", indexProvider);
	}

	@Override
	public ConfigRegistryService getService() {
		return OrbitServices.getInstance().getConfigRegistryService();
	}

	@Override
	public synchronized void updateIndex(IndexProviderClient indexProvider, ConfigRegistryService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		IndexItem indexItem = indexProvider.getIndexItem(ComponentConstants.CONFIG_REGISTRY_INDEXER_ID, ComponentConstants.CONFIG_REGISTRY_TYPE, name);
		if (indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(ComponentConstants.CONFIG_REGISTRY_HOST_URL, hostURL);
			props.put(ComponentConstants.CONFIG_REGISTRY_CONTEXT_ROOT, contextRoot);
			props.put(ComponentConstants.CONFIG_REGISTRY_NAME, name);
			props.put(ComponentConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.addIndexItem(ComponentConstants.CONFIG_REGISTRY_INDEXER_ID, ComponentConstants.CONFIG_REGISTRY_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(ComponentConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(ComponentConstants.CONFIG_REGISTRY_INDEXER_ID, indexItemId, props);
		}
	}

}
