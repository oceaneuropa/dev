package org.orbit.component.server.tier1.config.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.Activator;
import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier1.config.service.ConfigRegistryService;
import org.origin.common.thread.ServiceIndexTimerImplV2;
import org.origin.common.thread.ServiceIndexTimerV2;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

/**
 * ConfigRegistry service timer to update index item for the service.
 *
 */
public class ConfigRegistryServiceIndexTimerV2 extends ServiceIndexTimerImplV2<IndexProvider, ConfigRegistryService, IndexItem> implements ServiceIndexTimerV2<IndexProvider, ConfigRegistryService, IndexItem> {

	/**
	 * 
	 * @param indexProvider
	 */
	public ConfigRegistryServiceIndexTimerV2(IndexProvider indexProvider) {
		super("Index Timer [ConfigRegistry Service]", indexProvider);
	}

	@Override
	public ConfigRegistryService getService() {
		return Activator.getConfigRegistryService();
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, ConfigRegistryService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, OrbitConstants.CONFIG_REGISTRY_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, ConfigRegistryService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.CONFIG_REGISTRY_HOST_URL, hostURL);
		props.put(OrbitConstants.CONFIG_REGISTRY_CONTEXT_ROOT, contextRoot);
		props.put(OrbitConstants.CONFIG_REGISTRY_NAME, name);
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, OrbitConstants.CONFIG_REGISTRY_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, ConfigRegistryService service, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, indexItemId);
	}

}
