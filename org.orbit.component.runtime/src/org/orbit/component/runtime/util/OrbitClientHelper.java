package org.orbit.component.runtime.util;

import java.io.IOException;
import java.util.List;

import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.platform.api.Clients;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.PlatformConstants;

public class OrbitClientHelper {

	public static OrbitClientHelper INSTANCE = new OrbitClientHelper();

	/**
	 * 
	 * @param indexItem
	 * @return
	 */
	public PlatformClient getPlatformClient(IndexItem indexItem) {
		PlatformClient platformClient = null;
		if (indexItem != null) {
			String platformUrl = null;
			String platformHostUrl = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_HOST_URL);
			String platformContextRoot = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_CONTEXT_ROOT);

			if (platformHostUrl != null && platformContextRoot != null) {
				platformUrl = platformHostUrl;
				if (!platformUrl.endsWith("/") && !platformContextRoot.startsWith("/")) {
					platformUrl += "/";
				}
				platformUrl += platformContextRoot;
			}

			if (platformUrl != null) {
				platformClient = Clients.getInstance().getPlatformClient(platformUrl);
			}
		}
		return platformClient;
	}

	/**
	 * 
	 * @param indexServiceUrl
	 * @param platformParentId
	 * @param nodePlatformid
	 * @return
	 * @throws IOException
	 */
	public IndexItem getNodeIndexItem(String indexServiceUrl, String platformParentId, String nodePlatformid) throws IOException {
		IndexItem nodeIndexItem = null;
		if (indexServiceUrl != null && platformParentId != null && nodePlatformid != null) {
			IndexService indexService = getIndexService(indexServiceUrl);
			if (indexService != null) {
				List<IndexItem> indexItems = indexService.getIndexItems(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_INDEXER_TYPE);
				if (indexItems != null) {
					for (IndexItem indexItem : indexItems) {
						String currPlatformId = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_ID);
						String currPlatformParentId = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_PARENT_ID);
						String currPlatformType = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_TYPE);

						if (PlatformConstants.PLATFORM_TYPE__NODE.equalsIgnoreCase(currPlatformType) && platformParentId.equals(currPlatformParentId) && nodePlatformid.equals(currPlatformId)) {
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
	 * @param indexServiceUrl
	 * @return
	 */
	protected IndexService getIndexService(String indexServiceUrl) {
		IndexService indexService = null;
		if (indexServiceUrl != null) {
			indexService = InfraClients.getInstance().getIndexService(indexServiceUrl);
		}
		return indexService;
	}

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
