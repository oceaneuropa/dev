package other.orbit.component.runtime.tier1.config.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.OrbitServices;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceV0;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProviderClient;
import org.origin.common.thread.other.ServiceIndexTimerImplV1;
import org.origin.common.thread.other.ServiceIndexTimerV1;

/**
 * ConfigRegistry service timer to update index item for the service.
 *
 */
public class ConfigRegistryServiceIndexTimerV1 extends ServiceIndexTimerImplV1<IndexProviderClient, ConfigRegistryServiceV0> implements ServiceIndexTimerV1<IndexProviderClient, ConfigRegistryServiceV0> {

	/**
	 * 
	 * @param indexProvider
	 */
	public ConfigRegistryServiceIndexTimerV1(IndexProviderClient indexProvider) {
		super("ConfigRegistry Index Timer", indexProvider);
	}

	@Override
	public ConfigRegistryServiceV0 getService() {
		return OrbitServices.getInstance().getConfigRegistryService();
	}

	@Override
	public synchronized void updateIndex(IndexProviderClient indexProvider, ConfigRegistryServiceV0 service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		IndexItem indexItem = indexProvider.getIndexItem(ComponentConstants.CONFIG_REGISTRY_INDEXER_ID, ComponentConstants.CONFIG_REGISTRY_TYPE, name);
		if (indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(InfraConstants.SERVICE__NAME, name);
			props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
			props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
			props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.addIndexItem(ComponentConstants.CONFIG_REGISTRY_INDEXER_ID, ComponentConstants.CONFIG_REGISTRY_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(ComponentConstants.CONFIG_REGISTRY_INDEXER_ID, indexItemId, props);
		}
	}

}
