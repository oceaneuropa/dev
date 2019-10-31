package org.orbit.infra.api.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;

public class IndexServiceUtil {

	/**
	 * 
	 * @param accessToken
	 * @return
	 */
	public static IndexServiceClient getClient(String accessToken) {
		String indexServiceUrl = InfraServicesPropertiesHandler.getInstance().getIndexServiceURL();
		IndexServiceClient indexService = InfraClients.getInstance().getIndexService(indexServiceUrl, accessToken);
		return indexService;
	}

	/**
	 * Get all index items of a indexer.
	 * 
	 * @param indexServiceUrl
	 * @param accessToken
	 * @param indexerId
	 * @return
	 * @throws IOException
	 */
	public static List<IndexItem> getIndexItems(String accessToken, String indexerId) throws IOException {
		List<IndexItem> indexItems = null;
		if (indexerId != null) {
			IndexServiceClient indexService = getClient(accessToken);
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
	 * @param accessToken
	 * @param indexerId
	 * @param platformId
	 * @return
	 * @throws IOException
	 */
	public static List<IndexItem> getIndexItemsOfPlatform(String accessToken, String indexerId, String platformId) throws IOException {
		List<IndexItem> indexItems = new ArrayList<IndexItem>();
		if (indexerId != null) {
			IndexServiceClient indexService = getClient(accessToken);
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
	 * @param accessToken
	 * @param platformId
	 * @return
	 * @throws IOException
	 */
	public static IndexItem getIndexItemOfPlatform(String accessToken, String platformId) throws IOException {
		IndexItem platformIndexItem = null;
		if (platformId != null) {
			IndexServiceClient indexService = getClient(accessToken);
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
	 * @param accessToken
	 * @param parentPlatformId
	 * @param platformTypes
	 * @return
	 * @throws IOException
	 */
	public static Map<String, IndexItem> getPlatformIdToIndexItem(String accessToken, String parentPlatformId, String... platformTypes) throws IOException {
		Map<String, IndexItem> platformIdToIndexItem = new HashMap<String, IndexItem>();

		IndexServiceClient indexService = getClient(accessToken);
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
	 * @param accessToken
	 * @param parentPlatformId
	 * @param platformId
	 * @param platformTypes
	 * @return
	 * @throws IOException
	 */
	public static IndexItem getIndexItem(String accessToken, String parentPlatformId, String platformId, String... platformTypes) throws IOException {
		IndexItem indexItem = null;

		if (parentPlatformId != null && platformId != null) {
			IndexServiceClient indexService = getClient(accessToken);
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
	 * Delete an index item.
	 * 
	 * @param accessToken
	 * @param indexProviderId
	 * @param indexItemId
	 * @return
	 * @throws IOException
	 */
	public static boolean removeIndexItem(String accessToken, String indexProviderId, Integer indexItemId) throws IOException {
		boolean isDeleted = false;
		if (indexProviderId != null && !indexProviderId.isEmpty() && indexItemId != null) {
			IndexServiceClient indexService = getClient(accessToken);
			if (indexService != null) {
				isDeleted = indexService.removeIndexItem(indexProviderId, indexItemId);
			}
		}
		return isDeleted;
	}

	/**
	 * Delete index items.
	 * 
	 * @param accessToken
	 * @param indexProviderId
	 * @return
	 * @throws IOException
	 */
	public static boolean removeIndexItems(String accessToken, String indexProviderId) throws IOException {
		boolean isDeleted = false;
		if (indexProviderId != null && !indexProviderId.isEmpty()) {
			IndexServiceClient indexService = getClient(accessToken);
			if (indexService != null) {
				isDeleted = indexService.removeIndexItems(indexProviderId);
			}
		}
		return isDeleted;
	}

}

// /**
// *
// * @param indexServiceUrl
// * @param accessToken
// * @return
// */
// protected IndexServiceClient getIndexServiceClient(String indexServiceUrl, String accessToken) {
// IndexServiceClient indexService = InfraClients.getInstance().getIndexService(indexServiceUrl, accessToken);
// return indexService;
// }

// /**
// *
// * @param extensionRegistryUrl
// * @param platformId
// * @return
// * @throws IOException
// */
// public Map<String, List<ExtensionItem>> getExtensionItemMapOfPlatform(String extensionRegistryUrl, String platformId) throws IOException {
// Map<String, List<ExtensionItem>> extensionItemMap = new TreeMap<String, List<ExtensionItem>>();
//
// List<ExtensionItem> extensionItems = getExtensionItemsOfPlatform(extensionRegistryUrl, platformId);
// for (ExtensionItem extensionItem : extensionItems) {
// String typeId = extensionItem.getTypeId();
//
// List<ExtensionItem> currExtensionItems = extensionItemMap.get(typeId);
// if (currExtensionItems == null) {
// currExtensionItems = new ArrayList<ExtensionItem>();
// extensionItemMap.put(typeId, currExtensionItems);
// }
// currExtensionItems.add(extensionItem);
// }
//
// return extensionItemMap;
// }

/// **
// *
// * @param clientResolver
// * @param dataTubeServiceUrl
// * @param accessToken
// * @param channelId
// * @param createIfNotExist
// * @return
// * @throws ClientException
// */
// public boolean syncChannelMetadataById(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String channelId, boolean
/// createIfNotExist) throws ClientException {
// boolean isUpdated = false;
// DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
// if (dataTubeClient != null) {
// isUpdated = dataTubeClient.syncChannelMetadataId(channelId, createIfNotExist);
// }
// return isUpdated;
// }
//
/// **
// *
// * @param clientResolver
// * @param dataTubeServiceUrl
// * @param accessToken
// * @param name
// * @param createIfNotExist
// * @return
// * @throws ClientException
// */
// public boolean syncChannelMetadataByName(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String name, boolean
/// createIfNotExist) throws ClientException {
// boolean isUpdated = false;
// DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
// if (dataTubeClient != null) {
// isUpdated = dataTubeClient.syncChannelMetadataByName(name, createIfNotExist);
// }
// return isUpdated;
// }
