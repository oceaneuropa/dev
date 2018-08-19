package org.orbit.component.runtime.tier1.session.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentsConstants;
import org.orbit.component.runtime.tier1.session.service.OAuth2Service;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimer;

public class OAuth2ServiceIndexTimer extends ServiceIndexTimer<OAuth2Service> {

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

		return indexProvider.getIndexItem(ComponentsConstants.OAUTH2_INDEXER_ID, ComponentsConstants.OAUTH2_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, OAuth2Service service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(ComponentsConstants.OAUTH2_NAME, name);
		props.put(ComponentsConstants.OAUTH2_HOST_URL, hostURL);
		props.put(ComponentsConstants.OAUTH2_CONTEXT_ROOT, contextRoot);
		props.put(ComponentsConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(ComponentsConstants.OAUTH2_INDEXER_ID, ComponentsConstants.OAUTH2_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, OAuth2Service service, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(ComponentsConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(ComponentsConstants.OAUTH2_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(ComponentsConstants.OAUTH2_INDEXER_ID, indexItemId);
	}

}
