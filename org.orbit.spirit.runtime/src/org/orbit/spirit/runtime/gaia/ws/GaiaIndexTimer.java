package org.orbit.spirit.runtime.gaia.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.spirit.runtime.SpiritConstants;
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
		String name = gaia.getGaiaId();
		return indexProvider.getIndexItem(SpiritConstants.IDX__GAIA__INDEXER_ID, SpiritConstants.IDX__GAIA__TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider, GaiaService gaia) throws IOException {
		String gaiaId = gaia.getGaiaId();
		String name = gaia.getName();
		String hostURL = gaia.getHostURL();
		String contextRoot = gaia.getContextRoot();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SpiritConstants.IDX_PROP__GAIA__ID, gaiaId);
		props.put(SpiritConstants.IDX_PROP__GAIA__NAME, name);
		props.put(SpiritConstants.IDX_PROP__GAIA__HOST_URL, hostURL);
		props.put(SpiritConstants.IDX_PROP__GAIA__CONTEXT_ROOT, contextRoot);

		props.put(SpiritConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(SpiritConstants.IDX__GAIA__INDEXER_ID, SpiritConstants.IDX__GAIA__TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, GaiaService gaia, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SpiritConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(SpiritConstants.IDX__GAIA__INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.deleteIndexItem(SpiritConstants.IDX__GAIA__INDEXER_ID, indexItemId);
	}

}

// String OSName = gaia.getOSName();
// String OSVersion = gaia.getOSVersion();
// String nodeHome = gaia.getHome();
// props.put(OSConstants.OS_PROGRAM_NAME, OSName);
// props.put(OSConstants.OS_PROGRAM_VERSION, OSVersion);
// props.put(PlatformConstants.GAIA_HOME, nodeHome);
