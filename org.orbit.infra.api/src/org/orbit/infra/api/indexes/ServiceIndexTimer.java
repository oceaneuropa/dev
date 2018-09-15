package org.orbit.infra.api.indexes;

import java.io.IOException;
import java.util.Map;

import org.orbit.platform.sdk.IPlatform;
import org.orbit.platform.sdk.PlatformConstants;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.origin.common.thread.IndexTimerImpl;

public abstract class ServiceIndexTimer<SERVICE> extends IndexTimerImpl<IndexServiceClient, SERVICE, IndexItem> {

	public static class MyIndexServiceProxy extends IndexServiceClientWrapper {
		public MyIndexServiceProxy() {
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
		public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException {
			String platformId = getPlatformId();
			if (platformId != null && properties != null && !properties.containsKey(PlatformConstants.PLATFORM_ID)) {
				properties.put(PlatformConstants.PLATFORM_ID, platformId);
			}
			return super.addIndexItem(indexProviderId, type, name, properties);
		}

		@Override
		public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws IOException {
			String platformId = getPlatformId();
			if (platformId != null && properties != null && !properties.containsKey(PlatformConstants.PLATFORM_ID)) {
				properties.put(PlatformConstants.PLATFORM_ID, platformId);
			}
			return super.setProperties(indexProviderId, indexItemId, properties);
		}
	}

	// automatically set platformId
	protected IndexServiceClientWrapper indexProviderWrapper = new MyIndexServiceProxy();

	/**
	 * 
	 * @param name
	 * @param indexProvider
	 */
	public ServiceIndexTimer(String name, IndexServiceClient indexProvider) {
		super(name, indexProvider);
	}

	@Override
	public synchronized IndexServiceClient getIndexProvider() {
		if (this.indexProviderWrapper.get() != this.indexProvider) {
			this.indexProviderWrapper.set(this.indexProvider);
		}
		return this.indexProviderWrapper;
	}

	@Override
	public synchronized void setIndexProvider(IndexServiceClient indexProvider) {
		this.indexProvider = indexProvider;
	}

	@Override
	protected synchronized void performIndexing() {
		if (getIndexProvider() != null) {
			try {
				String time1 = String.valueOf(System.currentTimeMillis());
				String time2 = getIndexProvider().echo(time1);
				if (time1.equals(time2)) {
					super.performIndexing();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

// public static class MyIndexProviderWrapper extends IndexProviderClientWrapper {
// public MyIndexProviderWrapper() {
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
// if (platformId != null && properties != null && !properties.containsKey(PlatformConstants.PLATFORM_ID)) {
// properties.put(PlatformConstants.PLATFORM_ID, platformId);
// }
// return super.addIndexItem(indexProviderId, type, name, properties);
// }
//
// @Override
// public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws IOException {
// String platformId = getPlatformId();
// if (platformId != null && properties != null && !properties.containsKey(PlatformConstants.PLATFORM_ID)) {
// properties.put(PlatformConstants.PLATFORM_ID, platformId);
// }
// return super.setProperties(indexProviderId, indexItemId, properties);
// }
// }
