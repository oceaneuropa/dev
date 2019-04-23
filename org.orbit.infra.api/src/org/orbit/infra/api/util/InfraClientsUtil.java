package org.orbit.infra.api.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.configregistry.ConfigElement;
import org.orbit.infra.api.configregistry.ConfigRegistry;
import org.orbit.infra.api.configregistry.ConfigRegistryClient;
import org.orbit.infra.api.configregistry.ConfigRegistryClientResolver;
import org.orbit.infra.api.datacast.ChannelMetadata;
import org.orbit.infra.api.datacast.ChannelStatus;
import org.orbit.infra.api.datacast.DataCastClient;
import org.orbit.infra.api.datacast.DataCastClientResolver;
import org.orbit.infra.api.datacast.DataCastServiceMetadata;
import org.orbit.infra.api.datatube.DataTubeClient;
import org.orbit.infra.api.datatube.DataTubeClientResolver;
import org.orbit.infra.api.datatube.DataTubeServiceMetadata;
import org.orbit.infra.api.datatube.RuntimeChannel;
import org.orbit.infra.api.extensionregistry.ExtensionItem;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClient;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.origin.common.model.AccountConfig;
import org.origin.common.resource.Path;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.rest.model.ServiceMetadata;

public class InfraClientsUtil {

	public static INDEX_SERVICE INDEX_SERVICE = new INDEX_SERVICE();
	public static EXTENSION_REGISTRY EXTENSION_REGISTRY = new EXTENSION_REGISTRY();
	public static CONFIG_REGISTRY CONFIG_REGISTRY = new CONFIG_REGISTRY();
	public static DATA_CAST DATA_CAST = new DATA_CAST();
	public static DATA_TUBE DATA_TUBE = new DATA_TUBE();

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

	public static class DATA_CAST {
		public static ChannelMetadata[] EMPTY_CHANNEL_METADATAS = new ChannelMetadata[0];

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
		 */
		public DataCastClient getDataCastClient(DataCastClientResolver clientResolver, String dataCastServiceUrl, String accessToken) {
			DataCastClient dataCastClient = null;
			if (clientResolver != null && dataCastServiceUrl != null) {
				dataCastClient = clientResolver.resolve(dataCastServiceUrl, accessToken);
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
			DataCastClient dataCastClient = getDataCastClient(clientResolver, dataCastServiceUrl, accessToken);
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
		 * @param comparator
		 * @return
		 * @throws ClientException
		 */
		public ChannelMetadata[] getChannelMetadatas(DataCastClientResolver clientResolver, String dataCastServiceUrl, String accessToken, Comparator<ChannelMetadata> comparator) throws ClientException {
			ChannelMetadata[] channelMetadatas = null;
			DataCastClient dataCastClient = getDataCastClient(clientResolver, dataCastServiceUrl, accessToken);
			if (dataCastClient != null) {
				channelMetadatas = dataCastClient.getChannelMetadatas();
			}
			if (channelMetadatas == null) {
				channelMetadatas = EMPTY_CHANNEL_METADATAS;
			}
			if (comparator != null && channelMetadatas != null && channelMetadatas.length > 1) {
				Arrays.sort(channelMetadatas, comparator);
			}
			return channelMetadatas;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataCastServiceUrl
		 * @param accessToken
		 * @param dataTubeId
		 * @param comparator
		 * @return
		 * @throws ClientException
		 */
		public ChannelMetadata[] getChannelMetadatas(DataCastClientResolver clientResolver, String dataCastServiceUrl, String accessToken, String dataTubeId, Comparator<ChannelMetadata> comparator) throws ClientException {
			ChannelMetadata[] channelMetadatas = null;
			DataCastClient dataCastClient = getDataCastClient(clientResolver, dataCastServiceUrl, accessToken);
			if (dataCastClient != null) {
				channelMetadatas = dataCastClient.getChannelMetadatas(dataTubeId);
			}
			if (channelMetadatas == null) {
				channelMetadatas = EMPTY_CHANNEL_METADATAS;
			}
			if (comparator != null && channelMetadatas != null && channelMetadatas.length > 1) {
				Arrays.sort(channelMetadatas, comparator);
			}
			return channelMetadatas;
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
			DataCastClient dataCastClient = getDataCastClient(clientResolver, dataCastServiceUrl, accessToken);
			if (dataCastClient != null) {
				channelMetadata = dataCastClient.getChannelMetadataById(channelId);
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
			DataCastClient dataCastClient = getDataCastClient(clientResolver, dataCastServiceUrl, accessToken);
			if (dataCastClient != null) {
				channelMetadata = dataCastClient.getChannelMetadataByName(name);
			}
			return channelMetadata;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataCastServiceUrl
		 * @param accessToken
		 * @param dataTubeId
		 * @param name
		 * @param channelStatus
		 * @param accessType
		 * @param accessCode
		 * @param ownerAccountId
		 * @param accountConfigs
		 * @param properties
		 * @return
		 * @throws ClientException
		 */
		public ChannelMetadata createChannelMetadata(DataCastClientResolver clientResolver, String dataCastServiceUrl, String accessToken, String dataTubeId, String name, ChannelStatus channelStatus, String accessType, String accessCode, String ownerAccountId, List<AccountConfig> accountConfigs, Map<String, Object> properties) throws ClientException {
			ChannelMetadata channelMetadata = null;
			DataCastClient dataCastClient = getDataCastClient(clientResolver, dataCastServiceUrl, accessToken);
			if (dataCastClient != null) {
				channelMetadata = dataCastClient.createChannelMetadata(dataTubeId, name, channelStatus, accessType, accessCode, ownerAccountId, accountConfigs, properties);
			}
			return channelMetadata;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataCastServiceUrl
		 * @param accessToken
		 * @param channelId
		 * @param updateDataTubeId
		 * @param dataTubeId
		 * @param updateName
		 * @param name
		 * @param updateAccessType
		 * @param accessType
		 * @param updateAccessCode
		 * @param accessCode
		 * @param updateOwnerAccountId
		 * @param ownerAccountId
		 * @return
		 * @throws ClientException
		 */
		public boolean updateChannelMetadata(DataCastClientResolver clientResolver, String dataCastServiceUrl, String accessToken, String channelId, boolean updateDataTubeId, String dataTubeId, boolean updateName, String name, boolean updateAccessType, String accessType, boolean updateAccessCode, String accessCode, boolean updateOwnerAccountId, String ownerAccountId) throws ClientException {
			boolean isUpdated = false;
			DataCastClient dataCastClient = getDataCastClient(clientResolver, dataCastServiceUrl, accessToken);
			if (dataCastClient != null) {
				isUpdated = dataCastClient.updateChannelMetadataById(channelId, updateDataTubeId, dataTubeId, updateName, name, updateAccessType, accessType, updateAccessCode, accessCode, updateOwnerAccountId, ownerAccountId);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataCastServiceUrl
		 * @param accessToken
		 * @param channelId
		 * @param channelStatus
		 * @param append
		 * @return
		 * @throws ClientException
		 */
		public boolean setChannelMetadataStatusById(DataCastClientResolver clientResolver, String dataCastServiceUrl, String accessToken, String channelId, ChannelStatus channelStatus, boolean append) throws ClientException {
			boolean isUpdated = false;
			DataCastClient dataCastClient = getDataCastClient(clientResolver, dataCastServiceUrl, accessToken);
			if (dataCastClient != null) {
				isUpdated = dataCastClient.setChannelMetadataStatusById(channelId, channelStatus, append);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataCastServiceUrl
		 * @param accessToken
		 * @param channelId
		 * @param channelStatus
		 * @return
		 * @throws ClientException
		 */
		public boolean clearChannelMetadataStatusById(DataCastClientResolver clientResolver, String dataCastServiceUrl, String accessToken, String channelId, ChannelStatus channelStatus) throws ClientException {
			boolean isUpdated = false;
			DataCastClient dataCastClient = getDataCastClient(clientResolver, dataCastServiceUrl, accessToken);
			if (dataCastClient != null) {
				isUpdated = dataCastClient.clearChannelMetadataStatusById(channelId, channelStatus);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataCastServiceUrl
		 * @param accessToken
		 * @param channelId
		 * @param accountConfigs
		 * @param appendAccountConfigs
		 * @param appendAccountConfig
		 * @return
		 * @throws ClientException
		 */
		public boolean setChannelMetadataAccountConfigsById(DataCastClientResolver clientResolver, String dataCastServiceUrl, String accessToken, String channelId, List<AccountConfig> accountConfigs, boolean appendAccountConfigs, boolean appendAccountConfig) throws ClientException {
			boolean isUpdated = false;
			DataCastClient dataCastClient = getDataCastClient(clientResolver, dataCastServiceUrl, accessToken);
			if (dataCastClient != null) {
				isUpdated = dataCastClient.setChannelMetadataAccountConfigsById(channelId, accountConfigs, appendAccountConfigs, appendAccountConfig);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataCastServiceUrl
		 * @param accessToken
		 * @param channelId
		 * @param accountIds
		 * @return
		 * @throws ClientException
		 */
		public boolean removeChannelMetadataAccountConfigsById(DataCastClientResolver clientResolver, String dataCastServiceUrl, String accessToken, String channelId, List<String> accountIds) throws ClientException {
			boolean isUpdated = false;
			DataCastClient dataCastClient = getDataCastClient(clientResolver, dataCastServiceUrl, accessToken);
			if (dataCastClient != null) {
				isUpdated = dataCastClient.removeChannelMetadataAccountConfigsById(channelId, accountIds);
			}
			return isUpdated;
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
		public boolean deleteChannelMetadataById(DataCastClientResolver clientResolver, String dataCastServiceUrl, String accessToken, String channelId) throws ClientException {
			boolean isDeleted = false;
			DataCastClient dataCastClient = getDataCastClient(clientResolver, dataCastServiceUrl, accessToken);
			if (dataCastClient != null) {
				isDeleted = dataCastClient.deleteChannelMetadataById(channelId);
			}
			return isDeleted;
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
		public boolean deleteChannelMetadataByName(DataCastClientResolver clientResolver, String dataCastServiceUrl, String accessToken, String name) throws ClientException {
			boolean isDeleted = false;
			DataCastClient dataCastClient = getDataCastClient(clientResolver, dataCastServiceUrl, accessToken);
			if (dataCastClient != null) {
				isDeleted = dataCastClient.deleteChannelMetadataByName(name);
			}
			return isDeleted;
		}
	}

	public static class DATA_TUBE {
		public static RuntimeChannel[] EMPTY_RUNTIME_CHANNELS = new RuntimeChannel[0];

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

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @return
		 */
		public DataTubeClient getDataTubeClient(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken) {
			DataTubeClient dataTubeClient = null;
			if (clientResolver != null && dataTubeServiceUrl != null) {
				dataTubeClient = clientResolver.resolve(dataTubeServiceUrl, accessToken);
			}
			return dataTubeClient;
		}

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
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				metadata = dataTubeClient.getMetadata();
			}
			return metadata;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param channelId
		 * @param senderId
		 * @param message
		 * @return
		 * @throws ClientException
		 */
		public boolean send(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String channelId, String senderId, String message) throws ClientException {
			boolean succeed = false;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				succeed = dataTubeClient.send(channelId, senderId, message);
			}
			return succeed;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param comparator
		 * @return
		 * @throws ClientException
		 */
		public RuntimeChannel[] getRuntimeChannels(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, Comparator<RuntimeChannel> comparator) throws ClientException {
			RuntimeChannel[] runtimeChannels = null;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				runtimeChannels = dataTubeClient.getRuntimeChannels();
			}
			if (runtimeChannels == null) {
				runtimeChannels = EMPTY_RUNTIME_CHANNELS;
			}
			if (comparator != null && runtimeChannels != null && runtimeChannels.length > 1) {
				Arrays.sort(runtimeChannels, comparator);
			}
			return runtimeChannels;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param comparator
		 * @return
		 * @throws ClientException
		 */
		public Map<String, RuntimeChannel> getRuntimeChannelsMap(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, Comparator<RuntimeChannel> comparator) throws ClientException {
			Map<String, RuntimeChannel> runtimeChannelsMap = new LinkedHashMap<String, RuntimeChannel>();
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				RuntimeChannel[] runtimeChannels = dataTubeClient.getRuntimeChannels();
				if (runtimeChannels != null) {
					if (comparator != null && runtimeChannels.length > 1) {
						Arrays.sort(runtimeChannels, comparator);
					}
					for (RuntimeChannel runtimeChannel : runtimeChannels) {
						String channelId = runtimeChannel.getChannelId();
						runtimeChannelsMap.put(channelId, runtimeChannel);
					}
				}
			}
			return runtimeChannelsMap;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param channelId
		 * @param createIfNotExist
		 * @return
		 * @throws ClientException
		 */
		public RuntimeChannel getRuntimeChannelId(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String channelId, boolean createIfNotExist) throws ClientException {
			RuntimeChannel runtimeChannel = null;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				runtimeChannel = dataTubeClient.getRuntimeChannelId(channelId, createIfNotExist);
			}
			return runtimeChannel;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param name
		 * @param createIfNotExist
		 * @return
		 * @throws ClientException
		 */
		public RuntimeChannel getRuntimeChannelByName(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String name, boolean createIfNotExist) throws ClientException {
			RuntimeChannel runtimeChannel = null;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				runtimeChannel = dataTubeClient.getRuntimeChannelByName(name, createIfNotExist);
			}
			return runtimeChannel;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param channelId
		 * @return
		 * @throws ClientException
		 */
		public boolean runtimeChannelExistsById(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String channelId) throws ClientException {
			boolean exists = false;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				exists = dataTubeClient.runtimeChannelExistsById(channelId);
			}
			return exists;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public boolean runtimeChannelExistsByName(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String name) throws ClientException {
			boolean exists = false;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				exists = dataTubeClient.runtimeChannelExistsByName(name);
			}
			return exists;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param channelId
		 * @param useExisting
		 * @return
		 * @throws ClientException
		 */
		public RuntimeChannel createRuntimeChannelId(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String channelId, boolean useExisting) throws ClientException {
			RuntimeChannel runtimeChannel = null;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				runtimeChannel = dataTubeClient.createRuntimeChannelById(channelId, useExisting);
			}
			return runtimeChannel;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param name
		 * @param useExisting
		 * @return
		 * @throws ClientException
		 */
		public RuntimeChannel createRuntimeChannelByName(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String name, boolean useExisting) throws ClientException {
			RuntimeChannel runtimeChannel = null;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				runtimeChannel = dataTubeClient.createRuntimeChannelByName(name, useExisting);
			}
			return runtimeChannel;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param channelId
		 * @return
		 * @throws ClientException
		 */
		public boolean startRuntimeChannelById(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String channelId) throws ClientException {
			boolean succeed = false;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				succeed = dataTubeClient.startRuntimeChannelById(channelId);
			}
			return succeed;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public boolean startRuntimeChannelByName(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String name) throws ClientException {
			boolean succeed = false;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				succeed = dataTubeClient.startRuntimeChannelByName(name);
			}
			return succeed;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param channelId
		 * @return
		 * @throws ClientException
		 */
		public boolean suspendRuntimeChannelById(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String channelId) throws ClientException {
			boolean succeed = false;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				succeed = dataTubeClient.suspendRuntimeChannelById(channelId);
			}
			return succeed;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public boolean suspendRuntimeChannelByName(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String name) throws ClientException {
			boolean succeed = false;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				succeed = dataTubeClient.suspendRuntimeChannelByName(name);
			}
			return succeed;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param channelId
		 * @return
		 * @throws ClientException
		 */
		public boolean stopRuntimeChannelById(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String channelId) throws ClientException {
			boolean succeed = false;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				succeed = dataTubeClient.stopRuntimeChannelById(channelId);
			}
			return succeed;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public boolean stopRuntimeChannelByName(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String name) throws ClientException {
			boolean succeed = false;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				succeed = dataTubeClient.stopRuntimeChannelByName(name);
			}
			return succeed;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param channelId
		 * @return
		 * @throws ClientException
		 */
		public boolean syncRuntimeChannelById(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String channelId) throws ClientException {
			boolean succeed = false;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				succeed = dataTubeClient.syncRuntimeChannelById(channelId);
			}
			return succeed;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public boolean syncRuntimeChannelByName(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String name) throws ClientException {
			boolean succeed = false;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				succeed = dataTubeClient.syncRuntimeChannelByName(name);
			}
			return succeed;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param channelId
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteRuntimeChannelById(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String channelId) throws ClientException {
			boolean isDeleted = false;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				isDeleted = dataTubeClient.deleteRuntimeChannelId(channelId);
			}
			return isDeleted;
		}

		/**
		 * 
		 * @param clientResolver
		 * @param dataTubeServiceUrl
		 * @param accessToken
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteRuntimeChannelByName(DataTubeClientResolver clientResolver, String dataTubeServiceUrl, String accessToken, String name) throws ClientException {
			boolean isDeleted = false;
			DataTubeClient dataTubeClient = getDataTubeClient(clientResolver, dataTubeServiceUrl, accessToken);
			if (dataTubeClient != null) {
				isDeleted = dataTubeClient.deleteRuntimeChannelByName(name);
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
