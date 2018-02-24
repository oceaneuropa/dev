package org.orbit.component.runtime.tier3.nodecontrol.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeManagementService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.common.util.DateUtil;

public class TransferAgentServiceTimer extends ServiceIndexTimerImpl<IndexProvider, NodeManagementService, IndexItem> implements ServiceIndexTimer<IndexProvider, NodeManagementService, IndexItem> {

	protected NodeManagementService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public TransferAgentServiceTimer(IndexProvider indexProvider, NodeManagementService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public synchronized NodeManagementService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, NodeManagementService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OrbitConstants.TRANSFER_AGENT_INDEXER_ID, OrbitConstants.TRANSFER_AGENT_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, NodeManagementService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String taHome = service.getHome();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.TRANSFER_AGENT_NAME, name);
		props.put(OrbitConstants.TRANSFER_AGENT_HOST_URL, hostURL);
		props.put(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT, contextRoot);
		props.put(OrbitConstants.TRANSFER_AGENT_HOME, taHome);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		return indexProvider.addIndexItem(OrbitConstants.TRANSFER_AGENT_INDEXER_ID, OrbitConstants.TRANSFER_AGENT_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, NodeManagementService service, IndexItem indexItem) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String taHome = service.getHome();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.TRANSFER_AGENT_NAME, name);
		props.put(OrbitConstants.TRANSFER_AGENT_HOST_URL, hostURL);
		props.put(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT, contextRoot);
		props.put(OrbitConstants.TRANSFER_AGENT_HOME, taHome);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		indexProvider.setProperties(OrbitConstants.TRANSFER_AGENT_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.TRANSFER_AGENT_INDEXER_ID, indexItemId);
	}

}
