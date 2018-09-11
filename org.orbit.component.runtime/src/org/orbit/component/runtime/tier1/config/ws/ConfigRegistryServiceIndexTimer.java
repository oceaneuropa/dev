package org.orbit.component.runtime.tier1.config.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.origin.common.util.DateUtil;

/**
 * ConfigRegistry service timer to update index item for the service.
 *
 */
public class ConfigRegistryServiceIndexTimer extends ServiceIndexTimer<ConfigRegistryService> {

	protected ConfigRegistryService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public ConfigRegistryServiceIndexTimer(IndexServiceClient indexProvider, ConfigRegistryService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public ConfigRegistryService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider, ConfigRegistryService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(ComponentConstants.CONFIG_REGISTRY_INDEXER_ID, ComponentConstants.CONFIG_REGISTRY_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider, ConfigRegistryService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(ComponentConstants.CONFIG_REGISTRY_NAME, name);
		props.put(ComponentConstants.CONFIG_REGISTRY_HOST_URL, hostURL);
		props.put(ComponentConstants.CONFIG_REGISTRY_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(ComponentConstants.LAST_HEARTBEAT_TIME, now);
		props.put(ComponentConstants.HEARTBEAT_EXPIRE_TIME, expire);

		return indexProvider.addIndexItem(ComponentConstants.CONFIG_REGISTRY_INDEXER_ID, ComponentConstants.CONFIG_REGISTRY_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, ConfigRegistryService service, IndexItem indexItem) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(ComponentConstants.CONFIG_REGISTRY_NAME, name);
		props.put(ComponentConstants.CONFIG_REGISTRY_HOST_URL, hostURL);
		props.put(ComponentConstants.CONFIG_REGISTRY_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(ComponentConstants.LAST_HEARTBEAT_TIME, now);
		props.put(ComponentConstants.HEARTBEAT_EXPIRE_TIME, expire);

		indexProvider.setProperties(ComponentConstants.CONFIG_REGISTRY_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.deleteIndexItem(ComponentConstants.CONFIG_REGISTRY_INDEXER_ID, indexItemId);
	}

}
