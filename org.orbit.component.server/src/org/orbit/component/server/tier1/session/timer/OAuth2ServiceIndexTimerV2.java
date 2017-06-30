package org.orbit.component.server.tier1.session.timer;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.Activator;
import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier1.session.service.OAuth2Service;
import org.origin.common.thread.ServiceIndexTimerImplV2;
import org.origin.common.thread.ServiceIndexTimerV2;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

public class OAuth2ServiceIndexTimerV2 extends ServiceIndexTimerImplV2<IndexProvider, OAuth2Service, IndexItem> implements ServiceIndexTimerV2<IndexProvider, OAuth2Service, IndexItem> {

	public OAuth2ServiceIndexTimerV2(IndexProvider indexProvider) {
		super("Index Timer [OAuth2 Service]", indexProvider);
	}

	@Override
	public OAuth2Service getService() {
		return Activator.getOAuth2Service();
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, OAuth2Service service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OrbitConstants.OAUTH2_INDEXER_ID, OrbitConstants.OAUTH2_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, OAuth2Service service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.OAUTH2_NAME, name);
		props.put(OrbitConstants.OAUTH2_HOST_URL, hostURL);
		props.put(OrbitConstants.OAUTH2_CONTEXT_ROOT, contextRoot);
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(OrbitConstants.OAUTH2_INDEXER_ID, OrbitConstants.OAUTH2_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, OAuth2Service service, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(OrbitConstants.OAUTH2_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.OAUTH2_INDEXER_ID, indexItemId);
	}

}
