package org.orbit.component.server.tier0.channel.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier0.channel.service.ChannelService;
import org.origin.common.thread.ServiceIndexTimerImplV2;
import org.origin.common.thread.ServiceIndexTimerV2;
import org.origin.common.util.DateUtil;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;

public class ChannelServiceIndexTimer extends ServiceIndexTimerImplV2<IndexProvider, ChannelService, IndexItem> implements ServiceIndexTimerV2<IndexProvider, ChannelService, IndexItem> {

	protected ChannelService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public ChannelServiceIndexTimer(IndexProvider indexProvider, ChannelService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public ChannelService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, ChannelService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OrbitConstants.CHANNEL_INDEXER_ID, OrbitConstants.CHANNEL_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, ChannelService service) throws IOException {
		String namespace = service.getNamespace();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.CHANNEL_NAMESPACE, namespace);
		props.put(OrbitConstants.CHANNEL_NAME, name);
		props.put(OrbitConstants.CHANNEL_HOST_URL, hostURL);
		props.put(OrbitConstants.CHANNEL_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		return indexProvider.addIndexItem(OrbitConstants.CHANNEL_INDEXER_ID, OrbitConstants.CHANNEL_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, ChannelService service, IndexItem indexItem) throws IOException {
		String namespace = service.getNamespace();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Integer indexItemId = indexItem.getIndexItemId();

		Date now = new Date();
		Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OrbitConstants.CHANNEL_NAMESPACE, namespace);
		props.put(OrbitConstants.CHANNEL_NAME, name);
		props.put(OrbitConstants.CHANNEL_HOST_URL, hostURL);
		props.put(OrbitConstants.CHANNEL_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		props.put(OrbitConstants.LAST_HEARTBEAT_TIME, now);
		props.put(OrbitConstants.HEARTBEAT_EXPIRE_TIME, expire);

		indexProvider.setProperties(OrbitConstants.CHANNEL_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OrbitConstants.CHANNEL_INDEXER_ID, indexItemId);
	}

}
