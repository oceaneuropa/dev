package org.orbit.component.runtime.util;

import java.io.IOException;
import java.util.List;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.PlatformConstants;
import org.orbit.platform.api.util.PlatformClientsUtil;

public class OrbitClientHelper {

	public static OrbitClientHelper INSTANCE = new OrbitClientHelper();

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
			IndexServiceClient indexService = InfraClientsHelper.INDEX_SERVICE.getIndexServiceClient(accessToken);
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

	// /**
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

}

// /**
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
