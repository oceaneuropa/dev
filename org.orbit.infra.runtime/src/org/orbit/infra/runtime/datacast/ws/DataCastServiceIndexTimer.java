package org.orbit.infra.runtime.datacast.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.origin.common.service.WebServiceAwareHelper;

public class DataCastServiceIndexTimer extends ServiceIndexTimer<DataCastService> {

	protected DataCastService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public DataCastServiceIndexTimer(IndexServiceClient indexProvider, DataCastService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public DataCastService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider, DataCastService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(InfraConstants.IDX__DATACAST__INDEXER_ID, InfraConstants.IDX__DATACAST__TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexService, DataCastService service) throws IOException {
		String dataCastId = service.getDataCastId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(service);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.IDX_PROP__DATACAST__ID, dataCastId);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__NAME, name);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date());

		return indexService.addIndexItem(InfraConstants.IDX__DATACAST__INDEXER_ID, InfraConstants.IDX__DATACAST__TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexService, DataCastService service, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		String dataCastId = service.getDataCastId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(service);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.IDX_PROP__DATACAST__ID, dataCastId);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__NAME, name);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date());

		indexService.setProperties(InfraConstants.IDX__DATACAST__INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexServiceClient indexService, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexService.deleteIndexItem(InfraConstants.IDX__DATACAST__INDEXER_ID, indexItemId);
	}

}
