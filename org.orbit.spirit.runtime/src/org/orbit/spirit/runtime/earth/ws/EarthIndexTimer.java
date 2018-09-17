package org.orbit.spirit.runtime.earth.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.spirit.runtime.SpiritConstants;
import org.orbit.spirit.runtime.earth.service.EarthService;

public class EarthIndexTimer extends ServiceIndexTimer<EarthService> {

	protected EarthService earth;

	/**
	 * 
	 * @param indexProvider
	 * @param earth
	 */
	public EarthIndexTimer(IndexServiceClient indexProvider, EarthService earth) {
		super("Index Timer [" + earth.getName() + "]", indexProvider);
		setDebug(false);
		this.earth = earth;
	}

	@Override
	public EarthService getService() {
		return this.earth;
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider, EarthService earth) throws IOException {
		String name = earth.getName();
		return indexProvider.getIndexItem(SpiritConstants.IDX__EARTH__INDEXER_ID, SpiritConstants.IDX__EARTH__TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider, EarthService earth) throws IOException {
		String gaiaId = earth.getGaiaId();
		String earthId = earth.getEarthId();
		String name = earth.getName();
		String hostURL = earth.getHostURL();
		String contextRoot = earth.getContextRoot();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SpiritConstants.IDX_PROP__EARTH__GAIA_ID, gaiaId);
		props.put(SpiritConstants.IDX_PROP__EARTH__ID, earthId);
		props.put(SpiritConstants.IDX_PROP__EARTH__NAME, name);
		props.put(SpiritConstants.IDX_PROP__EARTH__HOST_URL, hostURL);
		props.put(SpiritConstants.IDX_PROP__EARTH__CONTEXT_ROOT, contextRoot);
		props.put(SpiritConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(SpiritConstants.IDX__EARTH__INDEXER_ID, SpiritConstants.IDX__EARTH__TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, EarthService earth, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SpiritConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(SpiritConstants.IDX__EARTH__INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.deleteIndexItem(SpiritConstants.IDX__EARTH__INDEXER_ID, indexItemId);
	}

}
