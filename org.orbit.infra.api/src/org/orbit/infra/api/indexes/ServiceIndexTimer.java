package org.orbit.infra.api.indexes;

import java.io.IOException;
import java.util.Map;

import org.orbit.platform.sdk.Activator;
import org.orbit.platform.sdk.IPlatform;
import org.orbit.platform.sdk.PlatformConstants;
import org.origin.common.thread.IndexTimerImpl;

public abstract class ServiceIndexTimer<SERVICE> extends IndexTimerImpl<IndexProvider, SERVICE, IndexItem> {

	public static class MyIndexProviderWrapper extends IndexProviderWrapper {
		public MyIndexProviderWrapper() {
		}

		protected String getPlatformId() {
			String platformId = null;
			if (Activator.getInstance() != null) {
				IPlatform platform = Activator.getInstance().getPlatform();
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

	protected IndexProviderWrapper indexProviderWrapper;

	/**
	 * 
	 * @param name
	 * @param indexProvider
	 */
	public ServiceIndexTimer(String name, IndexProvider indexProvider) {
		super(name, indexProvider);
	}

	@Override
	public synchronized IndexProvider getIndexProvider() {
		return getIndexProviderWrapper(this.indexProvider);
	}

	@Override
	public synchronized void setIndexProvider(IndexProvider indexProvider) {
		this.indexProvider = indexProvider;
		getIndexProviderWrapper(this.indexProvider);
	}

	/**
	 * 
	 * @param indexProvider
	 * @return
	 */
	protected synchronized IndexProviderWrapper getIndexProviderWrapper(IndexProvider indexProvider) {
		if (this.indexProviderWrapper == null) {
			this.indexProviderWrapper = new MyIndexProviderWrapper();
		}
		if (this.indexProviderWrapper.get() != indexProvider) {
			this.indexProviderWrapper.set(indexProvider);
		}
		return this.indexProviderWrapper;
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

// public MyIndexProviderWrapper(IndexProvider indexProvider) {
// super(indexProvider);
// }
