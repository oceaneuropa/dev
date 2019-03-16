package org.orbit.component.webconsole.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.IndexConstants;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.util.ComponentClients;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.util.PlatformClientsUtil;
import org.origin.common.rest.client.WSClientConstants;

public class OrbitClientHelper {

	public static OrbitClientHelper INSTANCE = new OrbitClientHelper();

	/**
	 * 
	 * @param accessToken
	 * @param indexItem
	 * @return
	 */
	public PlatformClient getPlatformClient(String accessToken, IndexItem indexItem) {
		PlatformClient platformClient = null;
		if (indexItem != null) {
			String platformUrl = null;
			String platformHostUrl = (String) indexItem.getProperties().get(InfraConstants.SERVICE__HOST_URL);
			String platformContextRoot = (String) indexItem.getProperties().get(InfraConstants.SERVICE__CONTEXT_ROOT);

			if (platformHostUrl != null && platformContextRoot != null) {
				platformUrl = platformHostUrl;
				if (!platformUrl.endsWith("/") && !platformContextRoot.startsWith("/")) {
					platformUrl += "/";
				}
				platformUrl += platformContextRoot;
			}

			if (platformUrl != null) {
				platformClient = PlatformClientsUtil.Platform.getPlatformClient(accessToken, platformUrl);
			}
		}
		return platformClient;
	}

	/**
	 * 
	 * @param accessToken
	 * @param platformId
	 * @return
	 * @throws IOException
	 */
	public NodeControlClient getNodeControlClient(String accessToken, String platformId) throws IOException {
		NodeControlClient nodeControlClient = null;
		if (platformId != null) {

			IndexItem nodeControlIndexItem = null;
			List<IndexItem> nodeControlIndexItems = InfraClientsHelper.INDEX_SERVICE.getIndexItemsOfPlatform(accessToken, IndexConstants.NODE_CONTROL_INDEXER_ID, platformId);
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
				String hostURL = (String) nodeControlIndexItem.getProperties().get(InfraConstants.SERVICE__HOST_URL);
				String contextRoot = (String) nodeControlIndexItem.getProperties().get(InfraConstants.SERVICE__CONTEXT_ROOT);

				if (hostURL != null && contextRoot != null) {
					String nodeControlServiceUrl = hostURL;
					if (!hostURL.endsWith("/") && !contextRoot.startsWith("/")) {
						nodeControlServiceUrl += "/";
					}
					nodeControlServiceUrl += contextRoot;

					Map<String, Object> properties = new HashMap<String, Object>();
					properties.put(WSClientConstants.REALM, null);
					properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
					properties.put(WSClientConstants.URL, nodeControlServiceUrl);

					nodeControlClient = ComponentClients.getInstance().getNodeControl(properties);
				}
			}
		}
		return nodeControlClient;
	}

}
