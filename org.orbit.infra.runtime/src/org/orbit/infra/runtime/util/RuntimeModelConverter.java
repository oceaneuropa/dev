package org.orbit.infra.runtime.util;

import java.util.Map;

import org.orbit.infra.model.configregistry.ConfigElementDTO;
import org.orbit.infra.model.configregistry.ConfigRegistryDTO;
import org.orbit.infra.model.extensionregistry.ExtensionItemDTO;
import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.model.indexes.IndexItemDTO;
import org.orbit.infra.model.indexes.IndexProviderItem;
import org.orbit.infra.model.indexes.IndexProviderItemDTO;
import org.orbit.infra.model.subs.SubsMapping;
import org.orbit.infra.model.subs.SubsSource;
import org.orbit.infra.model.subs.SubsSourceType;
import org.orbit.infra.model.subs.SubsTarget;
import org.orbit.infra.model.subs.SubsTargetType;
import org.orbit.infra.model.subs.dto.SubsMappingDTO;
import org.orbit.infra.model.subs.dto.SubsSourceDTO;
import org.orbit.infra.model.subs.dto.SubsSourceTypeDTO;
import org.orbit.infra.model.subs.dto.SubsTargetDTO;
import org.orbit.infra.model.subs.dto.SubsTargetTypeDTO;
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
	public static SUBS_SERVER SUBS_SERVER = new SUBS_SERVER();
	public static COMMON COMMON = new COMMON();

	public static class INDEX_SERVICE {
		/**
		 * 
		 * @param indexProviderItem
		 * @return
		 */
		public IndexProviderItemDTO toDTO(IndexProviderItem indexProviderItem) {
			if (indexProviderItem == null) {
				return null;
			}

			IndexProviderItemDTO dto = new IndexProviderItemDTO();
			dto.setId(indexProviderItem.getId());
			dto.setName(indexProviderItem.getName());
			dto.setDescription(indexProviderItem.getDescription());
			dto.setDateCreated(indexProviderItem.getDateCreated());
			dto.setDateModified(indexProviderItem.getDateModified());

			return dto;
		}

		/**
		 * 
		 * @param indexItem
		 * @return
		 */
		public IndexItemDTO toDTO(IndexItem indexItem) {
			if (indexItem == null) {
				return null;
			}

			IndexItemDTO dto = new IndexItemDTO();
			dto.setIndexItemId(indexItem.getIndexItemId());
			dto.setIndexProviderId(indexItem.getIndexProviderId());
			dto.setType(indexItem.getType());
			dto.setName(indexItem.getName());
			dto.setProperties(indexItem.getProperties());
			dto.setCreateTime(indexItem.getDateCreated());
			dto.setUpdateTime(indexItem.getDateModified());

			return dto;
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

	public static class SUBS_SERVER {
		/**
		 * 
		 * @param typeObj
		 * @return
		 */
		public SubsSourceTypeDTO toDTO(SubsSourceType typeObj) {
			if (typeObj == null) {
				return null;
			}

			Integer id = typeObj.getId();
			String type = typeObj.getType();
			String name = typeObj.getName();
			long dateCreated = typeObj.getDateCreated();
			long dateModified = typeObj.getDateModified();

			SubsSourceTypeDTO typeDTO = new SubsSourceTypeDTO();
			typeDTO.setId(id);
			typeDTO.setType(type);
			typeDTO.setName(name);
			typeDTO.setDateCreated(dateCreated);
			typeDTO.setDateModified(dateModified);

			return typeDTO;
		}

		/**
		 * 
		 * @param typeObj
		 * @return
		 */
		public SubsTargetTypeDTO toDTO(SubsTargetType typeObj) {
			if (typeObj == null) {
				return null;
			}

			Integer id = typeObj.getId();
			String type = typeObj.getType();
			String name = typeObj.getName();
			long dateCreated = typeObj.getDateCreated();
			long dateModified = typeObj.getDateModified();

			SubsTargetTypeDTO typeDTO = new SubsTargetTypeDTO();
			typeDTO.setId(id);
			typeDTO.setType(type);
			typeDTO.setName(name);
			typeDTO.setDateCreated(dateCreated);
			typeDTO.setDateModified(dateModified);

			return typeDTO;
		}

		/**
		 * 
		 * @param source
		 * @return
		 */
		public SubsSourceDTO toDTO(SubsSource source) {
			if (source == null) {
				return null;
			}

			Integer id = source.getId();
			String type = source.getType();
			String typeId = source.getInstanceId();
			String name = source.getName();
			Map<String, Object> properties = source.getProperties();
			long dateCreated = source.getDateCreated();
			long dateModified = source.getDateModified();

			SubsSourceDTO sourceDTO = new SubsSourceDTO();
			sourceDTO.setId(id);
			sourceDTO.setType(type);
			sourceDTO.setInstanceId(typeId);
			sourceDTO.setName(name);
			sourceDTO.setProperties(properties);
			sourceDTO.setDateCreated(dateCreated);
			sourceDTO.setDateModified(dateModified);

			return sourceDTO;
		}

		/**
		 * 
		 * @param target
		 * @return
		 */
		public SubsTargetDTO toDTO(SubsTarget target) {
			if (target == null) {
				return null;
			}

			Integer id = target.getId();
			String type = target.getType();
			String typeId = target.getInstanceId();
			String name = target.getName();
			String serverId = target.getServerId();
			String serverURL = target.getServerURL();
			long serverHeartbeatTime = target.getServerHeartbeatTime();
			Map<String, Object> properties = target.getProperties();
			long dateCreated = target.getDateCreated();
			long dateModified = target.getDateModified();

			SubsTargetDTO targetDTO = new SubsTargetDTO();
			targetDTO.setId(id);
			targetDTO.setType(type);
			targetDTO.setInstanceId(typeId);
			targetDTO.setName(name);
			targetDTO.setServerId(serverId);
			targetDTO.setServerURL(serverURL);
			targetDTO.setServerHeartbeatTime(serverHeartbeatTime);
			targetDTO.setProperties(properties);
			targetDTO.setDateCreated(dateCreated);
			targetDTO.setDateModified(dateModified);

			return targetDTO;
		}

		/**
		 * 
		 * @param mapping
		 * @return
		 */
		public SubsMappingDTO toDTO(SubsMapping mapping) {
			if (mapping == null) {
				return null;
			}

			Integer id = mapping.getId();
			Integer sourceId = mapping.getSourceId();
			Integer targetId = mapping.getTargetId();
			String clientId = mapping.getClientId();
			String clientURL = mapping.getClientURL();
			long clientHeartbeatTime = mapping.getClientHeartbeatTime();
			Map<String, Object> properties = mapping.getProperties();
			long dateCreated = mapping.getDateCreated();
			long dateModified = mapping.getDateModified();

			SubsMappingDTO mappingDTO = new SubsMappingDTO();
			mappingDTO.setId(id);
			mappingDTO.setSourceId(sourceId);
			mappingDTO.setTargetId(targetId);
			mappingDTO.setClientId(clientId);
			mappingDTO.setClientURL(clientURL);
			mappingDTO.setClientHeartbeatTime(clientHeartbeatTime);
			mappingDTO.setProperties(properties);
			mappingDTO.setDateCreated(dateCreated);
			mappingDTO.setDateModified(dateModified);

			return mappingDTO;
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
		 * @param map
		 * @return
		 */
		public String toMapString(Map<String, Object> map) {
			String mapString = JSONUtil.toJsonString(map);
			return mapString;
		}
	}

}

/*-
public Map<String, Object> toProperties(String propertiesString) {
	Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);
	return properties;
}

public String toPropertiesString(Map<String, Object> properties) {
	String propertiesString = JSONUtil.toJsonString(properties);
	return propertiesString;
}

public Map<String, Object> toMap(String mapString) {
	Map<String, Object> map = JSONUtil.toProperties(mapString, true);
	return map;
}
*/

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
