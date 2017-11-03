package org.orbit.component.server.tier3.domain.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.Activator;
import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

public class DomainMgmtServiceTimer extends ServiceIndexTimerImpl<IndexProvider, DomainManagementService> implements ServiceIndexTimer<IndexProvider, DomainManagementService> {

	protected IndexItem indexItem;

	public DomainMgmtServiceTimer(IndexProvider indexProvider) {
		super("Index Timer [Domain Management Service]", indexProvider);
		setDebug(true);
	}

	@Override
	public synchronized DomainManagementService getService() {
		return Activator.getDomainMgmtService();
	}

	@Override
	public synchronized void updateIndex(IndexProvider indexProvider, DomainManagementService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		if (debug) {
			System.out.println(getClass().getSimpleName() + ".updateIndex() " + name + " - " + hostURL + " - " + contextRoot);
			System.out.println();
		}

		this.indexItem = indexProvider.getIndexItem(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, OrbitConstants.DOMAIN_MANAGEMENT_TYPE, name);
		if (this.indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.DOMAIN_MANAGEMENT_NAME, name);
			props.put(OrbitConstants.DOMAIN_MANAGEMENT_HOST_URL, hostURL);
			props.put(OrbitConstants.DOMAIN_MANAGEMENT_CONTEXT_ROOT, contextRoot);
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			this.indexItem = indexProvider.addIndexItem(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, OrbitConstants.DOMAIN_MANAGEMENT_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = this.indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, indexItemId, props);
		}
	}

	/**
	 * Delete the index item for the service.
	 * 
	 * @param indexProvider
	 * @throws IOException
	 */
	@Override
	public synchronized void removeIndex(IndexProvider indexProvider) throws IOException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".removeIndex()");
		}

		if (this.indexItem != null) {
			Integer indexItemId = indexItem.getIndexItemId();
			if (debug) {
				System.out.println("\tindexItemId = " + indexItemId);
			}

			indexProvider.removeIndexItem(OrbitConstants.DOMAIN_MANAGEMENT_INDEXER_ID, indexItemId);
		}
	}

}
