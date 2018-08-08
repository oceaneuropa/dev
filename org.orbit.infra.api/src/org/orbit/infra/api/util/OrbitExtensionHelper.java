package org.orbit.infra.api.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.extensionregistry.ExtensionItem;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClient;

public class OrbitExtensionHelper {

	public static OrbitExtensionHelper INSTANCE = new OrbitExtensionHelper();

	/**
	 * Get all extension items from a platform.
	 * 
	 * @param extensionRegistryUrl
	 * @param platformId
	 * @return
	 * @throws IOException
	 */
	public List<ExtensionItem> getExtensionItemsOfPlatform(String extensionRegistryUrl, String platformId) throws IOException {
		List<ExtensionItem> extensionItems = null;
		if (extensionRegistryUrl != null && platformId != null) {
			ExtensionRegistryClient extensionRegistry = getExtensionRegistry(extensionRegistryUrl);
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
	 * @param platformId
	 * @param extensionTypeId
	 * @return
	 * @throws IOException
	 */
	public List<ExtensionItem> getExtensionItemsOfPlatform(String extensionRegistryUrl, String platformId, String extensionTypeId) throws IOException {
		List<ExtensionItem> extensionItems = null;
		if (extensionRegistryUrl != null && platformId != null) {
			ExtensionRegistryClient extensionRegistry = getExtensionRegistry(extensionRegistryUrl);
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
	 * Get extension ids of specified extension type from a platform.
	 * 
	 * @param extensionRegistryUrl
	 * @param platformId
	 * @param typeId
	 * @return
	 * @throws IOException
	 */
	public List<String> getExtensionIdsOfPlatform(String extensionRegistryUrl, String platformId, String typeId) throws IOException {
		List<String> extensionIds = new ArrayList<String>();
		if (extensionRegistryUrl != null && platformId != null) {
			ExtensionRegistryClient extensionRegistry = getExtensionRegistry(extensionRegistryUrl);
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
	 * @param indexServiceUrl
	 * @return
	 */
	protected ExtensionRegistryClient getExtensionRegistry(String extensionRegistryUrl) {
		ExtensionRegistryClient extensionRegistry = null;
		if (extensionRegistryUrl != null) {
			extensionRegistry = InfraClients.getInstance().getExtensionRegistry(extensionRegistryUrl);
		}
		return extensionRegistry;
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
