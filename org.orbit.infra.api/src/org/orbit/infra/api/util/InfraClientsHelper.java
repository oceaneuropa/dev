package org.orbit.infra.api.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.configregistry.ConfigElement;
import org.orbit.infra.api.configregistry.ConfigRegistry;
import org.orbit.infra.api.configregistry.ConfigRegistryClient;
import org.orbit.infra.api.configregistry.ConfigRegistryClientResolver;
import org.orbit.infra.api.datacast.ChannelMetadata;
import org.orbit.infra.api.datacast.DataCastClient;
import org.orbit.infra.api.datacast.DataCastClientResolver;
import org.orbit.infra.api.datacast.DataCastServiceMetadata;
import org.orbit.infra.api.datatube.DataTubeClient;
import org.orbit.infra.api.datatube.DataTubeClientResolver;
import org.orbit.infra.api.datatube.DataTubeServiceMetadata;
import org.orbit.infra.api.extensionregistry.ExtensionItem;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClient;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.origin.common.resource.Path;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.rest.model.ServiceMetadata;

public class InfraClientsHelper {

	public static INDEX_SERVICE INDEX_SERVICE = new INDEX_SERVICE();
	public static EXTENSION_REGISTRY EXTENSION_REGISTRY = new EXTENSION_REGISTRY();
	public static CONFIG_REGISTRY CONFIG_REGISTRY = new CONFIG_REGISTRY();
	public static DATA_CAST DATA_CAST = new DATA_CAST();
	public static DATA_TUBE DATA_TUBE = new DATA_TUBE();

	public static class INDEX_SERVICE {
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

	public static class EXTENSION_REGISTRY {
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

	public static class CONFIG_REGISTRY {
		public ConfigRegistry[] EMPTY_CONFIG_REGISTRIES = new ConfigRegistry[0];
		public ConfigElement[] EMPTY_CONFIG_ELEMENTS = new ConfigElement[0];

		/**
		 * 
		 * @param configRegistryUrl
		 * @param accessToken
		 * @return
		 */
		public ConfigRegistryClient getConfigRegistryClient(String configRegistryUrl, String accessToken) {
			ConfigRegistryClient configRegistryClient = InfraClients.getInstance().getConfigRegistryClient(configRegistryUrl, accessToken);
			return configRegistryClient;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public ServiceMetadata getServiceMetadata(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken) throws ClientException {
			ServiceMetadata metadata = null;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				metadata = client.getMetadata();
			}
			return metadata;
		}

		// -----------------------------------------------------------------------------------
		// Config Registries
		// -----------------------------------------------------------------------------------
		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public ConfigRegistry[] getConfigRegistries(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken) throws ClientException {
			ConfigRegistry[] configRegistries = null;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				configRegistries = client.getConfigRegistries();
			}
			if (configRegistries == null) {
				configRegistries = EMPTY_CONFIG_REGISTRIES;
			}
			return configRegistries;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param type
		 * @return
		 * @throws ClientException
		 */
		public ConfigRegistry[] getConfigRegistries(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String type) throws ClientException {
			ConfigRegistry[] configRegistries = null;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				configRegistries = client.getConfigRegistries(type);
			}
			if (configRegistries == null) {
				configRegistries = EMPTY_CONFIG_REGISTRIES;
			}
			return configRegistries;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param id
		 * @return
		 * @throws ClientException
		 */
		public ConfigRegistry getConfigRegistryById(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String id) throws ClientException {
			ConfigRegistry configRegistry = null;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				configRegistry = client.getConfigRegistryById(id);
			}
			return configRegistry;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public ConfigRegistry getConfigRegistryByName(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String name) throws ClientException {
			ConfigRegistry configRegistry = null;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				configRegistry = client.getConfigRegistryByName(name);
			}
			return configRegistry;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param id
		 * @return
		 * @throws ClientException
		 */
		public boolean configRegistryExistsById(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String id) throws ClientException {
			boolean exists = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				exists = client.configRegistryExistsById(id);
			}
			return exists;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public boolean configRegistryExistsByName(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String name) throws ClientException {
			boolean exists = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				exists = client.configRegistryExistsByName(name);
			}
			return exists;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param type
		 * @param name
		 * @param properties
		 * @param generateUniqueName
		 * @return
		 * @throws ClientException
		 */
		public ConfigRegistry createConfigRegistry(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String type, String name, Map<String, Object> properties, boolean generateUniqueName) throws ClientException {
			ConfigRegistry configRegistry = null;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				configRegistry = client.createConfigRegistry(type, name, properties, generateUniqueName);
			}
			return configRegistry;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param id
		 * @param type
		 * @return
		 * @throws ClientException
		 */
		public boolean updateConfigRegistryType(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String id, String type) throws ClientException {
			boolean isUpdated = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				isUpdated = client.updateConfigRegistryType(id, type);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param id
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public boolean updateConfigRegistryName(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String id, String name) throws ClientException {
			boolean isUpdated = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				isUpdated = client.updateConfigRegistryName(id, name);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param id
		 * @param properties
		 * @return
		 * @throws ClientException
		 */
		public boolean setConfigRegistryProperties(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String id, Map<String, Object> properties) throws ClientException {
			boolean isUpdated = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				isUpdated = client.setConfigRegistryProperties(id, properties);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param id
		 * @param propertyNames
		 * @return
		 * @throws ClientException
		 */
		public boolean removeConfigRegistryProperties(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String id, List<String> propertyNames) throws ClientException {
			boolean isUpdated = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				isUpdated = client.removeConfigRegistryProperties(id, propertyNames);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param id
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteConfigRegistryById(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String id) throws ClientException {
			boolean isDeleted = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				isDeleted = client.deleteConfigRegistryById(id);
			}
			return isDeleted;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteConfigRegistryByName(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String name) throws ClientException {
			boolean isDeleted = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				isDeleted = client.deleteConfigRegistryByName(name);
			}
			return isDeleted;
		}

		// -----------------------------------------------------------------------------------
		// Config Elements
		// -----------------------------------------------------------------------------------
		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement[] listRootConfigElements(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId) throws ClientException {
			ConfigElement[] configElements = null;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				configElements = client.listRootConfigElements(configRegistryId);
			}
			if (configElements == null) {
				configElements = EMPTY_CONFIG_ELEMENTS;
			}
			return configElements;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param parentElementId
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement[] listConfigElements(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, String parentElementId) throws ClientException {
			ConfigElement[] configElements = null;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				configElements = client.listConfigElements(configRegistryId, parentElementId);
			}
			if (configElements == null) {
				configElements = EMPTY_CONFIG_ELEMENTS;
			}
			return configElements;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param parentPath
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement[] listConfigElements(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, Path parentPath) throws ClientException {
			ConfigElement[] configElements = null;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				configElements = client.listConfigElements(configRegistryId, parentPath);
			}
			if (configElements == null) {
				configElements = EMPTY_CONFIG_ELEMENTS;
			}
			return configElements;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement getConfigElement(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, String elementId) throws ClientException {
			ConfigElement configElement = null;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				configElement = client.getConfigElement(configRegistryId, elementId);
			}
			return configElement;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param path
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement getConfigElement(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, Path path) throws ClientException {
			ConfigElement configElement = null;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				configElement = client.getConfigElement(configRegistryId, path);
			}
			return configElement;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param parentElementId
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement getConfigElement(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, String parentElementId, String name) throws ClientException {
			ConfigElement configElement = null;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				configElement = client.getConfigElement(configRegistryId, parentElementId, name);
			}
			return configElement;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @return
		 * @throws ClientException
		 */
		public Path getConfigElementPath(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, String elementId) throws ClientException {
			Path path = null;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				path = client.getConfigElementPath(configRegistryId, elementId);
			}
			return path;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @return
		 * @throws ClientException
		 */
		public boolean configElementExists(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, String elementId) throws ClientException {
			boolean exists = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				exists = client.configElementExists(configRegistryId, elementId);
			}
			return exists;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param path
		 * @return
		 * @throws ClientException
		 */
		public boolean configElementExists(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, Path path) throws ClientException {
			boolean exists = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				exists = client.configElementExists(configRegistryId, path);
			}
			return exists;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param parentElementId
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public boolean configElementExists(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, String parentElementId, String name) throws ClientException {
			boolean exists = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				exists = client.configElementExists(configRegistryId, parentElementId, name);
			}
			return exists;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param path
		 * @param attributes
		 * @param generateUniqueName
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement createConfigElement(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, Path path, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException {
			ConfigElement configElement = null;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				configElement = client.createConfigElement(configRegistryId, path, attributes, generateUniqueName);
			}
			return configElement;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param parentElementId
		 * @param name
		 * @param attributes
		 * @param generateUniqueName
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement createConfigElement(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, String parentElementId, String name, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException {
			ConfigElement configElement = null;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				configElement = client.createConfigElement(configRegistryId, parentElementId, name, attributes, generateUniqueName);
			}
			return configElement;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @param newName
		 * @return
		 * @throws ClientException
		 */
		public boolean updateConfigElementName(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, String elementId, String newName) throws ClientException {
			boolean isUpdated = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				isUpdated = client.updateConfigElementName(configRegistryId, elementId, newName);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @param attributes
		 * @return
		 * @throws ClientException
		 */
		public boolean setConfigElementAttributes(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, String elementId, Map<String, Object> attributes) throws ClientException {
			boolean isUpdated = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				isUpdated = client.setConfigElementAttributes(configRegistryId, elementId, attributes);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @param attributeName
		 * @return
		 * @throws ClientException
		 */
		public boolean removeConfigElementAttributes(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, String elementId, List<String> attributeName) throws ClientException {
			boolean isUpdated = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				isUpdated = client.removeConfigElementAttributes(configRegistryId, elementId, attributeName);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteConfigElement(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, String elementId) throws ClientException {
			boolean isDeleted = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				isDeleted = client.deleteConfigElement(configRegistryId, elementId);
			}
			return isDeleted;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param serviceUrl
		 * @param accessToken
		 * @param configRegistryId
		 * @param path
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteConfigElement(ConfigRegistryClientResolver clientResolver, String serviceUrl, String accessToken, String configRegistryId, Path path) throws ClientException {
			boolean isDeleted = false;
			ConfigRegistryClient client = clientResolver.resolve(serviceUrl, accessToken);
			if (client != null) {
				isDeleted = client.deleteConfigElement(configRegistryId, path);
			}
			return isDeleted;
		}
	}

	public static class DATA_CAST {
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

		/**
		 * 
		 * @param clientResolver
		 * @param dataCastServiceUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public DataCastServiceMetadata getServiceMetadata(DataCastClientResolver clientResolver, String dataCastServiceUrl, String accessToken) throws ClientException {
			DataCastServiceMetadata metadata = null;
			DataCastClient dataCastClient = clientResolver.resolve(dataCastServiceUrl, accessToken);
			if (dataCastClient != null) {
				metadata = dataCastClient.getMetadata();
			}
			return metadata;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataCastServiceUrl
		 * @param accessToken
		 * @param channelId
		 * @return
		 * @throws ClientException
		 */
		public ChannelMetadata getChannelMetadataByChannelId(DataCastClientResolver clientResolver, String dataCastServiceUrl, String accessToken, String channelId) throws ClientException {
			ChannelMetadata channelMetadata = null;
			DataCastClient dataCastClient = clientResolver.resolve(dataCastServiceUrl, accessToken);
			if (dataCastClient != null) {
				dataCastClient.getChannelMetadataById(channelId);
			}
			return channelMetadata;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataCastServiceUrl
		 * @param accessToken
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public ChannelMetadata getChannelMetadataByChannelName(DataCastClientResolver clientResolver, String dataCastServiceUrl, String accessToken, String name) throws ClientException {
			ChannelMetadata channelMetadata = null;
			DataCastClient dataCastClient = clientResolver.resolve(dataCastServiceUrl, accessToken);
			if (dataCastClient != null) {
				dataCastClient.getChannelMetadataByName(name);
			}
			return channelMetadata;
		}
	}

	public static class DATA_TUBE {
		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public DataTubeServiceMetadata getServiceMetadata(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken) throws ClientException {
			DataTubeServiceMetadata metadata = null;
			DataTubeClient dataTubeClient = clientResolver.resolve(dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				metadata = dataTubeClient.getMetadata();
			}
			return metadata;
		}

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
