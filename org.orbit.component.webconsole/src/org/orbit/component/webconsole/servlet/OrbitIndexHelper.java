package org.orbit.component.webconsole.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.platform.api.PlatformConstants;

public class OrbitIndexHelper {

	public static OrbitIndexHelper INSTANCE = new OrbitIndexHelper();

	/**
	 * 
	 * @param indexServiceUrl
	 * @param platformId
	 * @return
	 * @throws IOException
	 */
	public IndexItem getPlatformIndexItem(String indexServiceUrl, String platformId) throws IOException {
		IndexItem platformIndexItem = null;
		if (indexServiceUrl != null && platformId != null) {
			IndexService indexService = getIndexService(indexServiceUrl);
			if (indexService != null) {
				List<IndexItem> indexItems = indexService.getIndexItems(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_INDEXER_TYPE);
				if (indexItems != null) {
					for (IndexItem currIndexItem : indexItems) {
						String currPlatformId = (String) currIndexItem.getProperties().get(PlatformConstants.PLATFORM_ID);
						if (platformId.equals(currPlatformId)) {
							platformIndexItem = currIndexItem;
							break;
						}
					}
				}
			}
		}
		return platformIndexItem;
	}

	/**
	 * 
	 * @param indexService
	 * @param platformTypes
	 * @return
	 * @throws IOException
	 */
	public Map<String, IndexItem> getPlatformIdToIndexItem(String indexServiceUrl, String platformParentId, String... platformTypes) throws IOException {
		Map<String, IndexItem> platformIdToIndexItem = new HashMap<String, IndexItem>();

		IndexService indexService = getIndexService(indexServiceUrl);
		if (indexService != null) {
			List<IndexItem> indexItems = indexService.getIndexItems(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_INDEXER_TYPE);
			if (indexItems != null) {
				for (IndexItem indexItem : indexItems) {
					String currPlatformId = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_ID);
					String currPlatformParentId = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_PARENT_ID);
					String currPlatformType = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_TYPE);

					boolean matchParentId = false;
					if (platformParentId == null) {
						matchParentId = true;
					} else {
						if (platformParentId.equals(currPlatformParentId)) {
							matchParentId = true;
						}
					}

					boolean matchType = false;
					if (platformTypes == null) {
						matchType = true;

					} else {
						for (String platformType : platformTypes) {
							if (platformType.equalsIgnoreCase(currPlatformType)) {
								matchType = true;
								break;
							}
						}
					}

					if (matchParentId && matchType) {
						platformIdToIndexItem.put(currPlatformId, indexItem);
					}
				}
			}
		}

		return platformIdToIndexItem;
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

		if (platformParentId != null && nodePlatformid != null) {
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
