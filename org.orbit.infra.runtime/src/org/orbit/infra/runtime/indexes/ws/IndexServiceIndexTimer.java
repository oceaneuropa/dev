package org.orbit.infra.runtime.indexes.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.orbit.platform.sdk.IPlatform;
import org.orbit.platform.sdk.PlatformConstants;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.origin.common.lang.MapHelper;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.WebServiceAwareHelper;
import org.origin.common.thread.IndexTimer;
import org.origin.common.thread.IndexTimerImpl;

public class IndexServiceIndexTimer extends IndexTimerImpl<IndexService, IndexService, IndexItem> implements IndexTimer<IndexService, IndexService, IndexItem> {

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public IndexServiceIndexTimer(IndexService service) {
		super(InfraConstants.INDEX_SERVICE_INDEXER_ID, "Index Timer [" + service.getName() + "]", service);
		this.service = service;
		setDebug(true);
	}

	@Override
	public IndexService getIndexService() {
		return getService();
	}

	protected String getPlatformId() {
		String platformId = null;
		if (PlatformSDKActivator.getInstance() != null) {
			IPlatform platform = PlatformSDKActivator.getInstance().getPlatform();
			if (platform != null) {
				platformId = platform.getId();
			}
		}
		return platformId;
	}

	@Override
	public IndexItem getIndex(IndexService indexService) throws IOException {
		try {
			IndexService service = getService();

			String name = service.getName();
			return indexService.getIndexItem(getIndexProviderId(), InfraConstants.INDEX_SERVICE_TYPE, name);
		} catch (ServerException e) {
			throw new IOException(e);
		}
	}

	@Override
	public IndexItem addIndex(IndexService indexService) throws IOException {
		try {
			IndexService service = getService();

			String name = service.getName();
			String baseURL = WebServiceAwareHelper.INSTANCE.getURL(service);
			String hostURL = service.getHostURL();
			String contextRoot = service.getContextRoot();

			Map<String, Object> props = new Hashtable<String, Object>();
			String platformId = getPlatformId();
			if (platformId != null && props != null) {
				props.put(PlatformConstants.IDX_PROP__PLATFORM_ID, platformId);
			}
			props.put(org.orbit.infra.api.InfraConstants.SERVICE__NAME, name);
			props.put(org.orbit.infra.api.InfraConstants.SERVICE__BASE_URL, baseURL);
			props.put(org.orbit.infra.api.InfraConstants.SERVICE__HOST_URL, hostURL);
			props.put(org.orbit.infra.api.InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
			props.put(org.orbit.infra.api.InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date().getTime());
			return indexService.addIndexItem(getIndexProviderId(), InfraConstants.INDEX_SERVICE_TYPE, name, props);

		} catch (ServerException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void updateIndex(IndexService indexService, IndexItem indexItem) throws IOException {
		try {
			IndexService service = getService();

			Integer indexItemId = indexItem.getIndexItemId();
			String name = service.getName();
			String url = WebServiceAwareHelper.INSTANCE.getURL(service);
			// String hostURL = service.getHostURL();
			// String contextRoot = service.getContextRoot();

			Map<String, Object> properties = new Hashtable<String, Object>();
			String platformId = getPlatformId();
			if (platformId != null && properties != null) {
				properties.put(PlatformConstants.IDX_PROP__PLATFORM_ID, platformId);
			}
			properties.put(org.orbit.infra.api.InfraConstants.SERVICE__NAME, name);
			properties.put(org.orbit.infra.api.InfraConstants.SERVICE__BASE_URL, url);
			properties.put(org.orbit.infra.api.InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date().getTime());
			// props.put(InfraConstants.INDEX_SERVICE_HOST_URL, hostURL);
			// props.put(InfraConstants.INDEX_SERVICE_CONTEXT_ROOT, contextRoot);
			indexService.setProperties(getIndexProviderId(), indexItemId, properties);

			// List<String> propNames = new ArrayList<String>();
			// propNames.add(InfraConstants.URL);
			// propNames.add(InfraConstants.INDEX_SERVICE_NAME);
			// propNames.add(InfraConstants.INDEX_SERVICE_HOST_URL);
			// propNames.add(InfraConstants.INDEX_SERVICE_CONTEXT_ROOT);
			// indexProvider.removeProperty(InfraConstants.INDEX_SERVICE_INDEXER_ID, indexItemId, propNames);

		} catch (ServerException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void cleanupIndex(IndexService indexService, IndexItem indexItem) throws IOException {
		try {
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = indexItem.getProperties();
			List<String> propertyNames = MapHelper.INSTANCE.getKeyList(props);
			indexService.removeProperties(getIndexProviderId(), indexItemId, propertyNames);
		} catch (ServerException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void removeIndex(IndexService indexProvider, IndexItem indexItem) throws IOException {
		try {
			Integer indexItemId = indexItem.getIndexItemId();
			indexProvider.removeIndexItem(getIndexProviderId(), indexItemId);
		} catch (ServerException e) {
			throw new IOException(e);
		}
	}

}
