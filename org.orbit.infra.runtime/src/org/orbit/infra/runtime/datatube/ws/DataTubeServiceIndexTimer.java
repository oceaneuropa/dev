package org.orbit.infra.runtime.datatube.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.origin.common.lang.MapHelper;
import org.origin.common.service.WebServiceAwareHelper;

public class DataTubeServiceIndexTimer extends ServiceIndexTimer<DataTubeService> {

	/**
	 * 
	 * @param indexService
	 * @param service
	 */
	public DataTubeServiceIndexTimer(DataTubeService service) {
		super(InfraConstants.IDX__DATATUBE__INDEXER_ID, "Index Timer [" + service.getName() + "]", service);
		setDebug(true);
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexService) throws IOException {
		DataTubeService service = getService();

		String name = service.getName();
		return indexService.getIndexItem(getIndexProviderId(), InfraConstants.IDX__DATATUBE__TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexService) throws IOException {
		DataTubeService service = getService();

		String dataCastId = service.getDataCastId();
		String dataTubeId = service.getDataTubeId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(service);
		String webSocketPort = service.getWebSocketPort();
		String webSocketURL = DataTubeWebSocketApplication.getChannelWebSocketURL(hostURL, webSocketPort);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.IDX_PROP__DATATUBE__DATACAST_ID, dataCastId);
		props.put(InfraConstants.IDX_PROP__DATATUBE__ID, dataTubeId);
		props.put(InfraConstants.IDX_PROP__DATATUBE__WEB_SOCKET_PORT, webSocketPort);
		if (webSocketURL != null) {
			props.put(InfraConstants.IDX_PROP__DATATUBE__CHANNEL_WEB_SOCKET_URL, webSocketURL);
		}
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__NAME, name);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date());

		return indexService.addIndexItem(getIndexProviderId(), InfraConstants.IDX__DATATUBE__TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexService, IndexItem indexItem) throws IOException {
		DataTubeService service = getService();

		Integer indexItemId = indexItem.getIndexItemId();
		String dataCastId = service.getDataCastId();
		String dataTubeId = service.getDataTubeId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(service);
		String webSocketPort = service.getWebSocketPort();
		String webSocketURL = DataTubeWebSocketApplication.getChannelWebSocketURL(hostURL, webSocketPort);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.IDX_PROP__DATATUBE__DATACAST_ID, dataCastId);
		props.put(InfraConstants.IDX_PROP__DATATUBE__ID, dataTubeId);
		props.put(InfraConstants.IDX_PROP__DATATUBE__WEB_SOCKET_PORT, webSocketPort);
		if (webSocketURL != null) {
			props.put(InfraConstants.IDX_PROP__DATATUBE__CHANNEL_WEB_SOCKET_URL, webSocketURL);
		}
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__NAME, name);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date());

		indexService.setProperties(getIndexProviderId(), indexItemId, props);
	}

	@Override
	public void cleanupIndex(IndexServiceClient indexService, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = indexItem.getProperties();
		List<String> propertyNames = MapHelper.INSTANCE.getKeyList(props);
		indexService.removeProperties(getIndexProviderId(), indexItemId, propertyNames);
	}

	@Override
	public void removeIndex(IndexServiceClient indexService, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		indexService.deleteIndexItem(getIndexProviderId(), indexItemId);
	}

}
