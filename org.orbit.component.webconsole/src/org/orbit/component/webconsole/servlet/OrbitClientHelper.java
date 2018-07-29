package org.orbit.component.webconsole.servlet;

import java.io.IOException;
import java.util.List;

import org.orbit.component.api.IndexConstants;
import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.util.OrbitIndexHelper;

public class OrbitClientHelper {

	public static OrbitClientHelper INSTANCE = new OrbitClientHelper();

	/**
	 * 
	 * @param indexServiceUrl
	 * @param platformId
	 * @return
	 * @throws IOException
	 */
	public NodeControlClient getNodeControlClient(String indexServiceUrl, String platformId) throws IOException {
		NodeControlClient nodeControlClient = null;
		if (indexServiceUrl != null && platformId != null) {

			IndexItem nodeControlIndexItem = null;
			List<IndexItem> nodeControlIndexItems = OrbitIndexHelper.INSTANCE.getIndexItemsOfPlatform(indexServiceUrl, IndexConstants.NODE_CONTROL_INDEXER_ID, platformId);
			if (nodeControlIndexItems != null && !nodeControlIndexItems.isEmpty()) {
				for (IndexItem currNodeControlIndexItem : nodeControlIndexItems) {
					boolean isOnline = IndexItemHelper.INSTANCE.isOnline(currNodeControlIndexItem);
					if (isOnline) {
						nodeControlIndexItem = currNodeControlIndexItem;
						break;
					}
				}
				if (nodeControlIndexItem == null) {
					nodeControlIndexItem = nodeControlIndexItems.get(0);
				}
			}

			if (nodeControlIndexItem != null) {
				String hostURL = (String) nodeControlIndexItem.getProperties().get(IndexConstants.NODE_CONTROL_HOST_URL);
				String contextRoot = (String) nodeControlIndexItem.getProperties().get(IndexConstants.NODE_CONTROL_CONTEXT_ROOT);
				if (hostURL != null && contextRoot != null) {
					String url = hostURL;
					if (!hostURL.endsWith("/") && !contextRoot.startsWith("/")) {
						url += "/";
					}
					url += contextRoot;
					nodeControlClient = OrbitClients.getInstance().getNodeControl(url);
				}
			}
		}
		return nodeControlClient;
	}

}
