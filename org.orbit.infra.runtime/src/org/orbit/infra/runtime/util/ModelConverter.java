package org.orbit.infra.runtime.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.infra.model.datacast.DataTubeConfigDTO;
import org.orbit.infra.model.extensionregistry.ExtensionItemDTO;
import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.model.indexes.IndexItemDTO;
import org.orbit.infra.runtime.datacast.service.DataTubeConfig;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionItem;
import org.origin.common.json.JSONUtil;

public class ModelConverter {

	public static Indexes Indexes = new Indexes();
	public static Extensions Extensions = new Extensions();
	public static DataCast DataCast = new DataCast();
	public static DataTube DataTube = new DataTube();

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
