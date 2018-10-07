package org.orbit.infra.api.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.datacast.DataCastClient;
import org.orbit.infra.api.datatube.DataTubeClient;
import org.orbit.infra.api.extensionregistry.ExtensionItem;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClient;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.origin.common.rest.client.WSClientConstants;

public class InfraClientsUtil {

	public static Indexes Indexes = new Indexes();
	public static Extensions Extensions = new Extensions();
	public static DataCast DataCast = new DataCast();
	public static DataTube DataTube = new DataTube();

	public static class Indexes {
		/**
		 * Get all index items of a indexer.
		 * 
		 * @param indexServiceUrl
		 * @param accessToken
		 * @param indexerId
		 * @return
		 * @throws IOException
		 */
		public List<IndexItem> getIndexItems(String indexServiceUrl, String accessToken, String indexerId) throws IOException {
			List<IndexItem> indexItems = null;
			if (indexServiceUrl != null && indexerId != null) {
				IndexServiceClient indexService = getIndexServiceClient(indexServiceUrl, accessToken);
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
		 * @param accessToken
		 * @param indexerId
		 * @param platformId
		 * @return
		 * @throws IOException
		 */
		public List<IndexItem> getIndexItemsOfPlatform(String indexServiceUrl, String accessToken, String indexerId, String platformId) throws IOException {
			List<IndexItem> indexItems = new ArrayList<IndexItem>();
			if (indexServiceUrl != null && indexerId != null) {
				IndexServiceClient indexService = getIndexServiceClient(indexServiceUrl, accessToken);
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
		 * @param accessToken
		 * @param platformId
		 * @return
		 * @throws IOException
		 */
		public IndexItem getIndexItemOfPlatform(String indexServiceUrl, String accessToken, String platformId) throws IOException {
			IndexItem platformIndexItem = null;
			if (indexServiceUrl != null && platformId != null) {
				IndexServiceClient indexService = getIndexServiceClient(indexServiceUrl, accessToken);
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
		 * @param accessToken
		 * @param parentPlatformId
		 * @param platformTypes
		 * @return
		 * @throws IOException
		 */
		public Map<String, IndexItem> getPlatformIdToIndexItem(String indexServiceUrl, String accessToken, String parentPlatformId, String... platformTypes) throws IOException {
			Map<String, IndexItem> platformIdToIndexItem = new HashMap<String, IndexItem>();

			IndexServiceClient indexService = getIndexServiceClient(indexServiceUrl, accessToken);
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
		 * @param accessToken
		 * @param parentPlatformId
		 * @param platformId
		 * @param platformTypes
		 * @return
		 * @throws IOException
		 */
		public IndexItem getIndexItem(String indexServiceUrl, String accessToken, String parentPlatformId, String platformId, String... platformTypes) throws IOException {
			IndexItem indexItem = null;

			if (parentPlatformId != null && platformId != null) {
				IndexServiceClient indexService = getIndexServiceClient(indexServiceUrl, accessToken);
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
		 * @param indexServiceUrl
		 * @param accessToken
		 * @param indexProviderId
		 * @param indexItemId
		 * @return
		 * @throws IOException
		 */
		public boolean deleteIndexItem(String indexServiceUrl, String accessToken, String indexProviderId, Integer indexItemId) throws IOException {
			boolean isDeleted = false;
			if (indexServiceUrl != null && indexProviderId != null && !indexProviderId.isEmpty() && indexItemId != null) {
				IndexServiceClient indexService = getIndexServiceClient(indexServiceUrl, accessToken);
				if (indexService != null) {
					isDeleted = indexService.deleteIndexItem(indexProviderId, indexItemId);
				}
			}
			return isDeleted;
		}

		/**
		 * 
		 * @param indexServiceUrl
		 * @param accessToken
		 * @return
		 */
		public IndexServiceClient getIndexServiceClient(String indexServiceUrl, String accessToken) {
			IndexServiceClient indexServiceClient = null;
			if (indexServiceUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, indexServiceUrl);
				indexServiceClient = InfraClients.getInstance().getIndexService(properties, true);
			}
			return indexServiceClient;
		}
	}

	public static class Extensions {
		/**
		 * Get all extension items from a platform.
		 * 
		 * @param extensionRegistryUrl
		 * @param accessToken
		 * @param platformId
		 * @return
		 * @throws IOException
		 */
		public List<ExtensionItem> getExtensionItemsOfPlatform(String extensionRegistryUrl, String accessToken, String platformId) throws IOException {
			List<ExtensionItem> extensionItems = null;
			if (extensionRegistryUrl != null && platformId != null) {
				ExtensionRegistryClient extensionRegistry = getExtensionRegistryClient(extensionRegistryUrl, accessToken);
				if (extensionRegistry != null) {
					extensionItems = extensionRegistry.getExtensionItems(platformId);
				}
			}
			if (extensionItems == null) {
				extensionItems = new ArrayList<ExtensionItem>();
			}
			return extensionItems;
		}

		/**
		 * Get extension items of specified extension type from a platform.
		 * 
		 * @param extensionRegistryUrl
		 * @param accessToken
		 * @param platformId
		 * @param extensionTypeId
		 * @return
		 * @throws IOException
		 */
		public List<ExtensionItem> getExtensionItemsOfPlatform(String extensionRegistryUrl, String accessToken, String platformId, String extensionTypeId) throws IOException {
			List<ExtensionItem> extensionItems = null;
			if (extensionRegistryUrl != null && platformId != null) {
				ExtensionRegistryClient extensionRegistry = getExtensionRegistryClient(extensionRegistryUrl, accessToken);
				if (extensionRegistry != null) {
					extensionItems = extensionRegistry.getExtensionItems(platformId, extensionTypeId);
				}
			}
			if (extensionItems == null) {
				extensionItems = new ArrayList<ExtensionItem>();
			}
			return extensionItems;
		}

		/**
		 * 
		 * @param extensionRegistryUrl
		 * @param accessToken
		 * @param platformId
		 * @param typeId
		 * @return
		 * @throws IOException
		 */
		public List<String> getExtensionIdsOfPlatform(String extensionRegistryUrl, String accessToken, String platformId, String typeId) throws IOException {
			List<String> extensionIds = new ArrayList<String>();
			if (extensionRegistryUrl != null && platformId != null) {
				ExtensionRegistryClient extensionRegistry = getExtensionRegistryClient(extensionRegistryUrl, accessToken);
				if (extensionRegistry != null) {
					List<ExtensionItem> extensionItems = extensionRegistry.getExtensionItems(platformId, typeId);
					if (extensionItems != null) {
						for (ExtensionItem indexerExtensionItem : extensionItems) {
							String extensionId = indexerExtensionItem.getExtensionId();
							if (extensionId != null && !extensionIds.contains(extensionId)) {
								extensionIds.add(extensionId);
							}
						}
					}
				}
			}
			return extensionIds;
		}

		/**
		 * 
		 * @param extensionItems
		 * @return
		 * @throws IOException
		 */
		public Map<String, List<ExtensionItem>> toExtensionItemMap(List<ExtensionItem> extensionItems) throws IOException {
			Map<String, List<ExtensionItem>> extensionItemMap = new TreeMap<String, List<ExtensionItem>>();

			for (ExtensionItem extensionItem : extensionItems) {
				String typeId = extensionItem.getTypeId();

				List<ExtensionItem> currExtensionItems = extensionItemMap.get(typeId);
				if (currExtensionItems == null) {
					currExtensionItems = new ArrayList<ExtensionItem>();
					extensionItemMap.put(typeId, currExtensionItems);
				}
				currExtensionItems.add(extensionItem);
			}

			return extensionItemMap;
		}

		/**
		 * 
		 * @param extensionRegistryUrl
		 * @param accessToken
		 * @return
		 */
		public ExtensionRegistryClient getExtensionRegistryClient(String extensionRegistryUrl, String accessToken) {
			ExtensionRegistryClient extensionRegistry = null;
			if (extensionRegistryUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, extensionRegistryUrl);
				extensionRegistry = InfraClients.getInstance().getExtensionRegistry(properties, true);
			}
			return extensionRegistry;
		}
	}

	public static class DataCast {
		/**
		 * 
		 * @param dataCastServiceUrl
		 * @param accessToken
		 * @return
		 */
		public DataCastClient getDataCastClient(String dataCastServiceUrl, String accessToken) {
			DataCastClient dataCastClient = null;
			if (dataCastServiceUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, dataCastServiceUrl);

				dataCastClient = InfraClients.getInstance().getDataCastClient(properties);
			}
			return dataCastClient;
		}
	}

	public static class DataTube {
		/**
		 * 
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @return
		 */
		public DataTubeClient getDataTubeClient(String dataTubeServiceUrl, String accessToken) {
			DataTubeClient dataTubeClient = null;
			if (dataTubeServiceUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, dataTubeServiceUrl);

				dataTubeClient = InfraClients.getInstance().getDataTubeClient(properties);
			}
			return dataTubeClient;
		}
	}

}

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
