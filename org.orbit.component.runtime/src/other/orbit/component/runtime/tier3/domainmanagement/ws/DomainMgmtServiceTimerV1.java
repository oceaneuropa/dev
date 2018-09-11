package other.orbit.component.runtime.tier3.domainmanagement.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.OrbitServices;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProviderClient;
import org.origin.common.thread.other.ServiceIndexTimerImplV1;
import org.origin.common.thread.other.ServiceIndexTimerV1;

public class DomainMgmtServiceTimerV1 extends ServiceIndexTimerImplV1<IndexProviderClient, DomainManagementService> implements ServiceIndexTimerV1<IndexProviderClient, DomainManagementService> {

	protected IndexItem indexItem;

	public DomainMgmtServiceTimerV1(IndexProviderClient indexProvider) {
		super("Index Timer [Domain Management Service]", indexProvider);
		setDebug(true);
	}

	@Override
	public synchronized DomainManagementService getService() {
		return OrbitServices.getInstance().getDomainService();
	}

	@Override
	public synchronized void updateIndex(IndexProviderClient indexProvider, DomainManagementService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		if (debug) {
			System.out.println(getClass().getSimpleName() + ".updateIndex() " + name + " - " + hostURL + " - " + contextRoot);
			System.out.println();
		}

		this.indexItem = indexProvider.getIndexItem(ComponentConstants.DOMAIN_SERVICE_INDEXER_ID, ComponentConstants.DOMAIN_SERVICE_TYPE, name);
		if (this.indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(ComponentConstants.DOMAIN_SERVICE_NAME, name);
			props.put(ComponentConstants.DOMAIN_SERVICE_HOST_URL, hostURL);
			props.put(ComponentConstants.DOMAIN_SERVICE_CONTEXT_ROOT, contextRoot);
			props.put(ComponentConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			this.indexItem = indexProvider.addIndexItem(ComponentConstants.DOMAIN_SERVICE_INDEXER_ID, ComponentConstants.DOMAIN_SERVICE_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = this.indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(ComponentConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(ComponentConstants.DOMAIN_SERVICE_INDEXER_ID, indexItemId, props);
		}
	}

	/**
	 * Delete the index item for the service.
	 * 
	 * @param indexProvider
	 * @throws IOException
	 */
	@Override
	public synchronized void removeIndex(IndexProviderClient indexProvider) throws IOException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".removeIndex()");
		}

		if (this.indexItem != null) {
			Integer indexItemId = indexItem.getIndexItemId();
			if (debug) {
				System.out.println("\tindexItemId = " + indexItemId);
			}

			indexProvider.deleteIndexItem(ComponentConstants.DOMAIN_SERVICE_INDEXER_ID, indexItemId);
		}
	}

}
