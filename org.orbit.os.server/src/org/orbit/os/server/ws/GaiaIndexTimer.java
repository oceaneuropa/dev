package org.orbit.os.server.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.os.server.Activator;
import org.orbit.os.server.Constants;
import org.orbit.os.server.service.GAIA;
import org.origin.common.thread.ServiceIndexTimerImplV2;
import org.origin.common.thread.ServiceIndexTimerV2;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

public class GaiaIndexTimer extends ServiceIndexTimerImplV2<IndexProvider, GAIA, IndexItem> implements ServiceIndexTimerV2<IndexProvider, GAIA, IndexItem> {

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
		return Activator.getNodeOS();
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, GAIA service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(Constants.NODE_INDEXER_ID, Constants.NODE_TYPE, name);
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
		props.put(Constants.NODE_OS_NAME, OSName);
		props.put(Constants.NODE_OS_VERSION, OSVersion);
		props.put(Constants.NODE_NAME, name);
		props.put(Constants.NODE_HOST_URL, hostURL);
		props.put(Constants.NODE_CONTEXT_ROOT, contextRoot);
		props.put(Constants.NODE_HOME, nodeHome);
		props.put(Constants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(Constants.NODE_INDEXER_ID, Constants.NODE_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, GAIA service, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(Constants.NODE_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(Constants.NODE_INDEXER_ID, indexItemId);
	}

}
