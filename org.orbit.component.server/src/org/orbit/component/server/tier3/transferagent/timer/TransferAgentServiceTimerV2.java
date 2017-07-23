package org.orbit.component.server.tier3.transferagent.timer;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.Activator;
import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.thread.ServiceIndexTimerImplV2;
import org.origin.common.thread.ServiceIndexTimerV2;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

public class TransferAgentServiceTimerV2 extends ServiceIndexTimerImplV2<IndexProvider, TransferAgentService, IndexItem> implements ServiceIndexTimerV2<IndexProvider, TransferAgentService, IndexItem> {

	/**
	 * 
	 * @param indexProvider
	 */
	public TransferAgentServiceTimerV2(IndexProvider indexProvider) {
		super("Index Timer [Transfer Agent Service]", indexProvider);
	}

	@Override
	public synchronized TransferAgentService getService() {
		return Activator.getTransferAgentService();
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, TransferAgentService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OrbitConstants.TRANSFER_AGENT_INDEXER_ID, OrbitConstants.TRANSFER_AGENT_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, TransferAgentService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String taHome = service.getHome();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.TRANSFER_AGENT_NAME, name);
		props.put(OrbitConstants.TRANSFER_AGENT_HOST_URL, hostURL);
		props.put(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT, contextRoot);
		props.put(OrbitConstants.TRANSFER_AGENT_HOME, taHome);
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(OrbitConstants.TRANSFER_AGENT_INDEXER_ID, OrbitConstants.TRANSFER_AGENT_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, TransferAgentService service, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(OrbitConstants.TRANSFER_AGENT_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.TRANSFER_AGENT_INDEXER_ID, indexItemId);
	}

}
