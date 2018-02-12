package org.orbit.platform.runtime.gaia.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.platform.runtime.PlatformConstants;
import org.orbit.platform.runtime.gaia.service.GAIA;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;

public class GaiaIndexTimer extends ServiceIndexTimerImpl<IndexProvider, GAIA, IndexItem> implements ServiceIndexTimer<IndexProvider, GAIA, IndexItem> {

	protected GAIA gaia;

	/**
	 * 
	 * @param indexProvider
	 * @param gaia
	 */
	public GaiaIndexTimer(IndexProvider indexProvider, GAIA gaia) {
		super("Index Timer [" + gaia.getName() + "]", indexProvider);
		setDebug(false);
		this.gaia = gaia;
	}

	@Override
	public GAIA getService() {
		return this.gaia;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, GAIA gaia) throws IOException {
		String name = gaia.getName();

		return indexProvider.getIndexItem(PlatformConstants.GAIA_INDEXER_ID, PlatformConstants.GAIA_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, GAIA gaia) throws IOException {
		// String OSName = gaia.getOSName();
		// String OSVersion = gaia.getOSVersion();
		String name = gaia.getName();
		String hostURL = gaia.getHostURL();
		String contextRoot = gaia.getContextRoot();
		String nodeHome = gaia.getHome();

		Map<String, Object> props = new Hashtable<String, Object>();
		// props.put(OSConstants.OS_PROGRAM_NAME, OSName);
		// props.put(OSConstants.OS_PROGRAM_VERSION, OSVersion);
		props.put(PlatformConstants.GAIA_NAME, name);
		props.put(PlatformConstants.GAIA_HOST_URL, hostURL);
		props.put(PlatformConstants.GAIA_CONTEXT_ROOT, contextRoot);
		// props.put(PlatformConstants.GAIA_HOME, nodeHome);
		props.put(PlatformConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(PlatformConstants.GAIA_INDEXER_ID, PlatformConstants.GAIA_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, GAIA gaia, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(PlatformConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(PlatformConstants.GAIA_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(PlatformConstants.GAIA_INDEXER_ID, indexItemId);
	}

}
