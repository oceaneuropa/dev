package org.orbit.platform.runtime.core.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.platform.runtime.PlatformConstants;
import org.orbit.platform.runtime.core.Platform;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;

public class PlatformIndexTimer extends ServiceIndexTimerImpl<IndexProvider, Platform, IndexItem> implements ServiceIndexTimer<IndexProvider, Platform, IndexItem> {

	protected Platform platform;

	/**
	 * 
	 * @param indexProvider
	 * @param platform
	 */
	public PlatformIndexTimer(IndexProvider indexProvider, Platform platform) {
		super("Index Timer [" + platform.getName() + "(" + platform.getVersion() + ")]", indexProvider);
		setDebug(false);
		this.platform = platform;
	}

	@Override
	public Platform getService() {
		return this.platform;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, Platform platform) throws IOException {
		String id = platform.getId();
		return indexProvider.getIndexItem(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_TYPE, id);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, Platform platform) throws IOException {
		String id = platform.getId();
		String name = platform.getName();
		String version = platform.getVersion();
		String hostURL = platform.getHostURL();
		String contextRoot = platform.getContextRoot();
		String home = platform.getHome();

		Map<String, Object> properties = new Hashtable<String, Object>();
		properties.put(PlatformConstants.PLATFORM_ID, id);
		properties.put(PlatformConstants.PLATFORM_NAME, name);
		properties.put(PlatformConstants.PLATFORM_VERSION, version);
		properties.put(PlatformConstants.PLATFORM_HOST_URL, hostURL);
		properties.put(PlatformConstants.PLATFORM_CONTEXT_ROOT, contextRoot);
		properties.put(PlatformConstants.PLATFORM_HOME, home);
		properties.put(PlatformConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_TYPE, id, properties);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, Platform platform, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		String id = platform.getId();
		String name = platform.getName();
		String version = platform.getVersion();
		String hostURL = platform.getHostURL();
		String contextRoot = platform.getContextRoot();
		String home = platform.getHome();

		Map<String, Object> properties = new Hashtable<String, Object>();
		properties.put(PlatformConstants.PLATFORM_ID, id);
		properties.put(PlatformConstants.PLATFORM_NAME, name);
		properties.put(PlatformConstants.PLATFORM_VERSION, version);
		properties.put(PlatformConstants.PLATFORM_HOST_URL, hostURL);
		properties.put(PlatformConstants.PLATFORM_CONTEXT_ROOT, contextRoot);
		properties.put(PlatformConstants.PLATFORM_HOME, home);
		properties.put(PlatformConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(PlatformConstants.PLATFORM_INDEXER_ID, indexItemId, properties);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(PlatformConstants.PLATFORM_INDEXER_ID, indexItemId);
	}

}
