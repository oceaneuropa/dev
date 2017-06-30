package org.origin.mgm.timer;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.mgm.Activator;
import org.origin.mgm.OriginConstants;
import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.runtime.IndexItem;
import org.origin.mgm.service.IndexService;

public class IndexServiceIndexTimer extends ServiceIndexTimerImpl<IndexService, IndexService> implements ServiceIndexTimer<IndexService, IndexService> {

	/**
	 * 
	 * @param indexProvider
	 */
	public IndexServiceIndexTimer(IndexService indexProvider) {
		super("Index Timer [IndexService]", indexProvider);
		setDebug(false);
	}

	@Override
	public IndexService getService() {
		return Activator.getIndexService();
	}

	@Override
	public void updateIndex(IndexService indexProvider, IndexService service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		try {
			IndexItem indexItem = indexProvider.getIndexItem(OriginConstants.INDEX_SERVICE_INDEXER_ID, OriginConstants.INDEX_SERVICE_TYPE, name);
			if (indexItem == null) {
				// Create new index item
				Map<String, Object> props = new Hashtable<String, Object>();
				props.put(OriginConstants.INDEX_SERVICE_NAME, name);
				props.put(OriginConstants.INDEX_SERVICE_HOST_URL, hostURL);
				props.put(OriginConstants.INDEX_SERVICE_CONTEXT_ROOT, contextRoot);
				props.put(OriginConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

				indexProvider.addIndexItem(OriginConstants.INDEX_SERVICE_INDEXER_ID, OriginConstants.INDEX_SERVICE_TYPE, name, props);

			} else {
				// Update index item
				Integer indexItemId = indexItem.getIndexItemId();
				Map<String, Object> props = new Hashtable<String, Object>();
				props.put(OriginConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

				indexProvider.setProperties(OriginConstants.INDEX_SERVICE_INDEXER_ID, indexItemId, props);
			}
		} catch (IndexServiceException e) {
			throw new IOException(e);
		}
	}

}
