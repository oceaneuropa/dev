package org.orbit.infra.runtime.indexes.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.orbit.platform.sdk.IPlatform;
import org.orbit.platform.sdk.PlatformConstants;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.WebServiceAwareHelper;
import org.origin.common.thread.IndexTimer;
import org.origin.common.thread.IndexTimerImpl;

public class IndexServiceIndexTimer extends IndexTimerImpl<IndexService, IndexService, IndexItem> implements IndexTimer<IndexService, IndexService, IndexItem> {

	protected IndexService service;

	/**
	 * 
	 * @param indexProvider
	 */
	public IndexServiceIndexTimer(IndexService indexProvider, IndexService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public IndexService getService() {
		return this.service;
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
	public IndexItem getIndex(IndexService indexProvider, IndexService service) throws IOException {
		try {
			String name = service.getName();

			return indexProvider.getIndexItem(InfraConstants.INDEX_SERVICE_INDEXER_ID, InfraConstants.INDEX_SERVICE_TYPE, name);

		} catch (ServerException e) {
			throw new IOException(e);
		}
	}

	@Override
	public IndexItem addIndex(IndexService indexProvider, IndexService service) throws IOException {
		try {
			String name = service.getName();
			String url = WebServiceAwareHelper.INSTANCE.getURL(service);
			// String hostURL = service.getHostURL();
			// String contextRoot = service.getContextRoot();

			Map<String, Object> properties = new Hashtable<String, Object>();
			String platformId = getPlatformId();
			if (platformId != null && properties != null) {
				properties.put(PlatformConstants.PLATFORM_ID, platformId);
			}
			properties.put(InfraConstants.NAME, name);
			properties.put(InfraConstants.BASE_URL, url);
			// props.put(InfraConstants.INDEX_SERVICE_HOST_URL, hostURL);
			// props.put(InfraConstants.INDEX_SERVICE_CONTEXT_ROOT, contextRoot);
			properties.put(InfraConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
			return indexProvider.addIndexItem(InfraConstants.INDEX_SERVICE_INDEXER_ID, InfraConstants.INDEX_SERVICE_TYPE, name, properties);

		} catch (ServerException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void updateIndex(IndexService indexProvider, IndexService service, IndexItem indexItem) throws IOException {
		try {
			Integer indexItemId = indexItem.getIndexItemId();
			String name = service.getName();
			String url = WebServiceAwareHelper.INSTANCE.getURL(service);
			// String hostURL = service.getHostURL();
			// String contextRoot = service.getContextRoot();

			Map<String, Object> properties = new Hashtable<String, Object>();
			String platformId = getPlatformId();
			if (platformId != null && properties != null) {
				properties.put(PlatformConstants.PLATFORM_ID, platformId);
			}
			properties.put(InfraConstants.NAME, name);
			properties.put(InfraConstants.BASE_URL, url);
			properties.put(InfraConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
			// props.put(InfraConstants.INDEX_SERVICE_HOST_URL, hostURL);
			// props.put(InfraConstants.INDEX_SERVICE_CONTEXT_ROOT, contextRoot);
			indexProvider.setProperties(InfraConstants.INDEX_SERVICE_INDEXER_ID, indexItemId, properties);

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
	public void removeIndex(IndexService indexProvider, IndexItem indexItem) throws IOException {
		try {
			Integer indexItemId = indexItem.getIndexItemId();

			indexProvider.removeIndexItem(InfraConstants.INDEX_SERVICE_INDEXER_ID, indexItemId);

		} catch (ServerException e) {
			throw new IOException(e);
		}
	}

}
