package org.orbit.component.runtime.tier3.transferagent.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier3.transferagent.service.TransferAgentService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.common.util.DateUtil;

public class TransferAgentServiceTimer extends ServiceIndexTimerImpl<IndexProvider, TransferAgentService, IndexItem> implements ServiceIndexTimer<IndexProvider, TransferAgentService, IndexItem> {

	protected TransferAgentService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public TransferAgentServiceTimer(IndexProvider indexProvider, TransferAgentService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public synchronized TransferAgentService getService() {
		return this.service;
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
	public void updateIndex(IndexProvider indexProvider, TransferAgentService service, IndexItem indexItem) throws IOException {
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