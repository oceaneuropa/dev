package org.orbit.platform.runtime.gaia.ws.other;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.platform.runtime.Activator;
import org.orbit.platform.runtime.OSConstants;
import org.orbit.platform.runtime.gaia.service.GAIA;
import org.origin.common.thread.other.ServiceIndexTimerImplV1;
import org.origin.common.thread.other.ServiceIndexTimerV1;

public class NodeOSIndexTimerV1 extends ServiceIndexTimerImplV1<IndexProvider, GAIA> implements ServiceIndexTimerV1<IndexProvider, GAIA> {

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
		return Activator.getInstance().getGAIA();
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, GAIA nodeOS) throws IOException {
		// String OSName = nodeOS.getOSName();
		// String OSVersion = nodeOS.getOSVersion();
		String name = nodeOS.getName();
		String hostURL = nodeOS.getHostURL();
		String contextRoot = nodeOS.getContextRoot();
		String nodeHome = nodeOS.getHome();

		IndexItem indexItem = indexProvider.getIndexItem(OSConstants.GAIA_INDEXER_ID, OSConstants.GAIA_TYPE, name);
		if (indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			// props.put(OSConstants.OS_PROGRAM_NAME, OSName);
			// props.put(OSConstants.OS_PROGRAM_VERSION, OSVersion);
			props.put(OSConstants.GAIA_NAME, name);
			props.put(OSConstants.GAIA_HOST_URL, hostURL);
			props.put(OSConstants.GAIA_CONTEXT_ROOT, contextRoot);
			props.put(OSConstants.GAIA_HOME, nodeHome);
			props.put(OSConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.addIndexItem(OSConstants.GAIA_INDEXER_ID, OSConstants.GAIA_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OSConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(OSConstants.GAIA_INDEXER_ID, indexItemId, props);
		}
	}

}
