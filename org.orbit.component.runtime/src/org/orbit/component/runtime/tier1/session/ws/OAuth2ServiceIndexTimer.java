package org.orbit.component.runtime.tier1.session.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.tier1.session.service.OAuth2Service;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.origin.common.lang.MapHelper;
import org.origin.common.service.WebServiceAwareHelper;

public class OAuth2ServiceIndexTimer extends ServiceIndexTimer<OAuth2Service> {

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public OAuth2ServiceIndexTimer(OAuth2Service service) {
		super(ComponentConstants.OAUTH2_INDEXER_ID, "Index Timer [" + service.getName() + "]", service);
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider) throws IOException {
		OAuth2Service service = getService();

		String name = service.getName();
		return indexProvider.getIndexItem(getIndexProviderId(), ComponentConstants.OAUTH2_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider) throws IOException {
		OAuth2Service service = getService();

		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(getIndexProviderId(), ComponentConstants.OAUTH2_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		OAuth2Service service = getService();

		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date().getTime());

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
		indexProvider.deleteIndexItem(getIndexProviderId(), indexItemId);
	}

}
