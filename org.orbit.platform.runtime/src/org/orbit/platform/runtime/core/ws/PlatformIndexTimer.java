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

	protected String getIndexItemName(Platform platform) {
		String name = "";

		String platformParentId = platform.getParentId();
		if (platformParentId != null) {
			name += platformParentId + "_";
		}

		String platformId = platform.getId();
		name += platformId;

		return name;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, Platform platform) throws IOException {
		String idxName = getIndexItemName(platform);
		return indexProvider.getIndexItem(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_INDEXER_TYPE, idxName);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, Platform platform) throws IOException {
		String idxName = getIndexItemName(platform);

		String id = platform.getId();
		String name = platform.getName();
		String version = platform.getVersion();
		String parentId = platform.getParentId();
		String type = platform.getType();
		String hostURL = platform.getHostURL();
		String contextRoot = platform.getContextRoot();
		String home = platform.getHome();
		String runtimeState = platform.getRuntimeState().toString();

		Map<String, Object> properties = new Hashtable<String, Object>();
		properties.put(PlatformConstants.PLATFORM_ID, id);
		properties.put(PlatformConstants.PLATFORM_NAME, name);
		properties.put(PlatformConstants.PLATFORM_VERSION, version);
		if (parentId != null) {
			properties.put(PlatformConstants.PLATFORM_PARENT_ID, parentId);
		}
		if (type != null) {
			properties.put(PlatformConstants.PLATFORM_TYPE, type);
		}
		properties.put(PlatformConstants.PLATFORM_HOST_URL, hostURL);
		properties.put(PlatformConstants.PLATFORM_CONTEXT_ROOT, contextRoot);
		properties.put(PlatformConstants.PLATFORM_HOME, home);
		properties.put(PlatformConstants.PLATFORM_RUNTIME_STATE, runtimeState);
		properties.put(IndexItem.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_INDEXER_TYPE, idxName, properties);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, Platform platform, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		String id = platform.getId();
		String name = platform.getName();
		String version = platform.getVersion();
		String parentId = platform.getParentId();
		String type = platform.getType();
		String hostURL = platform.getHostURL();
		String contextRoot = platform.getContextRoot();
		String home = platform.getHome();
		String runtimeState = platform.getRuntimeState().toString();

		Map<String, Object> properties = new Hashtable<String, Object>();
		properties.put(PlatformConstants.PLATFORM_ID, id);
		properties.put(PlatformConstants.PLATFORM_NAME, name);
		properties.put(PlatformConstants.PLATFORM_VERSION, version);
		if (parentId != null) {
			properties.put(PlatformConstants.PLATFORM_PARENT_ID, parentId);
		}
		if (type != null) {
			properties.put(PlatformConstants.PLATFORM_TYPE, type);
		}
		properties.put(PlatformConstants.PLATFORM_HOST_URL, hostURL);
		properties.put(PlatformConstants.PLATFORM_CONTEXT_ROOT, contextRoot);
		properties.put(PlatformConstants.PLATFORM_HOME, home);
		properties.put(PlatformConstants.PLATFORM_RUNTIME_STATE, runtimeState);
		properties.put(IndexItem.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(PlatformConstants.PLATFORM_INDEXER_ID, indexItemId, properties);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(PlatformConstants.PLATFORM_INDEXER_ID, indexItemId);
	}

}
