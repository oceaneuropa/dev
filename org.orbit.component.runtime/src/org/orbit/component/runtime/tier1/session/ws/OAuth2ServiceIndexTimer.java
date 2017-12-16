package org.orbit.component.runtime.tier1.session.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier1.session.service.OAuth2Service;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.common.thread.ServiceIndexTimer;

public class OAuth2ServiceIndexTimer extends ServiceIndexTimerImpl<IndexProvider, OAuth2Service, IndexItem> implements ServiceIndexTimer<IndexProvider, OAuth2Service, IndexItem> {

	protected OAuth2Service service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public OAuth2ServiceIndexTimer(IndexProvider indexProvider, OAuth2Service service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
	}

	@Override
	public OAuth2Service getService() {
		// return Activator.getOAuth2Service();
		return this.service;
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
