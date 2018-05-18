package other.orbit.component.runtime.tier1.config.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.OrbitServices;
import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.origin.common.thread.other.ServiceIndexTimerImplV1;
import org.origin.common.thread.other.ServiceIndexTimerV1;

/**
 * ConfigRegistry service timer to update index item for the service.
 *
 */
public class ConfigRegistryServiceIndexTimerV1 extends ServiceIndexTimerImplV1<IndexProvider, ConfigRegistryService> implements ServiceIndexTimerV1<IndexProvider, ConfigRegistryService> {

	/**
	 * 
	 * @param indexProvider
	 */
	public ConfigRegistryServiceIndexTimerV1(IndexProvider indexProvider) {
		super("ConfigRegistry Index Timer", indexProvider);
	}

	@Override
	public ConfigRegistryService getService() {
		return OrbitServices.getInstance().getConfigRegistryService();
	}

	@Override
	public synchronized void updateIndex(IndexProvider indexProvider, ConfigRegistryService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		IndexItem indexItem = indexProvider.getIndexItem(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, OrbitConstants.CONFIG_REGISTRY_TYPE, name);
		if (indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.CONFIG_REGISTRY_HOST_URL, hostURL);
			props.put(OrbitConstants.CONFIG_REGISTRY_CONTEXT_ROOT, contextRoot);
			props.put(OrbitConstants.CONFIG_REGISTRY_NAME, name);
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.addIndexItem(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, OrbitConstants.CONFIG_REGISTRY_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, indexItemId, props);
		}
	}

}
