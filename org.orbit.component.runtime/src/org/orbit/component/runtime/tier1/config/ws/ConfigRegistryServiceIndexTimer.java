package org.orbit.component.runtime.tier1.config.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.OrbitConstants;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.util.DateUtil;

/**
 * ConfigRegistry service timer to update index item for the service.
 *
 */
public class ConfigRegistryServiceIndexTimer extends ServiceIndexTimerImpl<IndexProvider, ConfigRegistryService, IndexItem> implements ServiceIndexTimer<IndexProvider, ConfigRegistryService, IndexItem> {

	protected ConfigRegistryService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public ConfigRegistryServiceIndexTimer(IndexProvider indexProvider, ConfigRegistryService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public ConfigRegistryService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, ConfigRegistryService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, OrbitConstants.CONFIG_REGISTRY_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, ConfigRegistryService service) throws IOException {
		String namespace = service.getNamespace();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.CONFIG_REGISTRY_NAMESPACE, namespace);
		props.put(OrbitConstants.CONFIG_REGISTRY_NAME, name);
		props.put(OrbitConstants.CONFIG_REGISTRY_HOST_URL, hostURL);
		props.put(OrbitConstants.CONFIG_REGISTRY_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		return indexProvider.addIndexItem(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, OrbitConstants.CONFIG_REGISTRY_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, ConfigRegistryService service, IndexItem indexItem) throws IOException {
		String namespace = service.getNamespace();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.CONFIG_REGISTRY_NAMESPACE, namespace);
		props.put(OrbitConstants.CONFIG_REGISTRY_NAME, name);
		props.put(OrbitConstants.CONFIG_REGISTRY_HOST_URL, hostURL);
		props.put(OrbitConstants.CONFIG_REGISTRY_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		indexProvider.setProperties(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, indexItemId);
	}

}
