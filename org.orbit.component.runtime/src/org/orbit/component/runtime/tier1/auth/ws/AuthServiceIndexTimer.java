package org.orbit.component.runtime.tier1.auth.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.tier1.auth.service.AuthService;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.origin.common.service.WebServiceAwareHelper;

public class AuthServiceIndexTimer extends ServiceIndexTimer<AuthService> {

	protected AuthService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public AuthServiceIndexTimer(IndexServiceClient indexProvider, AuthService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public AuthService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider, AuthService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(ComponentConstants.AUTH_INDEXER_ID, ComponentConstants.AUTH_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider, AuthService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);

		Date now = new Date();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);

		return indexProvider.addIndexItem(ComponentConstants.AUTH_INDEXER_ID, ComponentConstants.AUTH_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, AuthService service, IndexItem indexItem) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);

		Integer indexItemId = indexItem.getIndexItemId();

		Date now = new Date();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);

		indexProvider.setProperties(ComponentConstants.AUTH_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.deleteIndexItem(ComponentConstants.AUTH_INDEXER_ID, indexItemId);
	}

}
