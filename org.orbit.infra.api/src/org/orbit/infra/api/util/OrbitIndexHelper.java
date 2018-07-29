package org.orbit.infra.api.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;

public class OrbitIndexHelper {

	public static OrbitIndexHelper INSTANCE = new OrbitIndexHelper();

	/**
	 * Get all index items of a indexer.
	 * 
	 * @param indexServiceUrl
	 * @param indexerId
	 * @return
	 * @throws IOException
	 */
	public List<IndexItem> getIndexItems(String indexServiceUrl, String indexerId) throws IOException {
		List<IndexItem> indexItems = null;
		if (indexServiceUrl != null && indexerId != null) {
			IndexService indexService = getIndexService(indexServiceUrl);
			if (indexService != null) {
				indexItems = indexService.getIndexItems(indexerId);
			}
		}
		if (indexItems == null) {
			indexItems = new ArrayList<IndexItem>();
		}
		return indexItems;
	}

	/**
	 * Get all index items of a indexer of a platform.
	 * 
	 * @param indexServiceUrl
	 * @param indexerId
	 * @param platformId
	 * @return
	 * @throws IOException
	 */
	public List<IndexItem> getIndexItemsOfPlatform(String indexServiceUrl, String indexerId, String platformId) throws IOException {
		List<IndexItem> indexItems = new ArrayList<IndexItem>();
		if (indexServiceUrl != null && indexerId != null) {
			IndexService indexService = getIndexService(indexServiceUrl);
			if (indexService != null) {
				List<IndexItem> allIndexItems = indexService.getIndexItems(indexerId);
				if (allIndexItems != null) {
					for (IndexItem currIndexItem : allIndexItems) {
						String currPlatformId = (String) currIndexItem.getProperties().get(InfraConstants.PLATFORM_ID);
						if (platformId.equals(currPlatformId)) {
							indexItems.add(currIndexItem);
						}
					}
				}
			}
		}
		return indexItems;
	}

	/**
	 * Get the index item of a platform.
	 * 
	 * @param indexServiceUrl
	 * @param platformId
	 * @return
	 * @throws IOException
	 */
	public IndexItem getIndexItemOfPlatform(String indexServiceUrl, String platformId) throws IOException {
		IndexItem platformIndexItem = null;
		if (indexServiceUrl != null && platformId != null) {
			IndexService indexService = getIndexService(indexServiceUrl);
			if (indexService != null) {
				List<IndexItem> indexItems = indexService.getIndexItems(InfraConstants.PLATFORM_INDEXER_ID, InfraConstants.PLATFORM_INDEXER_TYPE);
				if (indexItems != null) {
					for (IndexItem currIndexItem : indexItems) {
						String currPlatformId = (String) currIndexItem.getProperties().get(InfraConstants.PLATFORM_ID);
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
	 * @param indexServiceUrl
	 * @param parentPlatformId
	 * @param platformTypes
	 * @return
	 * @throws IOException
	 */
	public Map<String, IndexItem> getPlatformIdToIndexItem(String indexServiceUrl, String parentPlatformId, String... platformTypes) throws IOException {
		Map<String, IndexItem> platformIdToIndexItem = new HashMap<String, IndexItem>();

		IndexService indexService = getIndexService(indexServiceUrl);
		if (indexService != null) {
			List<IndexItem> indexItems = indexService.getIndexItems(InfraConstants.PLATFORM_INDEXER_ID, InfraConstants.PLATFORM_INDEXER_TYPE);
			if (indexItems != null) {
				for (IndexItem indexItem : indexItems) {
					String currPlatformId = (String) indexItem.getProperties().get(InfraConstants.PLATFORM_ID);
					String currPlatformParentId = (String) indexItem.getProperties().get(InfraConstants.PLATFORM_PARENT_ID);
					String currPlatformType = (String) indexItem.getProperties().get(InfraConstants.PLATFORM_TYPE);

					boolean matchParentId = false;
					if (parentPlatformId == null) {
						matchParentId = true;
					} else {
						if (parentPlatformId.equals(currPlatformParentId)) {
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
	 * @param parentPlatformId
	 * @param platformId
	 * @param platformTypes
	 * @return
	 * @throws IOException
	 */
	public IndexItem getIndexItem(String indexServiceUrl, String parentPlatformId, String platformId, String... platformTypes) throws IOException {
		IndexItem indexItem = null;

		if (parentPlatformId != null && platformId != null) {
			IndexService indexService = getIndexService(indexServiceUrl);
			if (indexService != null) {
				List<IndexItem> indexItems = indexService.getIndexItems(InfraConstants.PLATFORM_INDEXER_ID, InfraConstants.PLATFORM_INDEXER_TYPE);
				if (indexItems != null) {
					for (IndexItem currIndexItem : indexItems) {
						String currPlatformId = (String) currIndexItem.getProperties().get(InfraConstants.PLATFORM_ID);
						String currPlatformParentId = (String) currIndexItem.getProperties().get(InfraConstants.PLATFORM_PARENT_ID);
						String currPlatformType = (String) currIndexItem.getProperties().get(InfraConstants.PLATFORM_TYPE);

						boolean matchParentId = false;
						if (parentPlatformId == null) {
							matchParentId = true;
						} else {
							if (parentPlatformId.equals(currPlatformParentId)) {
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

						if (matchParentId && platformId.equals(currPlatformId) && matchType) {
							indexItem = currIndexItem;
							break;
						}
					}
				}
			}
		}

		return indexItem;
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
