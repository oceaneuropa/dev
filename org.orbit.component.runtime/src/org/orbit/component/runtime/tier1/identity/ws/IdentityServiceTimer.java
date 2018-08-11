package org.orbit.component.runtime.tier1.identity.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.OrbitConstants;
import org.orbit.component.runtime.tier1.identity.service.IdentityService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
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
	public IdentityServiceTimer(IndexProvider indexProvider, IdentityService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public IdentityService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, IdentityService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OrbitConstants.IDENTITY_INDEXER_ID, OrbitConstants.IDENTITY_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, IdentityService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.IDENTITY_NAME, name);
		props.put(OrbitConstants.IDENTITY_HOST_URL, hostURL);
		props.put(OrbitConstants.IDENTITY_CONTEXT_ROOT, contextRoot);
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);

		return indexProvider.addIndexItem(OrbitConstants.IDENTITY_INDEXER_ID, OrbitConstants.IDENTITY_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, IdentityService service, IndexItem indexItem) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> newProperties = new Hashtable<String, Object>();
		newProperties.put(OrbitConstants.IDENTITY_NAME, name);
		newProperties.put(OrbitConstants.IDENTITY_HOST_URL, hostURL);
		newProperties.put(OrbitConstants.IDENTITY_CONTEXT_ROOT, contextRoot);
		newProperties.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);

		indexProvider.setProperties(OrbitConstants.IDENTITY_INDEXER_ID, indexItemId, newProperties);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.IDENTITY_INDEXER_ID, indexItemId);
	}

}
