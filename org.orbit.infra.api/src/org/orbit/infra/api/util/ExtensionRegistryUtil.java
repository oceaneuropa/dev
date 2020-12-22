package org.orbit.infra.api.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.orbit.infra.api.extensionregistry.ExtensionItem;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClient;

public class ExtensionRegistryUtil {

	/**
	 * 
	 * @param accessToken
	 * @return
	 */
	public static ExtensionRegistryClient getClient(String accessToken) {
		String extensionRegistryUrl = InfraConfigPropertiesHandler.getInstance().getExtensionRegistryURL();
		ExtensionRegistryClient extensionRegistry = InfraClients.getInstance().getExtensionRegistryClient(extensionRegistryUrl, accessToken);
		return extensionRegistry;
	}

	/**
	 * Get all extension items from a platform.
	 * 
	 * @param accessToken
	 * @param platformId
	 * @return
	 * @throws IOException
	 */
	public static List<ExtensionItem> getExtensionItems(String accessToken, String platformId) throws IOException {
		List<ExtensionItem> extensionItems = null;
		if (platformId != null) {
			ExtensionRegistryClient extensionRegistry = getClient(accessToken);
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
	 * @param typeId
	 * @return
	 * @throws IOException
	 */
	public static List<ExtensionItem> getExtensionItems(String accessToken, String platformId, String typeId) throws IOException {
		List<ExtensionItem> extensionItems = null;
		if (platformId != null) {
			ExtensionRegistryClient extensionRegistry = getClient(accessToken);
			if (extensionRegistry != null) {
				extensionItems = extensionRegistry.getExtensionItems(platformId, typeId);
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
	public static List<String> getExtensionIds(String accessToken, String platformId, String typeId) throws IOException {
		List<String> extensionIds = new ArrayList<String>();
		if (platformId != null) {
			ExtensionRegistryClient extensionRegistry = getClient(accessToken);
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
	public static Map<String, List<ExtensionItem>> toMap(List<ExtensionItem> extensionItems) throws IOException {
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
	 * @param accessToken
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @return
	 * @throws IOException
	 */
	public static boolean removeExtensionItem(String accessToken, String platformId, String typeId, String extensionId) throws IOException {
		boolean succeed = false;
		if (platformId != null && typeId != null && extensionId != null) {
			ExtensionRegistryClient extensionRegistry = getClient(accessToken);
			if (extensionRegistry != null) {
				succeed = extensionRegistry.removeExtensionItem(platformId, typeId, extensionId);
			}
		}
		return succeed;
	}

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
