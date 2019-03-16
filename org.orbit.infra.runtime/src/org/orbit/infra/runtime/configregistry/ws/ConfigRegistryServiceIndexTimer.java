package org.orbit.infra.runtime.configregistry.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.origin.common.lang.MapHelper;
import org.origin.common.service.WebServiceAwareHelper;

public class ConfigRegistryServiceIndexTimer extends ServiceIndexTimer<ConfigRegistryService> {

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public ConfigRegistryServiceIndexTimer(ConfigRegistryService service) {
		super(InfraConstants.IDX__CONFIG_REGISTRY__INDEXER_ID, "Index Timer [" + service.getName() + "]", service);
		setDebug(true);
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider) throws IOException {
		ConfigRegistryService service = getService();

		String name = service.getName();
		return indexProvider.getIndexItem(getIndexProviderId(), InfraConstants.IDX__CONFIG_REGISTRY__TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexService) throws IOException {
		ConfigRegistryService service = getService();

		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__NAME, name);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(org.orbit.infra.api.InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date());

		return indexService.addIndexItem(getIndexProviderId(), InfraConstants.IDX__CONFIG_REGISTRY__TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexService, IndexItem indexItem) throws IOException {
		ConfigRegistryService service = getService();

		Integer indexItemId = indexItem.getIndexItemId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);

		Map<String, Object> props = new Hashtable<String, Object>();
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
