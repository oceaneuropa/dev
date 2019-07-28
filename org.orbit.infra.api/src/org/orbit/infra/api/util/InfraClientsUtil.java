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
import org.orbit.infra.api.extensionregistry.ExtensionItem;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClient;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.origin.common.resource.Path;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.ServiceMetadata;

public class InfraClientsUtil {

	public static INDEX_SERVICE INDEX_SERVICE = new INDEX_SERVICE();
	public static EXTENSION_REGISTRY EXTENSION_REGISTRY = new EXTENSION_REGISTRY();
	public static CONFIG_REGISTRY CONFIG_REGISTRY = new CONFIG_REGISTRY();

	public static class INDEX_SERVICE {
		/**
		 * 
		 * @param accessToken
		 * @return
		 */
		public IndexServiceClient getIndexServiceClient(String accessToken) {
			String indexServiceUrl = InfraServicesPropertiesHandler.getInstance().getIndexServiceURL();
			IndexServiceClient indexService = InfraClients.getInstance().getIndexService(indexServiceUrl, accessToken);
			return indexService;
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

		/**
		 * Get all index items of a indexer.
		 * 
		 * @param indexServiceUrl
		 * @param accessToken
		 * @param indexerId
		 * @return
		 * @throws IOException
		 */
		public List<IndexItem> getIndexItems(String accessToken, String indexerId) throws IOException {
			List<IndexItem> indexItems = null;
			if (indexerId != null) {
				IndexServiceClient indexService = getIndexServiceClient(accessToken);
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
		public List<IndexItem> getIndexItemsOfPlatform(String accessToken, String indexerId, String platformId) throws IOException {
			List<IndexItem> indexItems = new ArrayList<IndexItem>();
			if (indexerId != null) {
				IndexServiceClient indexService = getIndexServiceClient(accessToken);
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
		public IndexItem getIndexItemOfPlatform(String accessToken, String platformId) throws IOException {
			IndexItem platformIndexItem = null;
			if (platformId != null) {
				IndexServiceClient indexService = getIndexServiceClient(accessToken);
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
		public Map<String, IndexItem> getPlatformIdToIndexItem(String accessToken, String parentPlatformId, String... platformTypes) throws IOException {
			Map<String, IndexItem> platformIdToIndexItem = new HashMap<String, IndexItem>();

			IndexServiceClient indexService = getIndexServiceClient(accessToken);
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
		public IndexItem getIndexItem(String accessToken, String parentPlatformId, String platformId, String... platformTypes) throws IOException {
			IndexItem indexItem = null;

			if (parentPlatformId != null && platformId != null) {
				IndexServiceClient indexService = getIndexServiceClient(accessToken);
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
		public boolean deleteIndexItem(String accessToken, String indexProviderId, Integer indexItemId) throws IOException {
			boolean isDeleted = false;
			if (indexProviderId != null && !indexProviderId.isEmpty() && indexItemId != null) {
				IndexServiceClient indexService = getIndexServiceClient(accessToken);
				if (indexService != null) {
					isDeleted = indexService.deleteIndexItem(indexProviderId, indexItemId);
				}
			}
			return isDeleted;
		}
	}

	public static class EXTENSION_REGISTRY {
		/**
		 * 
		 * @param extensionRegistryUrl
		 * @param accessToken
		 * @return
		 */
		public ExtensionRegistryClient getExtensionRegistryClient(String accessToken) {
			String extensionRegistryUrl = InfraServicesPropertiesHandler.getInstance().getExtensionRegistryURL();
			ExtensionRegistryClient extensionRegistry = InfraClients.getInstance().getExtensionRegistryClient(extensionRegistryUrl, accessToken);
			return extensionRegistry;
		}

		// /**
		// *
		// * @param extensionRegistryUrl
		// * @param accessToken
		// * @return
		// */
		// protected ExtensionRegistryClient getExtensionRegistryClient(String extensionRegistryUrl, String accessToken) {
		// ExtensionRegistryClient extensionRegistry = InfraClients.getInstance().getExtensionRegistryClient(extensionRegistryUrl, accessToken);
		// return extensionRegistry;
		// }

		/**
		 * Get all extension items from a platform.
		 * 
		 * @param accessToken
		 * @param platformId
		 * @return
		 * @throws IOException
		 */
		public List<ExtensionItem> getExtensionItemsOfPlatform(String accessToken, String platformId) throws IOException {
			List<ExtensionItem> extensionItems = null;
			if (platformId != null) {
				ExtensionRegistryClient extensionRegistry = getExtensionRegistryClient(accessToken);
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
		 * @param accessToken
		 * @param platformId
		 * @param extensionTypeId
		 * @return
		 * @throws IOException
		 */
		public List<ExtensionItem> getExtensionItemsOfPlatform(String accessToken, String platformId, String extensionTypeId) throws IOException {
			List<ExtensionItem> extensionItems = null;
			if (platformId != null) {
				ExtensionRegistryClient extensionRegistry = getExtensionRegistryClient(accessToken);
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
		 * @param accessToken
		 * @param platformId
		 * @param typeId
		 * @return
		 * @throws IOException
		 */
		public List<String> getExtensionIdsOfPlatform(String accessToken, String platformId, String typeId) throws IOException {
			List<String> extensionIds = new ArrayList<String>();
			if (platformId != null) {
				ExtensionRegistryClient extensionRegistry = getExtensionRegistryClient(accessToken);
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
	}

	public static class CONFIG_REGISTRY {
		public ConfigRegistry[] EMPTY_CONFIG_REGISTRIES = new ConfigRegistry[0];
		public ConfigElement[] EMPTY_CONFIG_ELEMENTS = new ConfigElement[0];

		/**
		 * 
		 * @param configRegistryServiceUrl
		 * @param accessToken
		 * @return
		 */
		public ConfigRegistryClient getConfigRegistryClient(String accessToken) {
			String configRegistryServiceUrl = InfraServicesPropertiesHandler.getInstance().getConfigRegistryURL();
			ConfigRegistryClient configRegistryClient = InfraClients.getInstance().getConfigRegistryClient(configRegistryServiceUrl, accessToken);
			return configRegistryClient;
		}

		// /**
		// *
		// * @param configRegistryServiceUrl
		// * @param accessToken
		// * @return
		// */
		// protected ConfigRegistryClient getConfigRegistryClient(String configRegistryServiceUrl, String accessToken) {
		// ConfigRegistryClient configRegistryClient = InfraClients.getInstance().getConfigRegistryClient(configRegistryServiceUrl, accessToken);
		// return configRegistryClient;
		// }

		/**
		 * 
		 * @param clientResolver
		 * @param configRegistryServiceUrl
		 * @param accessToken
		 * @return
		 */
		public ConfigRegistryClient getConfigRegistryClient(ConfigRegistryClientResolver clientResolver, String accessToken) {
			ConfigRegistryClient configRegistryClient = null;
			if (clientResolver != null) {
				configRegistryClient = clientResolver.resolve(accessToken);
			}
			return configRegistryClient;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public ServiceMetadata getServiceMetadata(ConfigRegistryClientResolver clientResolver, String accessToken) throws ClientException {
			ServiceMetadata metadata = null;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
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
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public ConfigRegistry[] getConfigRegistries(ConfigRegistryClientResolver clientResolver, String accessToken) throws ClientException {
			ConfigRegistry[] configRegistries = null;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
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
		 * @param accessToken
		 * @param type
		 * @return
		 * @throws ClientException
		 */
		public ConfigRegistry[] getConfigRegistries(ConfigRegistryClientResolver clientResolver, String accessToken, String type) throws ClientException {
			ConfigRegistry[] configRegistries = null;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
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
		 * @param accessToken
		 * @param id
		 * @return
		 * @throws ClientException
		 */
		public ConfigRegistry getConfigRegistryById(ConfigRegistryClientResolver clientResolver, String accessToken, String id) throws ClientException {
			ConfigRegistry configRegistry = null;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				configRegistry = client.getConfigRegistryById(id);
			}
			return configRegistry;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public ConfigRegistry getConfigRegistryByName(ConfigRegistryClientResolver clientResolver, String accessToken, String name) throws ClientException {
			ConfigRegistry configRegistry = null;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				configRegistry = client.getConfigRegistryByName(name);
			}
			return configRegistry;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param id
		 * @return
		 * @throws ClientException
		 */
		public boolean configRegistryExistsById(ConfigRegistryClientResolver clientResolver, String accessToken, String id) throws ClientException {
			boolean exists = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				exists = client.configRegistryExistsById(id);
			}
			return exists;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public boolean configRegistryExistsByName(ConfigRegistryClientResolver clientResolver, String accessToken, String name) throws ClientException {
			boolean exists = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				exists = client.configRegistryExistsByName(name);
			}
			return exists;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param type
		 * @param name
		 * @param properties
		 * @param generateUniqueName
		 * @return
		 * @throws ClientException
		 */
		public ConfigRegistry createConfigRegistry(ConfigRegistryClientResolver clientResolver, String accessToken, String type, String name, Map<String, Object> properties, boolean generateUniqueName) throws ClientException {
			ConfigRegistry configRegistry = null;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				configRegistry = client.createConfigRegistry(type, name, properties, generateUniqueName);
			}
			return configRegistry;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param id
		 * @param type
		 * @return
		 * @throws ClientException
		 */
		public boolean updateConfigRegistryType(ConfigRegistryClientResolver clientResolver, String accessToken, String id, String type) throws ClientException {
			boolean isUpdated = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				isUpdated = client.updateConfigRegistryType(id, type);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param id
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public boolean updateConfigRegistryName(ConfigRegistryClientResolver clientResolver, String accessToken, String id, String name) throws ClientException {
			boolean isUpdated = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				isUpdated = client.updateConfigRegistryName(id, name);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param oldName
		 * @param name
		 * @param value
		 * @return
		 * @throws ClientException
		 */
		public boolean setConfigRegistryProperty(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String oldName, String name, Object value) throws ClientException {
			boolean succeed = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				succeed = client.setConfigRegistryProperty(configRegistryId, oldName, name, value);
			}
			return succeed;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param id
		 * @param properties
		 * @return
		 * @throws ClientException
		 */
		public boolean setConfigRegistryProperties(ConfigRegistryClientResolver clientResolver, String accessToken, String id, Map<String, Object> properties) throws ClientException {
			boolean isUpdated = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				isUpdated = client.setConfigRegistryProperties(id, properties);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param id
		 * @param propertyNames
		 * @return
		 * @throws ClientException
		 */
		public boolean removeConfigRegistryProperties(ConfigRegistryClientResolver clientResolver, String accessToken, String id, List<String> propertyNames) throws ClientException {
			boolean isUpdated = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				isUpdated = client.removeConfigRegistryProperties(id, propertyNames);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param id
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteConfigRegistryById(ConfigRegistryClientResolver clientResolver, String accessToken, String id) throws ClientException {
			boolean isDeleted = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				isDeleted = client.deleteConfigRegistryById(id);
			}
			return isDeleted;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteConfigRegistryByName(ConfigRegistryClientResolver clientResolver, String accessToken, String name) throws ClientException {
			boolean isDeleted = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
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
		 * @param accessToken
		 * @param configRegistryId
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement[] listRootConfigElements(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId) throws ClientException {
			ConfigElement[] configElements = null;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
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
		 * @param accessToken
		 * @param configRegistryId
		 * @param parentElementId
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement[] listConfigElements(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String parentElementId) throws ClientException {
			ConfigElement[] configElements = null;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
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
		 * @param accessToken
		 * @param configRegistryId
		 * @param parentPath
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement[] listConfigElements(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, Path parentPath) throws ClientException {
			ConfigElement[] configElements = null;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
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
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement getConfigElement(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId) throws ClientException {
			ConfigElement configElement = null;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				configElement = client.getConfigElement(configRegistryId, elementId);
			}
			return configElement;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param path
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement getConfigElement(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, Path path) throws ClientException {
			ConfigElement configElement = null;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				configElement = client.getConfigElement(configRegistryId, path);
			}
			return configElement;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param parentElementId
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement getConfigElement(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String parentElementId, String name) throws ClientException {
			ConfigElement configElement = null;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				configElement = client.getConfigElement(configRegistryId, parentElementId, name);
			}
			return configElement;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @return
		 * @throws ClientException
		 */
		public Path getConfigElementPath(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId) throws ClientException {
			Path path = null;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				path = client.getConfigElementPath(configRegistryId, elementId);
			}
			return path;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @return
		 * @throws ClientException
		 */
		public boolean configElementExists(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId) throws ClientException {
			boolean exists = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				exists = client.configElementExists(configRegistryId, elementId);
			}
			return exists;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param path
		 * @return
		 * @throws ClientException
		 */
		public boolean configElementExists(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, Path path) throws ClientException {
			boolean exists = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				exists = client.configElementExists(configRegistryId, path);
			}
			return exists;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param parentElementId
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public boolean configElementExists(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String parentElementId, String name) throws ClientException {
			boolean exists = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				exists = client.configElementExists(configRegistryId, parentElementId, name);
			}
			return exists;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param path
		 * @param attributes
		 * @param generateUniqueName
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement createConfigElement(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, Path path, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException {
			ConfigElement configElement = null;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				configElement = client.createConfigElement(configRegistryId, path, attributes, generateUniqueName);
			}
			return configElement;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param parentElementId
		 * @param name
		 * @param attributes
		 * @param generateUniqueName
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement createConfigElement(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String parentElementId, String name, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException {
			ConfigElement configElement = null;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				configElement = client.createConfigElement(configRegistryId, parentElementId, name, attributes, generateUniqueName);
			}
			return configElement;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @param newName
		 * @return
		 * @throws ClientException
		 */
		public boolean updateConfigElementName(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId, String newName) throws ClientException {
			boolean isUpdated = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				isUpdated = client.updateConfigElementName(configRegistryId, elementId, newName);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @param oldAttributeName
		 * @param attributeName
		 * @param attributeValue
		 * @return
		 * @throws ClientException
		 */
		public boolean setConfigElementAttribute(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId, String oldAttributeName, String attributeName, Object attributeValue) throws ClientException {
			boolean succeed = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				succeed = client.setConfigElementAttribute(configRegistryId, elementId, oldAttributeName, attributeName, attributeValue);
			}
			return succeed;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @param attributes
		 * @return
		 * @throws ClientException
		 */
		public boolean setConfigElementAttributes(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId, Map<String, Object> attributes) throws ClientException {
			boolean succeed = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				succeed = client.setConfigElementAttributes(configRegistryId, elementId, attributes);
			}
			return succeed;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @param attributeName
		 * @return
		 * @throws ClientException
		 */
		public boolean removeConfigElementAttribute(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId, String attributeName) throws ClientException {
			boolean succeed = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				succeed = client.removeConfigElementAttribute(configRegistryId, elementId, attributeName);
			}
			return succeed;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @param attributeNames
		 * @return
		 * @throws ClientException
		 */
		public boolean removeConfigElementAttributes(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId, List<String> attributeNames) throws ClientException {
			boolean succeed = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				succeed = client.removeConfigElementAttributes(configRegistryId, elementId, attributeNames);
			}
			return succeed;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param elementId
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteConfigElement(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId) throws ClientException {
			boolean isDeleted = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				isDeleted = client.deleteConfigElement(configRegistryId, elementId);
			}
			return isDeleted;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param accessToken
		 * @param configRegistryId
		 * @param path
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteConfigElement(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, Path path) throws ClientException {
			boolean isDeleted = false;
			ConfigRegistryClient client = getConfigRegistryClient(clientResolver, accessToken);
			if (client != null) {
				isDeleted = client.deleteConfigElement(configRegistryId, path);
			}
			return isDeleted;
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
