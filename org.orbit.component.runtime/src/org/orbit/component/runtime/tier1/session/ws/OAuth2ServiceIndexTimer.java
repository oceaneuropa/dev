package org.orbit.component.runtime.tier1.session.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.tier1.session.service.OAuth2Service;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;

public class OAuth2ServiceIndexTimer extends ServiceIndexTimer<OAuth2Service> {

	protected OAuth2Service service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public OAuth2ServiceIndexTimer(IndexServiceClient indexProvider, OAuth2Service service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
	}

	@Override
	public OAuth2Service getService() {
		// return Activator.getOAuth2Service();
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider, OAuth2Service service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(ComponentConstants.OAUTH2_INDEXER_ID, ComponentConstants.OAUTH2_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider, OAuth2Service service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(ComponentConstants.OAUTH2_INDEXER_ID, ComponentConstants.OAUTH2_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, OAuth2Service service, IndexItem indexItem) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(ComponentConstants.OAUTH2_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.deleteIndexItem(ComponentConstants.OAUTH2_INDEXER_ID, indexItemId);
	}

}
