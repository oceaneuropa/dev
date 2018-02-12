package org.orbit.platform.runtime.platform.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.platform.runtime.PlatformConstants;
import org.orbit.platform.runtime.platform.Platform;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;

public class PlatformIndexTimer extends ServiceIndexTimerImpl<IndexProvider, Platform, IndexItem> implements ServiceIndexTimer<IndexProvider, Platform, IndexItem> {

	protected Platform platform;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public PlatformIndexTimer(IndexProvider indexProvider, Platform service) {
		super("Index Timer [" + service.getName() + "(" + service.getVersion() + ")]", indexProvider);
		setDebug(false);
		this.platform = service;
	}

	@Override
	public Platform getService() {
		return this.platform;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, Platform platform) throws IOException {
		String name = platform.getName();

		return indexProvider.getIndexItem(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, Platform platform) throws IOException {
		String name = platform.getName();
		String version = platform.getVersion();
		String hostURL = platform.getHostURL();
		String contextRoot = platform.getContextRoot();
		String nodeHome = platform.getHome();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(PlatformConstants.PLATFORM_NAME, name);
		props.put(PlatformConstants.PLATFORM_VERSION, version);
		props.put(PlatformConstants.PLATFORM_HOST_URL, hostURL);
		props.put(PlatformConstants.PLATFORM_CONTEXT_ROOT, contextRoot);
		props.put(PlatformConstants.PLATFORM_HOME, nodeHome);
		props.put(PlatformConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, Platform platform, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(PlatformConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(PlatformConstants.PLATFORM_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(PlatformConstants.PLATFORM_INDEXER_ID, indexItemId);
	}

}