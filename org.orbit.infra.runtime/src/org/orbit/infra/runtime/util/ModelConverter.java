package org.orbit.infra.runtime.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.infra.model.configregistry.ConfigElementDTO;
import org.orbit.infra.model.configregistry.ConfigRegistryMetadataDTO;
import org.orbit.infra.model.datacast.ChannelMetadataDTO;
import org.orbit.infra.model.datacast.DataTubeConfigDTO;
import org.orbit.infra.model.extensionregistry.ExtensionItemDTO;
import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.model.indexes.IndexItemDTO;
import org.orbit.infra.runtime.configregistry.service.ConfigElement;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryMetadata;
import org.orbit.infra.runtime.datacast.service.ChannelMetadata;
import org.orbit.infra.runtime.datacast.service.DataTubeConfig;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionItem;
import org.origin.common.json.JSONUtil;

public class ModelConverter {

	public static Indexes Indexes = new Indexes();
	public static Extensions Extensions = new Extensions();
	public static ConfigRegistry CONFIG_REGISTRY = new ConfigRegistry();
	public static DataCast DATA_CAST = new DataCast();
	public static DataTube DATA_TUBE = new DataTube();

	public static class Indexes {
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

			return indexItemDTO;
		}
	}

	public static class Extensions {
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

	public static class ConfigRegistry {
		/**
		 * 
		 * @param metadata
		 * @return
		 */
		public ConfigRegistryMetadataDTO toDTO(ConfigRegistryMetadata metadata) {
			if (metadata == null) {
				return null;
			}

			String id = metadata.getId();
			String type = metadata.getType();
			String name = metadata.getName();
			Map<String, Object> properties = metadata.getProperties();
			long dateCreated = metadata.getDateCreated();
			long dateModified = metadata.getDateModified();

			ConfigRegistryMetadataDTO metadataDTO = new ConfigRegistryMetadataDTO();
			metadataDTO.setId(id);
			metadataDTO.setType(type);
			metadataDTO.setName(name);
			metadataDTO.setProperties(properties);
			metadataDTO.setDateCreated(dateCreated);
			metadataDTO.setDateModified(dateModified);

			return metadataDTO;
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

	public static class DataCast {
		/**
		 * 
		 * @param dataTubeConfig
		 * @return
		 */
		public DataTubeConfigDTO toDTO(DataTubeConfig dataTubeConfig) {
			if (dataTubeConfig == null) {
				return null;
			}

			String id = dataTubeConfig.getId();
			String dataCastId = dataTubeConfig.getDataCastId();
			String dataTubeId = dataTubeConfig.getDataTubeId();
			String name = dataTubeConfig.getName();
			boolean isEnabled = dataTubeConfig.isEnabled();
			Map<String, Object> properties = dataTubeConfig.getProperties();
			long dateCreated = dataTubeConfig.getDateCreated();
			long dateModified = dataTubeConfig.getDateModified();

			DataTubeConfigDTO dataTubeConfigDTO = new DataTubeConfigDTO();
			dataTubeConfigDTO.setId(id);
			dataTubeConfigDTO.setDataCastId(dataCastId);
			dataTubeConfigDTO.setDataTubeId(dataTubeId);
			dataTubeConfigDTO.setName(name);
			dataTubeConfigDTO.setEnabled(isEnabled);
			dataTubeConfigDTO.setProperties(properties);
			dataTubeConfigDTO.setDateCreated(dateCreated);
			dataTubeConfigDTO.setDateModified(dateModified);

			return dataTubeConfigDTO;
		}

		/**
		 * 
		 * @param channelMetadata
		 * @return
		 */
		public ChannelMetadataDTO toDTO(ChannelMetadata channelMetadata) {
			if (channelMetadata == null) {
				return null;
			}

			String dataCastId = channelMetadata.getDataCastId();
			String dataTubeId = channelMetadata.getDataTubeId();
			String channelId = channelMetadata.getChannelId();
			String name = channelMetadata.getName();
			String accessType = channelMetadata.getAccessType();
			String accessCode = channelMetadata.getAccessCode();
			String ownerAccountId = channelMetadata.getOwnerAccountId();
			List<String> accountIds = channelMetadata.getAccountIds();
			Map<String, Object> properties = channelMetadata.getProperties();
			long dateCreated = channelMetadata.getDateCreated();
			long dateModified = channelMetadata.getDateModified();

			ChannelMetadataDTO channelMetadataDTO = new ChannelMetadataDTO();
			channelMetadataDTO.setDataCastId(dataCastId);
			channelMetadataDTO.setDataTubeId(dataTubeId);
			channelMetadataDTO.setChannelId(channelId);
			channelMetadataDTO.setName(name);
			channelMetadataDTO.setAccessType(accessType);
			channelMetadataDTO.setAccessCode(accessCode);
			channelMetadataDTO.setOwnerAccountId(ownerAccountId);
			channelMetadataDTO.setAccountIds(accountIds);
			channelMetadataDTO.setProperties(properties);
			channelMetadataDTO.setDateCreated(dateCreated);
			channelMetadataDTO.setDateModified(dateModified);

			return channelMetadataDTO;
		}

		/**
		 * 
		 * @param accountIdsString
		 * @return
		 */
		public List<String> toAccountIds(String accountIdsString) {
			List<String> accountIds = new ArrayList<String>();
			if (accountIdsString != null && !accountIdsString.isEmpty()) {
				List<Object> list = JSONUtil.toList(accountIdsString, false);
				if (list != null) {
					for (Object object : list) {
						accountIds.add(object.toString());
					}
				}
			}
			return accountIds;
		}

		/**
		 * Convert a list of String to string
		 * 
		 * @param accountIds
		 * @return
		 */
		public String toAccountIdsString(List<String> accountIds) {
			String accountIdsString = JSONUtil.toJsonString(accountIds, false);
			if (accountIdsString == null) {
				accountIdsString = "";
			}
			return accountIdsString;
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
	}

	public static class DataTube {

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
