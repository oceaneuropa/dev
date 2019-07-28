package org.orbit.infra.runtime.util;

import java.util.Map;

import org.orbit.infra.model.configregistry.ConfigElementDTO;
import org.orbit.infra.model.configregistry.ConfigRegistryDTO;
import org.orbit.infra.model.extensionregistry.ExtensionItemDTO;
import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.model.indexes.IndexItemDTO;
import org.orbit.infra.runtime.configregistry.service.ConfigElement;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryMetadata;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionItem;
import org.origin.common.json.JSONUtil;
import org.origin.common.resource.Path;
import org.origin.common.resource.PathDTO;

public class RuntimeModelConverter {

	public static INDEX_SERVICE INDEX_SERVICE = new INDEX_SERVICE();
	public static EXTENSION_REGISTRY EXTENSION_REGISTRY = new EXTENSION_REGISTRY();
	public static CONFIG_REGISTRY CONFIG_REGISTRY = new CONFIG_REGISTRY();
	public static COMMON COMMON = new COMMON();

	public static class INDEX_SERVICE {
		/**
		 * Convert IndexItem runtime model to IndexItem DTO.
		 * 
		 * @param indexItem
		 * @return
		 */
		public IndexItemDTO toDTO(IndexItem indexItem) {
			if (indexItem == null) {
				return null;
			}

			IndexItemDTO indexItemDTO = new IndexItemDTO();
			indexItemDTO.setIndexItemId(indexItem.getIndexItemId());
			indexItemDTO.setIndexProviderId(indexItem.getIndexProviderId());
			indexItemDTO.setType(indexItem.getType());
			indexItemDTO.setName(indexItem.getName());
			indexItemDTO.setProperties(indexItem.getProperties());
			indexItemDTO.setCreateTime(indexItem.getCreateTime());
			indexItemDTO.setUpdateTime(indexItem.getLastUpdateTime());

			return indexItemDTO;
		}
	}

	public static class EXTENSION_REGISTRY {
		/**
		 * Convert ExtensionItem runtime model to ExtensionItem DTO.
		 * 
		 * @param item
		 * @return
		 */
		public ExtensionItemDTO toDTO(ExtensionItem item) {
			if (item == null) {
				return null;
			}

			ExtensionItemDTO extensionItemDTO = new ExtensionItemDTO();
			extensionItemDTO.setId(item.getId());
			extensionItemDTO.setPlatformId(item.getPlatformId());
			extensionItemDTO.setTypeId(item.getTypeId());
			extensionItemDTO.setExtensionId(item.getExtensionId());
			extensionItemDTO.setName(item.getName());
			extensionItemDTO.setDescription(item.getDescription());
			extensionItemDTO.setProperties(item.getProperties());

			return extensionItemDTO;
		}
	}

	public static class CONFIG_REGISTRY {
		/**
		 * 
		 * @param metadata
		 * @return
		 */
		public ConfigRegistryDTO toDTO(ConfigRegistryMetadata metadata) {
			if (metadata == null) {
				return null;
			}

			String id = metadata.getId();
			String type = metadata.getType();
			String name = metadata.getName();
			Map<String, Object> properties = metadata.getProperties();
			long dateCreated = metadata.getDateCreated();
			long dateModified = metadata.getDateModified();

			ConfigRegistryDTO configRegistryDTO = new ConfigRegistryDTO();
			configRegistryDTO.setId(id);
			configRegistryDTO.setType(type);
			configRegistryDTO.setName(name);
			configRegistryDTO.setProperties(properties);
			configRegistryDTO.setDateCreated(dateCreated);
			configRegistryDTO.setDateModified(dateModified);

			return configRegistryDTO;
		}

		/**
		 * 
		 * @param configElement
		 * @return
		 */
		public ConfigElementDTO toDTO(ConfigElement configElement) {
			if (configElement == null) {
				return null;
			}

			String configRegistryId = configElement.getConfigRegistryId();
			String parentElementId = configElement.getParentElementId();
			String elementId = configElement.getElementId();
			String pathString = configElement.getPath().getPathString();
			Map<String, Object> attributes = configElement.getAttributes();
			long dateCreated = configElement.getDateCreated();
			long dateModified = configElement.getDateModified();

			ConfigElementDTO configElementDTO = new ConfigElementDTO();
			configElementDTO.setConfigRegistryId(configRegistryId);
			configElementDTO.setParentElementId(parentElementId);
			configElementDTO.setElementId(elementId);
			configElementDTO.setPath(pathString);
			configElementDTO.setAttributes(attributes);
			configElementDTO.setDateCreated(dateCreated);
			configElementDTO.setDateModified(dateModified);

			return configElementDTO;
		}
	}

	public static class COMMON {
		/**
		 * 
		 * @param path
		 * @return
		 */
		public PathDTO toDTO(Path path) {
			if (path == null) {
				return null;
			}

			String pathString = path.getPathString();

			PathDTO pathDTO = new PathDTO();
			pathDTO.setPathString(pathString);

			return pathDTO;
		}

		/**
		 * 
		 * @param propertiesString
		 * @return
		 */
		public Map<String, Object> toProperties(String propertiesString) {
			Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);
			return properties;
		}

		/**
		 * 
		 * @param properties
		 * @return
		 */
		public String toPropertiesString(Map<String, Object> properties) {
			String propertiesString = JSONUtil.toJsonString(properties);
			return propertiesString;
		}

		/**
		 * 
		 * @param propertiesString
		 * @return
		 */
		public Map<String, Object> toMap(String mapString) {
			Map<String, Object> map = JSONUtil.toProperties(mapString, true);
			return map;
		}

		/**
		 * 
		 * @param map
		 * @return
		 */
		public String toMapString(Map<String, Object> map) {
			String mapString = JSONUtil.toJsonString(map);
			return mapString;
		}
	}

}

/// **
// * Convert ManagementException object to Error DTO.
// *
// * @param e
// * @return
// */
// public ErrorDTO toDTO(IndexServiceException e) {
// if (e == null) {
// return null;
// }
//
// ErrorDTO dto = new ErrorDTO();
// dto.setCode(e.getCode());
// dto.setMessage(e.getMessage());
//
// if (e.getCause() != null) {
// String causeName = e.getCause().getClass().getName();
// String causeMessage = e.getCause().getMessage();
// dto.setDetail(causeName + " " + causeMessage);
// } else {
// String causeName = e.getClass().getName();
// dto.setDetail(causeName);
// }
//
// return dto;
// }

/// **
// * Convert ServerException object to Error DTO.
// *
// * @param e
// * @return
// */
// public ErrorDTO toDTO(ServerException e) {
// if (e == null) {
// return null;
// }
//
// ErrorDTO dto = new ErrorDTO();
// dto.setCode(e.getCode());
// dto.setMessage(e.getMessage());
//
// if (e.getCause() != null) {
// String causeName = e.getCause().getClass().getName();
// String causeMessage = e.getCause().getMessage();
// dto.setDetail(causeName + " " + causeMessage);
// } else {
// String causeName = e.getClass().getName();
// dto.setDetail(causeName);
// }
//
// return dto;
// }

// ------------------------------------------------------------------------------------------
// RTO -> DTO
// ------------------------------------------------------------------------------------------
// /**
// * Convert ChannelException object to Error DTO.
// *
// * @param e
// * @return
// */
// public ErrorDTO toDTO(ChannelException e) {
// if (e == null) {
// return null;
// }
//
// ErrorDTO dto = new ErrorDTO();
//
// dto.setCode(e.getCode());
// dto.setMessage(e.getMessage());
//
// if (e.getCause() != null) {
// String causeName = e.getCause().getClass().getName();
// String causeMessage = e.getCause().getMessage();
// dto.setDetail(causeName + " " + causeMessage);
//
// } else {
// String causeName = e.getClass().getName();
// dto.setDetail(causeName);
// }
// return dto;
// }
