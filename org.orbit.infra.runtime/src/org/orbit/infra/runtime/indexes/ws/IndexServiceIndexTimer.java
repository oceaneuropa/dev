package org.orbit.infra.runtime.indexes.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.model.indexes.IndexServiceException;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.origin.common.service.WebServiceAwareHelper;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;

public class IndexServiceIndexTimer extends ServiceIndexTimerImpl<IndexService, IndexService, IndexItem> implements ServiceIndexTimer<IndexService, IndexService, IndexItem> {

	protected IndexService service;

	/**
	 * 
	 * @param indexProvider
	 */
	public IndexServiceIndexTimer(IndexService indexProvider, IndexService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public IndexService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexService indexProvider, IndexService service) throws IOException {
		try {
			String name = service.getName();

			return indexProvider.getIndexItem(InfraConstants.INDEX_SERVICE_INDEXER_ID, InfraConstants.INDEX_SERVICE_TYPE, name);

		} catch (IndexServiceException e) {
			throw new IOException(e);
		}
	}

	@Override
	public IndexItem addIndex(IndexService indexProvider, IndexService service) throws IOException {
		try {
			String name = service.getName();
			String url = WebServiceAwareHelper.INSTANCE.getURL(service);
			// String hostURL = service.getHostURL();
			// String contextRoot = service.getContextRoot();

			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(InfraConstants.NAME, name);
			props.put(InfraConstants.BASE_URL, url);
			// props.put(InfraConstants.INDEX_SERVICE_HOST_URL, hostURL);
			// props.put(InfraConstants.INDEX_SERVICE_CONTEXT_ROOT, contextRoot);
			props.put(InfraConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
			return indexProvider.addIndexItem(InfraConstants.INDEX_SERVICE_INDEXER_ID, InfraConstants.INDEX_SERVICE_TYPE, name, props);

		} catch (IndexServiceException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void updateIndex(IndexService indexProvider, IndexService service, IndexItem indexItem) throws IOException {
		try {
			Integer indexItemId = indexItem.getIndexItemId();
			String name = service.getName();
			String url = WebServiceAwareHelper.INSTANCE.getURL(service);
			// String hostURL = service.getHostURL();
			// String contextRoot = service.getContextRoot();

			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(InfraConstants.NAME, name);
			props.put(InfraConstants.BASE_URL, url);
			props.put(InfraConstants.LAST_HEARTBEAT_TIME, new Date().getTime());
			// props.put(InfraConstants.INDEX_SERVICE_HOST_URL, hostURL);
			// props.put(InfraConstants.INDEX_SERVICE_CONTEXT_ROOT, contextRoot);
			indexProvider.setProperties(InfraConstants.INDEX_SERVICE_INDEXER_ID, indexItemId, props);

			// List<String> propNames = new ArrayList<String>();
			// propNames.add(InfraConstants.URL);
			// propNames.add(InfraConstants.INDEX_SERVICE_NAME);
			// propNames.add(InfraConstants.INDEX_SERVICE_HOST_URL);
			// propNames.add(InfraConstants.INDEX_SERVICE_CONTEXT_ROOT);
			// indexProvider.removeProperty(InfraConstants.INDEX_SERVICE_INDEXER_ID, indexItemId, propNames);

		} catch (IndexServiceException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void removeIndex(IndexService indexProvider, IndexItem indexItem) throws IOException {
		try {
			Integer indexItemId = indexItem.getIndexItemId();

			indexProvider.removeIndexItem(InfraConstants.INDEX_SERVICE_INDEXER_ID, indexItemId);

		} catch (IndexServiceException e) {
			throw new IOException(e);
		}
	}

}
