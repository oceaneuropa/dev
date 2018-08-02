package org.orbit.infra.runtime.util;

import org.orbit.infra.model.extensionregistry.ExtensionItemDTO;
import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.model.indexes.IndexItemDTO;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionItem;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.server.ServerException;

public class ModelConverter {

	public static Indexes Indexes = new Indexes();
	public static Extensions Extensions = new Extensions();

	public static class Indexes {
		/**
		 * Convert ServerException object to Error DTO.
		 * 
		 * @param e
		 * @return
		 */
		public ErrorDTO toDTO(ServerException e) {
			if (e == null) {
				return null;
			}

			ErrorDTO dto = new ErrorDTO();
			dto.setCode(e.getCode());
			dto.setMessage(e.getMessage());

			if (e.getCause() != null) {
				String causeName = e.getCause().getClass().getName();
				String causeMessage = e.getCause().getMessage();
				dto.setDetail(causeName + " " + causeMessage);
			} else {
				String causeName = e.getClass().getName();
				dto.setDetail(causeName);
			}

			return dto;
		}

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

			IndexItemDTO dto = new IndexItemDTO();
			dto.setIndexItemId(indexItem.getIndexItemId());
			dto.setIndexProviderId(indexItem.getIndexProviderId());
			dto.setType(indexItem.getType());
			dto.setName(indexItem.getName());
			dto.setProperties(indexItem.getProperties());

			return dto;
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

			ExtensionItemDTO DTO = new ExtensionItemDTO();
			DTO.setId(item.getId());
			DTO.setPlatformId(item.getPlatformId());
			DTO.setTypeId(item.getTypeId());
			DTO.setExtensionId(item.getExtensionId());
			DTO.setName(item.getName());
			DTO.setDescription(item.getDescription());
			DTO.setProperties(item.getProperties());
			return DTO;
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
