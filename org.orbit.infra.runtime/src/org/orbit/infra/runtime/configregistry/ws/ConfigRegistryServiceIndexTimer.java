package org.orbit.infra.runtime.configregistry.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.origin.common.service.WebServiceAwareHelper;

public class ConfigRegistryServiceIndexTimer extends ServiceIndexTimer<ConfigRegistryService> {

	protected ConfigRegistryService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public ConfigRegistryServiceIndexTimer(IndexServiceClient indexProvider, ConfigRegistryService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public ConfigRegistryService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider, ConfigRegistryService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(InfraConstants.IDX__CONFIG_REGISTRY__INDEXER_ID, InfraConstants.IDX__CONFIG_REGISTRY__TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexService, ConfigRegistryService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String url = WebServiceAwareHelper.INSTANCE.getURL(service);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.IDX_PROP__CONFIG_REGISTRY__NAME, name);
		props.put(InfraConstants.IDX_PROP__CONFIG_REGISTRY__HOST_URL, hostURL);
		props.put(InfraConstants.IDX_PROP__CONFIG_REGISTRY__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.IDX_PROP__CONFIG_REGISTRY__BASE_URL, url);
		props.put(InfraConstants.LAST_HEARTBEAT_TIME, new Date());

		return indexService.addIndexItem(InfraConstants.IDX__CONFIG_REGISTRY__INDEXER_ID, InfraConstants.IDX__CONFIG_REGISTRY__TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexService, ConfigRegistryService service, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String url = WebServiceAwareHelper.INSTANCE.getURL(service);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.IDX_PROP__CONFIG_REGISTRY__NAME, name);
		props.put(InfraConstants.IDX_PROP__CONFIG_REGISTRY__HOST_URL, hostURL);
		props.put(InfraConstants.IDX_PROP__CONFIG_REGISTRY__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.IDX_PROP__CONFIG_REGISTRY__BASE_URL, url);
		props.put(InfraConstants.LAST_HEARTBEAT_TIME, new Date());

		indexService.setProperties(InfraConstants.IDX__CONFIG_REGISTRY__INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexServiceClient indexService, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexService.deleteIndexItem(InfraConstants.IDX__CONFIG_REGISTRY__INDEXER_ID, indexItemId);
	}

}

// String dataCastId = service.getDataCastId();
// props.put(InfraConstants.IDX_PROP__CONFIG_REGISTRY__ID, dataCastId);
// String dataCastId = service.getDataCastId();
// props.put(InfraConstants.IDX_PROP__CONFIG_REGISTRY__ID, dataCastId);
