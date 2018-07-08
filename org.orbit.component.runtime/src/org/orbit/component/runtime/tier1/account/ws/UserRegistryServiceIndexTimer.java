package org.orbit.component.runtime.tier1.account.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.origin.common.util.DateUtil;

/**
 * UserRegistry service timer to update index item for the service.
 *
 */
public class UserRegistryServiceIndexTimer extends ServiceIndexTimer<UserRegistryService> {

	protected UserRegistryService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public UserRegistryServiceIndexTimer(IndexProvider indexProvider, UserRegistryService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public UserRegistryService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, UserRegistryService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OrbitConstants.USER_REGISTRY_INDEXER_ID, OrbitConstants.USER_REGISTRY_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, UserRegistryService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.USER_REGISTRY_NAME, name);
		props.put(OrbitConstants.USER_REGISTRY_HOST_URL, hostURL);
		props.put(OrbitConstants.USER_REGISTRY_CONTEXT_ROOT, contextRoot);
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		return indexProvider.addIndexItem(OrbitConstants.USER_REGISTRY_INDEXER_ID, OrbitConstants.USER_REGISTRY_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, UserRegistryService service, IndexItem indexItem) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> newProperties = new Hashtable<String, Object>();
		newProperties.put(OrbitConstants.USER_REGISTRY_NAME, name);
		newProperties.put(OrbitConstants.USER_REGISTRY_HOST_URL, hostURL);
		newProperties.put(OrbitConstants.USER_REGISTRY_CONTEXT_ROOT, contextRoot);
		newProperties.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		newProperties.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		indexProvider.setProperties(OrbitConstants.USER_REGISTRY_INDEXER_ID, indexItemId, newProperties);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.USER_REGISTRY_INDEXER_ID, indexItemId);
	}

}

// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
