package org.orbit.component.server.tier1.account.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.Activator;
import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier1.account.service.UserRegistryService;
import org.origin.common.thread.ServiceIndexTimerImplV2;
import org.origin.common.thread.ServiceIndexTimerV2;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

/**
 * UserRegistry service timer to update index item for the service.
 *
 */
public class UserRegistryServiceIndexTimerV2 extends ServiceIndexTimerImplV2<IndexProvider, UserRegistryService, IndexItem> implements ServiceIndexTimerV2<IndexProvider, UserRegistryService, IndexItem> {

	/**
	 * 
	 * @param indexProvider
	 */
	public UserRegistryServiceIndexTimerV2(IndexProvider indexProvider) {
		super("Index Timer [UserRegistry Service]", indexProvider);
	}

	@Override
	public UserRegistryService getService() {
		return Activator.getUserRegistryService();
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

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.USER_REGISTRY_NAME, name);
		props.put(OrbitConstants.USER_REGISTRY_HOST_URL, hostURL);
		props.put(OrbitConstants.USER_REGISTRY_CONTEXT_ROOT, contextRoot);
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(OrbitConstants.USER_REGISTRY_INDEXER_ID, OrbitConstants.USER_REGISTRY_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, UserRegistryService service, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(OrbitConstants.USER_REGISTRY_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.USER_REGISTRY_INDEXER_ID, indexItemId);
	}

}
