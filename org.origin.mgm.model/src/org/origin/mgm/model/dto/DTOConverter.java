package org.origin.mgm.model.dto;

import org.origin.common.rest.model.ErrorDTO;
import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.runtime.IndexItem;

public class DTOConverter {

	private static DTOConverter converter = new DTOConverter();

	public static DTOConverter getInstance() {
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
			dto.setException(causeName + " " + causeMessage);
		} else {
			String causeName = e.getClass().getName();
			dto.setException(causeName);
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

		dto.setNamespace(indexItem.getNamespace());
		dto.setName(indexItem.getName());
		dto.setProperties(indexItem.getProperties());

		return dto;
	}

}
