package other.orbit.component.runtime.tier3.nodecontrol.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.OrbitServices;
import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.origin.common.thread.other.ServiceIndexTimerImplV1;
import org.origin.common.thread.other.ServiceIndexTimerV1;

public class TransferAgentServiceTimerV1 extends ServiceIndexTimerImplV1<IndexProvider, NodeControlService> implements ServiceIndexTimerV1<IndexProvider, NodeControlService> {

	protected IndexItem indexItem;

	public TransferAgentServiceTimerV1(IndexProvider indexProvider) {
		super("Index Timer [Transfer Agent Service]", indexProvider);
	}

	@Override
	public synchronized NodeControlService getService() {
		return OrbitServices.getInstance().getTransferAgentService();
	}

	@Override
	public synchronized void updateIndex(IndexProvider indexProvider, NodeControlService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String taHome = service.getPlatformHome();

		this.indexItem = indexProvider.getIndexItem(OrbitConstants.NODE_CONTROL_INDEXER_ID, OrbitConstants.NODE_CONTROL_TYPE, name);
		if (this.indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.NODE_CONTROL_NAME, name);
			props.put(OrbitConstants.NODE_CONTROL_HOST_URL, hostURL);
			props.put(OrbitConstants.NODE_CONTROL_CONTEXT_ROOT, contextRoot);
			props.put(OrbitConstants.NODE_CONTROL_HOME, taHome);
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			this.indexItem = indexProvider.addIndexItem(OrbitConstants.NODE_CONTROL_INDEXER_ID, OrbitConstants.NODE_CONTROL_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = this.indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(OrbitConstants.NODE_CONTROL_INDEXER_ID, indexItemId, props);
		}
	}

	@Override
	public synchronized void removeIndex(IndexProvider indexProvider) throws IOException {
		if (this.indexItem != null) {
			indexProvider.removeIndexItem(OrbitConstants.NODE_CONTROL_INDEXER_ID, indexItem.getIndexItemId());
		}
	}

}
