package org.orbit.component.runtime.tier3.nodecontrol.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.common.util.DateUtil;

public class NodeControlServiceTimer extends ServiceIndexTimerImpl<IndexProvider, NodeControlService, IndexItem> implements ServiceIndexTimer<IndexProvider, NodeControlService, IndexItem> {

	protected NodeControlService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public NodeControlServiceTimer(IndexProvider indexProvider, NodeControlService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public synchronized NodeControlService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, NodeControlService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OrbitConstants.NODE_CONTROL_INDEXER_ID, OrbitConstants.NODE_CONTROL_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, NodeControlService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String taHome = service.getHome();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.NODE_CONTROL_NAME, name);
		props.put(OrbitConstants.NODE_CONTROL_HOST_URL, hostURL);
		props.put(OrbitConstants.NODE_CONTROL_CONTEXT_ROOT, contextRoot);
		props.put(OrbitConstants.NODE_CONTROL_HOME, taHome);
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(OrbitConstants.NODE_CONTROL_INDEXER_ID, OrbitConstants.NODE_CONTROL_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, NodeControlService service, IndexItem indexItem) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String taHome = service.getHome();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.NODE_CONTROL_NAME, name);
		props.put(OrbitConstants.NODE_CONTROL_HOST_URL, hostURL);
		props.put(OrbitConstants.NODE_CONTROL_CONTEXT_ROOT, contextRoot);
		props.put(OrbitConstants.NODE_CONTROL_HOME, taHome);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		indexProvider.setProperties(OrbitConstants.NODE_CONTROL_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.NODE_CONTROL_INDEXER_ID, indexItemId);
	}

}
