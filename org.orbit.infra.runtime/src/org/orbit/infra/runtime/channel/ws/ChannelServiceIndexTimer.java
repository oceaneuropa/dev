package org.orbit.infra.runtime.channel.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.channel.service.ChannelService;
import org.origin.common.service.WebServiceAwareHelper;

public class ChannelServiceIndexTimer extends ServiceIndexTimer<ChannelService> {

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

		return indexProvider.getIndexItem(InfraConstants.CHANNEL_INDEXER_ID, InfraConstants.CHANNEL_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, ChannelService service) throws IOException {
		String name = service.getName();
		String url = WebServiceAwareHelper.INSTANCE.getURL(service);
		// String namespace = service.getNamespace();
		// String hostURL = service.getHostURL();
		// String contextRoot = service.getContextRoot();

		Date now = new Date();
		// Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.NAME, name);
		props.put(InfraConstants.BASE_URL, url);
		props.put(InfraConstants.LAST_HEARTBEAT_TIME, now);
		// props.put(InfraConstants.CHANNEL_NAMESPACE, namespace);
		// props.put(InfraConstants.CHANNEL_HOST_URL, hostURL);
		// props.put(InfraConstants.CHANNEL_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		// props.put(InfraConstants.HEARTBEAT_EXPIRE_TIME, expire);
		return indexProvider.addIndexItem(InfraConstants.CHANNEL_INDEXER_ID, InfraConstants.CHANNEL_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, ChannelService service, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		String name = service.getName();
		String url = WebServiceAwareHelper.INSTANCE.getURL(service);
		// String namespace = service.getNamespace();
		// String hostURL = service.getHostURL();
		// String contextRoot = service.getContextRoot();

		Date now = new Date();
		// Date expire = DateUtil.addSeconds(now, 30);

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(InfraConstants.NAME, name);
		props.put(InfraConstants.BASE_URL, url);
		props.put(InfraConstants.LAST_HEARTBEAT_TIME, now);
		// props.put(InfraConstants.CHANNEL_NAMESPACE, namespace);
		// props.put(InfraConstants.CHANNEL_HOST_URL, hostURL);
		// props.put(InfraConstants.CHANNEL_CONTEXT_ROOT, contextRoot);
		// props.put(OrbitConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
		// props.put(InfraConstants.HEARTBEAT_EXPIRE_TIME, expire);
		indexProvider.setProperties(InfraConstants.CHANNEL_INDEXER_ID, indexItemId, props);

		// List<String> propNames = new ArrayList<String>();
		// propNames.add(InfraConstants.URL);
		// propNames.add(InfraConstants.CHANNEL_NAME);
		// propNames.add(InfraConstants.CHANNEL_HOST_URL);
		// propNames.add(InfraConstants.CHANNEL_CONTEXT_ROOT);
		// indexProvider.removeProperties(InfraConstants.CHANNEL_INDEXER_ID, indexItemId, propNames);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(InfraConstants.CHANNEL_INDEXER_ID, indexItemId);
	}

}
