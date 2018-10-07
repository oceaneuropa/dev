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
		String name = service.getName();
		String url = WebServiceAwareHelper.INSTANCE.getURL(service);
		// String namespace = service.getNamespace();
		// String hostURL = service.getHostURL();
		// String contextRoot = service.getContextRoot();

		Date now = new Date();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.NAME, name);
		props.put(InfraConstants.BASE_URL, url);
		props.put(InfraConstants.LAST_HEARTBEAT_TIME, now);
		// props.put(InfraConstants.CHANNEL_NAMESPACE, namespace);
		// props.put(InfraConstants.CHANNEL_HOST_URL, hostURL);
		// props.put(InfraConstants.CHANNEL_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexService.addIndexItem(InfraConstants.IDX__DATATUBE__INDEXER_ID, InfraConstants.IDX__DATATUBE__TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexService, DataCastService service, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		String name = service.getName();
		String url = WebServiceAwareHelper.INSTANCE.getURL(service);
		// String namespace = service.getNamespace();
		// String hostURL = service.getHostURL();
		// String contextRoot = service.getContextRoot();

		Date now = new Date();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.NAME, name);
		props.put(InfraConstants.BASE_URL, url);
		props.put(InfraConstants.LAST_HEARTBEAT_TIME, now);
		// props.put(InfraConstants.CHANNEL_NAMESPACE, namespace);
		// props.put(InfraConstants.CHANNEL_HOST_URL, hostURL);
		// props.put(InfraConstants.CHANNEL_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexService.setProperties(InfraConstants.IDX__DATATUBE__INDEXER_ID, indexItemId, props);

		// List<String> propNames = new ArrayList<String>();
		// propNames.add(InfraConstants.URL);
		// propNames.add(InfraConstants.CHANNEL_NAME);
		// propNames.add(InfraConstants.CHANNEL_HOST_URL);
		// propNames.add(InfraConstants.CHANNEL_CONTEXT_ROOT);
		// indexProvider.removeProperties(InfraConstants.CHANNEL_INDEXER_ID, indexItemId, propNames);
	}

	@Override
	public void removeIndex(IndexServiceClient indexService, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexService.deleteIndexItem(InfraConstants.IDX__DATATUBE__INDEXER_ID, indexItemId);
	}

}
