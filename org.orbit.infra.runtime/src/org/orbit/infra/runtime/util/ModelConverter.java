package org.orbit.infra.runtime.util;

import org.orbit.infra.model.extensionregistry.ExtensionItemDTO;
import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.model.indexes.IndexItemDTO;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionItem;

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
