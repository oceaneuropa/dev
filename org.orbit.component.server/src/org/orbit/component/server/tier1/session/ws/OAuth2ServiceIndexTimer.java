package org.orbit.component.server.tier1.session.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.Activator;
import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier1.session.service.OAuth2Service;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

public class OAuth2ServiceIndexTimer extends ServiceIndexTimerImpl<IndexProvider, OAuth2Service> implements ServiceIndexTimer<IndexProvider, OAuth2Service> {

	public OAuth2ServiceIndexTimer(IndexProvider indexProvider) {
		super("OAuth2 Service Index Timer", indexProvider);
	}

	@Override
	public OAuth2Service getService() {
		return Activator.getOAuth2Service();
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, OAuth2Service service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		IndexItem indexItem = indexProvider.getIndexItem(OrbitConstants.OAUTH2_INDEXER_ID, OrbitConstants.OAUTH2_TYPE, name);
		if (indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.OAUTH2_HOST_URL, hostURL);
			props.put(OrbitConstants.OAUTH2_CONTEXT_ROOT, contextRoot);
			props.put(OrbitConstants.OAUTH2_NAME, name);
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.addIndexItem(OrbitConstants.OAUTH2_INDEXER_ID, OrbitConstants.OAUTH2_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(OrbitConstants.OAUTH2_INDEXER_ID, indexItemId, props);
		}
	}

}
