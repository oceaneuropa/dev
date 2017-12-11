package org.orbit.infra.model.indexes;

import org.origin.common.rest.model.ErrorDTO;

public class ModelConverter {

	private static ModelConverter converter = new ModelConverter();

	public static ModelConverter getInstance() {
		return converter;
	}

	/**
	 * Convert ManagementException object to Error DTO.
	 * 
	 * @param e
	 * @return
	 */
	public ErrorDTO toDTO(IndexServiceException e) {
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
