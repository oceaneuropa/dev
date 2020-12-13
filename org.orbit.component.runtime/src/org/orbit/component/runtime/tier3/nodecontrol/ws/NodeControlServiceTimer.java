package org.orbit.component.runtime.tier3.nodecontrol.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.origin.common.service.WebServiceAwareHelper;
import org.origin.common.util.MapHelper;

public class NodeControlServiceTimer extends ServiceIndexTimer<NodeControlService> {

	/**
	 * 
	 * @param service
	 */
	public NodeControlServiceTimer(NodeControlService service) {
		super(ComponentConstants.NODE_CONTROL_INDEXER_ID, "Index Timer [" + service.getName() + "]", service);
		setDebug(true);
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider) throws IOException {
		NodeControlService service = getService();

		String name = service.getName();
		return indexProvider.getIndexItem(getIndexProviderId(), ComponentConstants.NODE_CONTROL_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider) throws IOException {
		NodeControlService service = getService();

		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);
		String taHome = service.getPlatformHome();

		Date now = new Date();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);
		props.put(ComponentConstants.NODE_CONTROL_HOME, taHome);

		return indexProvider.addIndexItem(getIndexProviderId(), ComponentConstants.NODE_CONTROL_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		NodeControlService service = getService();

		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);
		String platformHome = service.getPlatformHome();

		Date now = new Date();

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);
		props.put(ComponentConstants.NODE_CONTROL_HOME, platformHome);

		indexProvider.setProperties(getIndexProviderId(), indexItemId, props);
	}

	@Override
	public void cleanupIndex(IndexServiceClient indexService, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = indexItem.getProperties();
		List<String> propertyNames = MapHelper.INSTANCE.getKeyList(props);
		indexService.removeProperties(getIndexProviderId(), indexItemId, propertyNames);
	}

	@Override
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		indexProvider.removeIndexItem(getIndexProviderId(), indexItemId);
	}

}
