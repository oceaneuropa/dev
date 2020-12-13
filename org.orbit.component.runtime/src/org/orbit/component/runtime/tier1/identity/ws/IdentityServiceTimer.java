package org.orbit.component.runtime.tier1.identity.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.tier1.identity.service.IdentityService;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.origin.common.service.WebServiceAwareHelper;
import org.origin.common.util.MapHelper;

/**
 * Indexer for Identity service.
 * 
 */
public class IdentityServiceTimer extends ServiceIndexTimer<IdentityService> {

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public IdentityServiceTimer(IdentityService service) {
		super(ComponentConstants.IDENTITY_INDEXER_ID, "Index Timer [" + service.getName() + "]", service);
		setDebug(true);
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexService) throws IOException {
		IdentityService service = getService();

		String name = service.getName();
		return indexService.getIndexItem(getIndexProviderId(), ComponentConstants.IDENTITY_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexService) throws IOException {
		IdentityService service = getService();

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

		return indexService.addIndexItem(getIndexProviderId(), ComponentConstants.IDENTITY_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexService, IndexItem indexItem) throws IOException {
		IdentityService service = getService();

		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);

		Date now = new Date();

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);

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
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		indexProvider.removeIndexItem(getIndexProviderId(), indexItemId);
	}

}
