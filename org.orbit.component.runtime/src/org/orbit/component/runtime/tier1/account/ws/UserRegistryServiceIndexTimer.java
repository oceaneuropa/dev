package org.orbit.component.runtime.tier1.account.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.origin.common.util.DateUtil;

/**
 * Indexer for UserRegistry service.
 *
 */
public class UserRegistryServiceIndexTimer extends ServiceIndexTimer<UserRegistryService> {

	protected UserRegistryService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public UserRegistryServiceIndexTimer(IndexServiceClient indexProvider, UserRegistryService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public UserRegistryService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider, UserRegistryService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(ComponentConstants.USER_REGISTRY_INDEXER_ID, ComponentConstants.USER_REGISTRY_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider, UserRegistryService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(ComponentConstants.USER_REGISTRY_NAME, name);
		props.put(ComponentConstants.USER_REGISTRY_HOST_URL, hostURL);
		props.put(ComponentConstants.USER_REGISTRY_CONTEXT_ROOT, contextRoot);
		props.put(ComponentConstants.LAST_HEARTBEAT_TIME, now);
		props.put(ComponentConstants.HEARTBEAT_EXPIRE_TIME, expire);

		return indexProvider.addIndexItem(ComponentConstants.USER_REGISTRY_INDEXER_ID, ComponentConstants.USER_REGISTRY_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, UserRegistryService service, IndexItem indexItem) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> newProperties = new Hashtable<String, Object>();
		newProperties.put(ComponentConstants.USER_REGISTRY_NAME, name);
		newProperties.put(ComponentConstants.USER_REGISTRY_HOST_URL, hostURL);
		newProperties.put(ComponentConstants.USER_REGISTRY_CONTEXT_ROOT, contextRoot);
		newProperties.put(ComponentConstants.LAST_HEARTBEAT_TIME, now);
		newProperties.put(ComponentConstants.HEARTBEAT_EXPIRE_TIME, expire);

		indexProvider.setProperties(ComponentConstants.USER_REGISTRY_INDEXER_ID, indexItemId, newProperties);
	}

	@Override
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.deleteIndexItem(ComponentConstants.USER_REGISTRY_INDEXER_ID, indexItemId);
	}

}

// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
