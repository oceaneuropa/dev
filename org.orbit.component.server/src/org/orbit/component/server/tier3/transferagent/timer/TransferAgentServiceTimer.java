package org.orbit.component.server.tier3.transferagent.timer;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.Activator;
import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

public class TransferAgentServiceTimer extends ServiceIndexTimerImpl<IndexProvider, TransferAgentService> implements ServiceIndexTimer<IndexProvider, TransferAgentService> {

	protected IndexItem indexItem;

	public TransferAgentServiceTimer(IndexProvider indexProvider) {
		super("Index Timer [Transfer Agent Service]", indexProvider);
	}

	@Override
	public synchronized TransferAgentService getService() {
		return Activator.getTransferAgentService();
	}

	@Override
	public synchronized void updateIndex(IndexProvider indexProvider, TransferAgentService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String taHome = service.getHome();

		this.indexItem = indexProvider.getIndexItem(OrbitConstants.TRANSFER_AGENT_INDEXER_ID, OrbitConstants.TRANSFER_AGENT_TYPE, name);
		if (this.indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.TRANSFER_AGENT_NAME, name);
			props.put(OrbitConstants.TRANSFER_AGENT_HOST_URL, hostURL);
			props.put(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT, contextRoot);
			props.put(OrbitConstants.TRANSFER_AGENT_HOME, taHome);
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			this.indexItem = indexProvider.addIndexItem(OrbitConstants.TRANSFER_AGENT_INDEXER_ID, OrbitConstants.TRANSFER_AGENT_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = this.indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(OrbitConstants.TRANSFER_AGENT_INDEXER_ID, indexItemId, props);
		}
	}

	@Override
	public synchronized void removeIndex(IndexProvider indexProvider) throws IOException {
		if (this.indexItem != null) {
			indexProvider.removeIndexItem(OrbitConstants.TRANSFER_AGENT_INDEXER_ID, indexItem.getIndexItemId());
		}
	}

}
