package org.orbit.spirit.runtime.gaia.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.spirit.runtime.Constants;
import org.orbit.spirit.runtime.gaia.service.GaiaService;

public class GaiaIndexTimer extends ServiceIndexTimer<GaiaService> {

	protected GaiaService gaia;

	/**
	 * 
	 * @param indexProvider
	 * @param gaia
	 */
	public GaiaIndexTimer(IndexServiceClient indexProvider, GaiaService gaia) {
		super("Index Timer [" + gaia.getName() + "]", indexProvider);
		setDebug(false);
		this.gaia = gaia;
	}

	@Override
	public GaiaService getService() {
		return this.gaia;
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider, GaiaService gaia) throws IOException {
		String name = gaia.getName();

		return indexProvider.getIndexItem(Constants.IDX__GAIA__INDEXER_ID, Constants.IDX__GAIA__TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider, GaiaService gaia) throws IOException {
		// String OSName = gaia.getOSName();
		// String OSVersion = gaia.getOSVersion();
		String name = gaia.getName();
		String hostURL = gaia.getHostURL();
		String contextRoot = gaia.getContextRoot();
		// String nodeHome = gaia.getHome();

		Map<String, Object> props = new Hashtable<String, Object>();
		// props.put(OSConstants.OS_PROGRAM_NAME, OSName);
		// props.put(OSConstants.OS_PROGRAM_VERSION, OSVersion);
		props.put(Constants.GAIA__NAME, name);
		props.put(Constants.GAIA__HOST_URL, hostURL);
		props.put(Constants.GAIA__CONTEXT_ROOT, contextRoot);
		// props.put(PlatformConstants.GAIA_HOME, nodeHome);
		props.put(Constants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(Constants.IDX__GAIA__INDEXER_ID, Constants.IDX__GAIA__TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, GaiaService gaia, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(Constants.IDX__GAIA__INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.deleteIndexItem(Constants.IDX__GAIA__INDEXER_ID, indexItemId);
	}

}
