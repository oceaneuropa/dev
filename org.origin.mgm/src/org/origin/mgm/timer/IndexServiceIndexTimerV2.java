package org.origin.mgm.timer;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.origin.common.thread.ServiceIndexTimerImplV2;
import org.origin.common.thread.ServiceIndexTimerV2;
import org.origin.mgm.Activator;
import org.origin.mgm.OriginConstants;
import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.runtime.IndexItem;
import org.origin.mgm.service.IndexService;

public class IndexServiceIndexTimerV2 extends ServiceIndexTimerImplV2<IndexService, IndexService, IndexItem> implements ServiceIndexTimerV2<IndexService, IndexService, IndexItem> {

	/**
	 * 
	 * @param indexProvider
	 */
	public IndexServiceIndexTimerV2(IndexService indexProvider) {
		super("Index Timer [IndexService]", indexProvider);
		setDebug(false);
	}

	@Override
	public IndexService getService() {
		return Activator.getIndexService();
	}

	@Override
	public IndexItem getIndex(IndexService indexProvider, IndexService service) throws IOException {
		try {
			String name = service.getName();

			return indexProvider.getIndexItem(OriginConstants.INDEX_SERVICE_INDEXER_ID, OriginConstants.INDEX_SERVICE_TYPE, name);

		} catch (IndexServiceException e) {
			throw new IOException(e);
		}
	}

	@Override
	public IndexItem addIndex(IndexService indexProvider, IndexService service) throws IOException {
		try {
			String name = service.getName();
			String hostURL = service.getHostURL();
			String contextRoot = service.getContextRoot();

			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OriginConstants.INDEX_SERVICE_NAME, name);
			props.put(OriginConstants.INDEX_SERVICE_HOST_URL, hostURL);
			props.put(OriginConstants.INDEX_SERVICE_CONTEXT_ROOT, contextRoot);
			props.put(OriginConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			return indexProvider.addIndexItem(OriginConstants.INDEX_SERVICE_INDEXER_ID, OriginConstants.INDEX_SERVICE_TYPE, name, props);

		} catch (IndexServiceException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void updateIndex(IndexService indexProvider, IndexService service, IndexItem indexItem) throws IOException {
		try {
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(OriginConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(OriginConstants.INDEX_SERVICE_INDEXER_ID, indexItemId, props);

		} catch (IndexServiceException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void removeIndex(IndexService indexProvider, IndexItem indexItem) throws IOException {
		try {
			Integer indexItemId = indexItem.getIndexItemId();

			indexProvider.removeIndexItem(OriginConstants.INDEX_SERVICE_INDEXER_ID, indexItemId);

		} catch (IndexServiceException e) {
			throw new IOException(e);
		}
	}

}
