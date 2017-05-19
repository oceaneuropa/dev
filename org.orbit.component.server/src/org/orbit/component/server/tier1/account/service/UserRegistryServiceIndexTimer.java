package org.orbit.component.server.tier1.account.service;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.Activator;
import org.orbit.component.server.OrbitConstants;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

/**
 * UserRegistry service timer to update index item for the service.
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class UserRegistryServiceIndexTimer extends ServiceIndexTimerImpl<IndexProvider, UserRegistryService> implements ServiceIndexTimer<IndexProvider, UserRegistryService> {

	/**
	 * 
	 * @param indexProvider
	 */
	public UserRegistryServiceIndexTimer(IndexProvider indexProvider) {
		super("UserRegistry Index Timer", indexProvider);
	}

	@Override
	public UserRegistryService getService() {
		return Activator.getUserRegistryService();
	}

	@Override
	public synchronized void updateIndex(IndexProvider indexProvider, UserRegistryService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		IndexItem indexItem = indexProvider.getIndexItem(OrbitConstants.USER_REGISTRY_INDEXER_ID, OrbitConstants.USER_REGISTRY_TYPE, name);
		if (indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.USER_REGISTRY_HOST_URL, hostURL);
			props.put(OrbitConstants.USER_REGISTRY_CONTEXT_ROOT, contextRoot);
			props.put(OrbitConstants.USER_REGISTRY_NAME, name);
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date());

			indexProvider.addIndexItem(OrbitConstants.USER_REGISTRY_INDEXER_ID, OrbitConstants.USER_REGISTRY_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date());

			indexProvider.setProperties(OrbitConstants.USER_REGISTRY_INDEXER_ID, indexItemId, props);
		}
	}

}
