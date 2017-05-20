package org.orbit.component.server.tier1.config.service;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.Activator;
import org.orbit.component.server.OrbitConstants;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

/**
 * ConfigRegistry service timer to update index item for the service.
 *
 */
public class ConfigRegistryServiceIndexTimer extends ServiceIndexTimerImpl<IndexProvider, ConfigRegistryService> implements ServiceIndexTimer<IndexProvider, ConfigRegistryService> {

	/**
	 * 
	 * @param indexProvider
	 */
	public ConfigRegistryServiceIndexTimer(IndexProvider indexProvider) {
		super("ConfigRegistry Index Timer", indexProvider);
	}

	@Override
	public ConfigRegistryService getService() {
		return Activator.getConfigRegistryService();
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
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date());

			indexProvider.addIndexItem(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, OrbitConstants.CONFIG_REGISTRY_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date());

			indexProvider.setProperties(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, indexItemId, props);
		}
	}

}
