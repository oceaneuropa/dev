package org.orbit.component.runtime.tier1.identity.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.tier1.identity.service.IdentityService;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;

/**
 * Indexer for Identity service.
 * 
 */
public class IdentityServiceTimer extends ServiceIndexTimer<IdentityService> {

	protected IdentityService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public IdentityServiceTimer(IndexServiceClient indexProvider, IdentityService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public IdentityService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider, IdentityService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(ComponentConstants.IDENTITY_INDEXER_ID, ComponentConstants.IDENTITY_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider, IdentityService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);

		return indexProvider.addIndexItem(ComponentConstants.IDENTITY_INDEXER_ID, ComponentConstants.IDENTITY_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, IdentityService service, IndexItem indexItem) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> newProperties = new Hashtable<String, Object>();
		newProperties.put(InfraConstants.SERVICE__NAME, name);
		newProperties.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		newProperties.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		newProperties.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);

		indexProvider.setProperties(ComponentConstants.IDENTITY_INDEXER_ID, indexItemId, newProperties);
	}

	@Override
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.deleteIndexItem(ComponentConstants.IDENTITY_INDEXER_ID, indexItemId);
	}

}
