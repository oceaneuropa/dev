package org.orbit.os.runtime.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.os.runtime.Activator;
import org.orbit.os.runtime.OSConstants;
import org.orbit.os.runtime.service.GAIA;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;

public class GaiaIndexTimer extends ServiceIndexTimerImpl<IndexProvider, GAIA, IndexItem> implements ServiceIndexTimer<IndexProvider, GAIA, IndexItem> {

	/**
	 * 
	 * @param indexProvider
	 */
	public GaiaIndexTimer(IndexProvider indexProvider) {
		super("Index Timer [NodeOS Service]", indexProvider);
		setDebug(false);
	}

	@Override
	public GAIA getService() {
		return Activator.getInstance().getGAIA();
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, GAIA service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OSConstants.OS_INDEXER_ID, OSConstants.OS_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, GAIA service) throws IOException {
		String OSName = service.getOSName();
		String OSVersion = service.getOSVersion();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String nodeHome = service.getHome();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OSConstants.OS_PROGRAM_NAME, OSName);
		props.put(OSConstants.OS_PROGRAM_VERSION, OSVersion);
		props.put(OSConstants.OS_NAME, name);
		props.put(OSConstants.OS_HOST_URL, hostURL);
		props.put(OSConstants.OS_CONTEXT_ROOT, contextRoot);
		props.put(OSConstants.OS_HOME, nodeHome);
		props.put(OSConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(OSConstants.OS_INDEXER_ID, OSConstants.OS_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, GAIA service, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OSConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(OSConstants.OS_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OSConstants.OS_INDEXER_ID, indexItemId);
	}

}
