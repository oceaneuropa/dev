package other.orbit.component.runtime.tier3.nodecontrol.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.ComponentServices;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProviderClient;
import org.origin.common.thread.other.ServiceIndexTimerImplV1;
import org.origin.common.thread.other.ServiceIndexTimerV1;

public class TransferAgentServiceTimerV1 extends ServiceIndexTimerImplV1<IndexProviderClient, NodeControlService> implements ServiceIndexTimerV1<IndexProviderClient, NodeControlService> {

	protected IndexItem indexItem;

	public TransferAgentServiceTimerV1(IndexProviderClient indexProvider) {
		super("Index Timer [Transfer Agent Service]", indexProvider);
	}

	@Override
	public synchronized NodeControlService getService() {
		return ComponentServices.getInstance().getTransferAgentService();
	}

	@Override
	public synchronized void updateIndex(IndexProviderClient indexProvider, NodeControlService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String taHome = service.getPlatformHome();

		this.indexItem = indexProvider.getIndexItem(ComponentConstants.NODE_CONTROL_INDEXER_ID, ComponentConstants.NODE_CONTROL_TYPE, name);
		if (this.indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(InfraConstants.SERVICE__NAME, name);
			props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
			props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
			props.put(ComponentConstants.NODE_CONTROL_HOME, taHome);
			props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date().getTime());

			this.indexItem = indexProvider.addIndexItem(ComponentConstants.NODE_CONTROL_INDEXER_ID, ComponentConstants.NODE_CONTROL_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = this.indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(ComponentConstants.NODE_CONTROL_INDEXER_ID, indexItemId, props);
		}
	}

	@Override
	public synchronized void removeIndex(IndexProviderClient indexProvider) throws IOException {
		if (this.indexItem != null) {
			indexProvider.deleteIndexItem(ComponentConstants.NODE_CONTROL_INDEXER_ID, indexItem.getIndexItemId());
		}
	}

}
