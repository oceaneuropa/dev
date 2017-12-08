package org.orbit.os.server.ws.other;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.os.server.Activator;
import org.orbit.os.server.Constants;
import org.orbit.os.server.service.GAIA;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

public class NodeOSIndexTimerV1 extends ServiceIndexTimerImpl<IndexProvider, GAIA> implements ServiceIndexTimer<IndexProvider, GAIA> {

	/**
	 * 
	 * @param indexProvider
	 */
	public NodeOSIndexTimerV1(IndexProvider indexProvider) {
		super("NodeOS Index Timer", indexProvider);
		setDebug(false);
	}

	@Override
	public GAIA getService() {
		return Activator.getNodeOS();
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, GAIA nodeOS) throws IOException {
		String OSName = nodeOS.getOSName();
		String OSVersion = nodeOS.getOSVersion();
		String name = nodeOS.getName();
		String hostURL = nodeOS.getHostURL();
		String contextRoot = nodeOS.getContextRoot();
		String nodeHome = nodeOS.getHome();

		IndexItem indexItem = indexProvider.getIndexItem(Constants.NODE_INDEXER_ID, Constants.NODE_TYPE, name);
		if (indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(Constants.NODE_OS_NAME, OSName);
			props.put(Constants.NODE_OS_VERSION, OSVersion);
			props.put(Constants.NODE_NAME, name);
			props.put(Constants.NODE_HOST_URL, hostURL);
			props.put(Constants.NODE_CONTEXT_ROOT, contextRoot);
			props.put(Constants.NODE_HOME, nodeHome);
			props.put(Constants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.addIndexItem(Constants.NODE_INDEXER_ID, Constants.NODE_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(Constants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(Constants.NODE_INDEXER_ID, indexItemId, props);
		}
	}

}
