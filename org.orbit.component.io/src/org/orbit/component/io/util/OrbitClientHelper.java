package org.orbit.component.io.util;

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
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.IndexServiceUtil;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.PlatformConstants;
import org.orbit.platform.api.util.PlatformClientsUtil;
import org.origin.common.rest.client.WSClientConstants;

public class OrbitClientHelper {

	public static OrbitClientHelper INSTANCE = new OrbitClientHelper();

	/**
	 * 
	 * @param accessToken
	 * @param platformId
	 * @return
	 * @throws IOException
	 */
	public IndexItem getPlatformIndexItem(String accessToken, String platformId) throws IOException {
		IndexItem platformIndexItem = null;
		if (platformId != null) {
			IndexServiceClient indexService = IndexServiceUtil.getClient(accessToken);
			if (indexService != null) {
				platformIndexItem = indexService.getIndexItem(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_INDEXER_TYPE, platformId);
			}
		}
		return platformIndexItem;
	}

	/**
	 * 
	 * @param indexServiceUrl
	 * @param accessToken
	 * @param platformParentId
	 * @param nodePlatformid
	 * @return
	 * @throws IOException
	 */
	public IndexItem getNodeIndexItem(String accessToken, String platformParentId, String nodePlatformid) throws IOException {
		IndexItem nodeIndexItem = null;
		if (platformParentId != null && nodePlatformid != null) {
			IndexServiceClient indexService = IndexServiceUtil.getClient(accessToken);
			if (indexService != null) {
				List<IndexItem> indexItems = indexService.getIndexItems(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_INDEXER_TYPE);
				if (indexItems != null) {
					for (IndexItem indexItem : indexItems) {
						String currPlatformId = (String) indexItem.getProperties().get(PlatformConstants.IDX_PROP__PLATFORM_ID);
						String currPlatformParentId = (String) indexItem.getProperties().get(PlatformConstants.IDX_PROP__PLATFORM_PARENT_ID);
						// String currPlatformType = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_TYPE);

						// if (PlatformConstants.PLATFORM_TYPE__NODE.equalsIgnoreCase(currPlatformType) && platformParentId.equals(currPlatformParentId) &&
						// nodePlatformid.equals(currPlatformId)) {
						if (platformParentId.equals(currPlatformParentId) && nodePlatformid.equals(currPlatformId)) {
							nodeIndexItem = indexItem;
							break;
						}
					}
				}
			}
		}
		return nodeIndexItem;
	}

	/**
	 * 
	 * @param indexItem
	 * @param accessToken
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
				platformClient = PlatformClientsUtil.INSTANCE.getPlatformClient(accessToken, platformUrl);
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
			List<IndexItem> nodeControlIndexItems = IndexServiceUtil.getIndexItemsOfPlatform(accessToken, IndexConstants.NODE_CONTROL_INDEXER_ID, platformId);
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

// /**
// *
// * @param accessToken
// * @param indexItem
// * @return
// */
// public PlatformClient getPlatformClient(String accessToken, IndexItem indexItem) {
// PlatformClient platformClient = null;
// if (indexItem != null) {
// String platformUrl = null;
// String platformHostUrl = (String) indexItem.getProperties().get(InfraConstants.SERVICE__HOST_URL);
// String platformContextRoot = (String) indexItem.getProperties().get(InfraConstants.SERVICE__CONTEXT_ROOT);
//
// if (platformHostUrl != null && platformContextRoot != null) {
// platformUrl = platformHostUrl;
// if (!platformUrl.endsWith("/") && !platformContextRoot.startsWith("/")) {
// platformUrl += "/";
// }
// platformUrl += platformContextRoot;
// }
//
// if (platformUrl != null) {
// platformClient = PlatformClientsUtil.INSTANCE.getPlatformClient(accessToken, platformUrl);
// }
// }
// return platformClient;
// }

/// **
// *
// * @param indexServiceUrl
// * @param accessToken
// * @return
// */
// protected IndexServiceClient getIndexService(String indexServiceUrl, String accessToken) {
// IndexServiceClient indexService = null;
// if (indexServiceUrl != null) {
// indexService = InfraClientsHelper.INDEX_SERVICE.getIndexServiceClient(indexServiceUrl, accessToken);
// }
// return indexService;
// }

/// **
// *
// * @param indexService
// * @param platformParentId
// * @param nodePlatformid
// * @return
// * @throws IOException
// */
// public IndexItem getNodeIndexItem(IndexService indexService, String platformParentId, String nodePlatformid) throws IOException {
// IndexItem nodeIndexItem = null;
// if (indexService != null && platformParentId != null && nodePlatformid != null) {
// List<IndexItem> indexItems = indexService.getIndexItems(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_INDEXER_TYPE);
// if (indexItems != null) {
// for (IndexItem indexItem : indexItems) {
// String currPlatformId = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_ID);
// String currPlatformParentId = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_PARENT_ID);
// String currPlatformType = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_TYPE);
//
// if (PlatformConstants.PLATFORM_TYPE__NODE.equalsIgnoreCase(currPlatformType) && platformParentId.equals(currPlatformParentId) &&
// nodePlatformid.equals(currPlatformId)) {
// nodeIndexItem = indexItem;
// break;
// }
// }
// }
// }
// return nodeIndexItem;
// }

/// **
// *
// * @param accessToken
// * @param indexItem
// * @return
// */
// public PlatformClient getPlatformClient(String accessToken, IndexItem indexItem) {
// PlatformClient platformClient = null;
// if (indexItem != null) {
// String platformUrl = null;
// String platformHostUrl = (String) indexItem.getProperties().get(InfraConstants.SERVICE__HOST_URL);
// String platformContextRoot = (String) indexItem.getProperties().get(InfraConstants.SERVICE__CONTEXT_ROOT);
//
// if (platformHostUrl != null && platformContextRoot != null) {
// platformUrl = platformHostUrl;
// if (!platformUrl.endsWith("/") && !platformContextRoot.startsWith("/")) {
// platformUrl += "/";
// }
// platformUrl += platformContextRoot;
// }
//
// if (platformUrl != null) {
// platformClient = PlatformClientsUtil.INSTANCE.getPlatformClient(accessToken, platformUrl);
// }
// }
// return platformClient;
// }
//
/// **
// *
// * @param accessToken
// * @param platformId
// * @return
// * @throws IOException
// */
// public IndexItem getPlatformIndexItem(String accessToken, String platformId) throws IOException {
// IndexItem platformIndexItem = null;
// if (platformId != null) {
// IndexServiceClient indexService = InfraClientsUtil.INDEX_SERVICE.getIndexServiceClient(accessToken);
// if (indexService != null) {
// platformIndexItem = indexService.getIndexItem(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_INDEXER_TYPE, platformId);
// }
// }
// return platformIndexItem;
// }

/// **
// *
// * @param accessToken
// * @param platformId
// * @return
// * @throws IOException
// */
// public IndexItem getPlatformIndexItem(String accessToken, String platformId) throws IOException {
// IndexItem platformIndexItem = null;
// if (platformId != null) {
// IndexServiceClient indexService = InfraClientsUtil.INDEX_SERVICE.getIndexServiceClient(accessToken);
// if (indexService != null) {
// platformIndexItem = indexService.getIndexItem(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_INDEXER_TYPE, platformId);
// }
// }
// return platformIndexItem;
// }
