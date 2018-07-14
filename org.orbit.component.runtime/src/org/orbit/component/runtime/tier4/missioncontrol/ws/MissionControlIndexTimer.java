package org.orbit.component.runtime.tier4.missioncontrol.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlService;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.origin.common.util.DateUtil;

public class MissionControlIndexTimer extends ServiceIndexTimer<MissionControlService> {

	protected MissionControlService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public MissionControlIndexTimer(IndexProvider indexProvider, MissionControlService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public synchronized MissionControlService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, MissionControlService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OrbitConstants.MISSION_CONTROL_INDEXER_ID, OrbitConstants.MISSION_CONTROL_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, MissionControlService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.MISSION_CONTROL_NAME, name);
		props.put(OrbitConstants.MISSION_CONTROL_HOST_URL, hostURL);
		props.put(OrbitConstants.MISSION_CONTROL_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		return indexProvider.addIndexItem(OrbitConstants.MISSION_CONTROL_INDEXER_ID, OrbitConstants.MISSION_CONTROL_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, MissionControlService service, IndexItem indexItem) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Integer indexItemId = indexItem.getIndexItemId();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.MISSION_CONTROL_NAME, name);
		props.put(OrbitConstants.MISSION_CONTROL_HOST_URL, hostURL);
		props.put(OrbitConstants.MISSION_CONTROL_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		indexProvider.setProperties(OrbitConstants.MISSION_CONTROL_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.MISSION_CONTROL_INDEXER_ID, indexItemId);
	}

}
