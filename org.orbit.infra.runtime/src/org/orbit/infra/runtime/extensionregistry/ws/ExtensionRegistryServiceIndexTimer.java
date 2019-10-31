package org.orbit.infra.runtime.extensionregistry.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionRegistryService;
import org.origin.common.lang.MapHelper;
import org.origin.common.service.WebServiceAwareHelper;

public class ExtensionRegistryServiceIndexTimer extends ServiceIndexTimer<ExtensionRegistryService> {

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public ExtensionRegistryServiceIndexTimer(ExtensionRegistryService service) {
		super(InfraConstants.EXTENSION_REGISTRY_INDEXER_ID, "ExtensionRegistry Timer [" + service.getName() + "]", service);
		setDebug(true);
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider) throws IOException {
		ExtensionRegistryService service = getService();

		String name = service.getName();
		return indexProvider.getIndexItem(getIndexProviderId(), InfraConstants.EXTENSION_REGISTRY_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider) throws IOException {
		ExtensionRegistryService service = getService();

		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);

		Date now = new Date();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);

		return indexProvider.addIndexItem(getIndexProviderId(), InfraConstants.EXTENSION_REGISTRY_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		ExtensionRegistryService service = getService();

		Integer indexItemId = indexItem.getIndexItemId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);

		Date now = new Date();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);

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
