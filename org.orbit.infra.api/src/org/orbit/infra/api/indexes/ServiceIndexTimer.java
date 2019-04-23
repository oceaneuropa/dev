package org.orbit.infra.api.indexes;

import java.util.Map;

import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.platform.sdk.IPlatform;
import org.orbit.platform.sdk.PlatformConstants;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.origin.common.service.AccessTokenAware;
import org.origin.common.thread.IndexTimerImpl;

public abstract class ServiceIndexTimer<RUNTIME_SERVICE> extends IndexTimerImpl<IndexServiceClient, RUNTIME_SERVICE, IndexItem> {

	public static class IndexItemUpdaterImpl implements IndexItemUpdater {
		@Override
		public void addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) {
			String platformId = getPlatformId();
			if (platformId != null && properties != null && !properties.containsKey(PlatformConstants.IDX_PROP__PLATFORM_ID)) {
				properties.put(PlatformConstants.IDX_PROP__PLATFORM_ID, platformId);
			}
		}

		@Override
		public void setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) {
			String platformId = getPlatformId();
			if (platformId != null && properties != null && !properties.containsKey(PlatformConstants.IDX_PROP__PLATFORM_ID)) {
				properties.put(PlatformConstants.IDX_PROP__PLATFORM_ID, platformId);
			}
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
	}

	protected IndexItemUpdater indexItemUpdater = new IndexItemUpdaterImpl();
	protected IndexServiceClient indexService;

	/**
	 * 
	 * @param indexProviderId
	 * @param name
	 * @param service
	 */
	public ServiceIndexTimer(String indexProviderId, String name, RUNTIME_SERVICE service) {
		super(indexProviderId, name, service);
	}

	@Override
	public synchronized IndexServiceClient getIndexService() {
		if (this.indexService != null) {
			return this.indexService;
		}

		String accessToken = null;
		RUNTIME_SERVICE service = getService();
		if (service instanceof AccessTokenAware) {
			accessToken = ((AccessTokenAware) service).getAccessToken();
		}

		IndexServiceClient indexService = InfraClientsUtil.INDEX_SERVICE.getIndexServiceClient(accessToken);
		if (indexService != null) {
			indexService.addIndexItemUpdater(this.indexItemUpdater);
		}
		this.indexService = indexService;
		return indexService;
	}

}

// public static class IndexServiceClientInterceptorImpl extends IndexServiceClientInterceptor {
//
// public IndexServiceClientInterceptorImpl() {
// }
//
// protected String getPlatformId() {
// String platformId = null;
// if (PlatformSDKActivator.getInstance() != null) {
// IPlatform platform = PlatformSDKActivator.getInstance().getPlatform();
// if (platform != null) {
// platformId = platform.getId();
// }
// }
// return platformId;
// }
//
// @Override
// public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException {
// String platformId = getPlatformId();
// if (platformId != null && properties != null && !properties.containsKey(PlatformConstants.IDX_PROP__PLATFORM_ID)) {
// properties.put(PlatformConstants.IDX_PROP__PLATFORM_ID, platformId);
// }
// return super.addIndexItem(indexProviderId, type, name, properties);
// }
//
// @Override
// public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws IOException {
// String platformId = getPlatformId();
// if (platformId != null && properties != null && !properties.containsKey(PlatformConstants.IDX_PROP__PLATFORM_ID)) {
// properties.put(PlatformConstants.IDX_PROP__PLATFORM_ID, platformId);
// }
// return super.setProperties(indexProviderId, indexItemId, properties);
// }
// }

// automatically set platformId
// protected IndexServiceClientInterceptor indexProviderWithInterceptor = new IndexServiceClientInterceptorImpl();

// @Override
// public synchronized void setIndexService(IndexServiceClient indexService) {
// this.indexService = indexService;
// }

// @Override
// protected synchronized void performIndexing() {
// if (getIndexService() != null) {
// try {
// String time1 = String.valueOf(System.currentTimeMillis());
// String time2 = getIndexService().echo(time1);
// if (time1.equals(time2)) {
// super.performIndexing();
// }
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
// }
