package org.orbit.component.runtime.tier3.nodecontrol.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.origin.common.util.DateUtil;

public class NodeControlServiceTimer extends ServiceIndexTimer<NodeControlService> {

	protected NodeControlService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public NodeControlServiceTimer(IndexServiceClient indexProvider, NodeControlService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public synchronized NodeControlService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider, NodeControlService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(ComponentConstants.NODE_CONTROL_INDEXER_ID, ComponentConstants.NODE_CONTROL_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider, NodeControlService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String taHome = service.getPlatformHome();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(ComponentConstants.NODE_CONTROL_NAME, name);
		props.put(ComponentConstants.NODE_CONTROL_HOST_URL, hostURL);
		props.put(ComponentConstants.NODE_CONTROL_CONTEXT_ROOT, contextRoot);
		props.put(ComponentConstants.NODE_CONTROL_HOME, taHome);
		props.put(ComponentConstants.LAST_HEARTBEAT_TIME, now);
		props.put(ComponentConstants.HEARTBEAT_EXPIRE_TIME, expire);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(ComponentConstants.NODE_CONTROL_INDEXER_ID, ComponentConstants.NODE_CONTROL_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, NodeControlService service, IndexItem indexItem) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String platformHome = service.getPlatformHome();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(ComponentConstants.NODE_CONTROL_NAME, name);
		props.put(ComponentConstants.NODE_CONTROL_HOST_URL, hostURL);
		props.put(ComponentConstants.NODE_CONTROL_CONTEXT_ROOT, contextRoot);
		props.put(ComponentConstants.NODE_CONTROL_HOME, platformHome);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(ComponentConstants.LAST_HEARTBEAT_TIME, now);
		props.put(ComponentConstants.HEARTBEAT_EXPIRE_TIME, expire);

		indexProvider.setProperties(ComponentConstants.NODE_CONTROL_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.deleteIndexItem(ComponentConstants.NODE_CONTROL_INDEXER_ID, indexItemId);
	}

}
