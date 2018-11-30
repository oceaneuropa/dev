package org.orbit.infra.runtime.datatube.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.origin.common.service.WebServiceAwareHelper;

public class DataTubeServiceIndexTimer extends ServiceIndexTimer<DataTubeService> {

	protected DataTubeService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public DataTubeServiceIndexTimer(IndexServiceClient indexProvider, DataTubeService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public DataTubeService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider, DataTubeService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(InfraConstants.IDX__DATATUBE__INDEXER_ID, InfraConstants.IDX__DATATUBE__TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider, DataTubeService service) throws IOException {
		String dataCastId = service.getDataCastId();
		String dataTubeId = service.getDataTubeId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(service);
		String webSocketHttpPort = service.getWebSocketHttpPort();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.IDX_PROP__DATATUBE__DATACAST_ID, dataCastId);
		props.put(InfraConstants.IDX_PROP__DATATUBE__ID, dataTubeId);
		props.put(InfraConstants.IDX_PROP__DATATUBE__WEB_SOCKET_HTTP_PORT, webSocketHttpPort);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__NAME, name);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date());

		return indexProvider.addIndexItem(InfraConstants.IDX__DATATUBE__INDEXER_ID, InfraConstants.IDX__DATATUBE__TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, DataTubeService service, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		String dataCastId = service.getDataCastId();
		String dataTubeId = service.getDataTubeId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(service);
		String webSocketHttpPort = service.getWebSocketHttpPort();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.IDX_PROP__DATATUBE__DATACAST_ID, dataCastId);
		props.put(InfraConstants.IDX_PROP__DATATUBE__ID, dataTubeId);
		props.put(InfraConstants.IDX_PROP__DATATUBE__WEB_SOCKET_HTTP_PORT, webSocketHttpPort);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__NAME, name);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date());

		indexProvider.setProperties(InfraConstants.IDX__DATATUBE__INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.deleteIndexItem(InfraConstants.IDX__DATATUBE__INDEXER_ID, indexItemId);
	}

}
