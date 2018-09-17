package org.orbit.spirit.runtime.gaia.ws.other;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProviderClient;
import org.orbit.spirit.runtime.Constants;
import org.orbit.spirit.runtime.gaia.service.GaiaService;
import org.origin.common.thread.other.ServiceIndexTimerImplV1;
import org.origin.common.thread.other.ServiceIndexTimerV1;

public class NodeOSIndexTimerV1 extends ServiceIndexTimerImplV1<IndexProviderClient, GaiaService> implements ServiceIndexTimerV1<IndexProviderClient, GaiaService> {

	/**
	 * 
	 * @param indexProvider
	 */
	public NodeOSIndexTimerV1(IndexProviderClient indexProvider) {
		super("NodeOS Index Timer", indexProvider);
		setDebug(false);
	}

	@Override
	public GaiaService getService() {
		// return Activator.getInstance().getGAIA();
		return null;
	}

	@Override
	public void updateIndex(IndexProviderClient indexProvider, GaiaService nodeOS) throws IOException {
		// String OSName = nodeOS.getOSName();
		// String OSVersion = nodeOS.getOSVersion();
		String name = nodeOS.getName();
		String hostURL = nodeOS.getHostURL();
		String contextRoot = nodeOS.getContextRoot();
		// String nodeHome = nodeOS.getHome();

		IndexItem indexItem = indexProvider.getIndexItem(Constants.IDX__GAIA__INDEXER_ID, Constants.IDX__GAIA__TYPE, name);
		if (indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			// props.put(OSConstants.OS_PROGRAM_NAME, OSName);
			// props.put(OSConstants.OS_PROGRAM_VERSION, OSVersion);
			props.put(Constants.GAIA__NAME, name);
			props.put(Constants.GAIA__HOST_URL, hostURL);
			props.put(Constants.GAIA__CONTEXT_ROOT, contextRoot);
			// props.put(PlatformConstants.GAIA_HOME, nodeHome);
			props.put(Constants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.addIndexItem(Constants.IDX__GAIA__INDEXER_ID, Constants.IDX__GAIA__TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(Constants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(Constants.IDX__GAIA__INDEXER_ID, indexItemId, props);
		}
	}

}
