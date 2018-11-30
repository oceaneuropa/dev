package org.orbit.component.runtime.tier3.nodecontrol.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;

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

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(ComponentConstants.NODE_CONTROL_HOME, taHome);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);

		return indexProvider.addIndexItem(ComponentConstants.NODE_CONTROL_INDEXER_ID, ComponentConstants.NODE_CONTROL_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, NodeControlService service, IndexItem indexItem) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String platformHome = service.getPlatformHome();

		Date now = new Date();

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(ComponentConstants.NODE_CONTROL_HOME, platformHome);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);

		indexProvider.setProperties(ComponentConstants.NODE_CONTROL_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.deleteIndexItem(ComponentConstants.NODE_CONTROL_INDEXER_ID, indexItemId);
	}

}
