package org.origin.common.rest.model;

public class ModelConverter {

	/**
	 * Convert Exception to Error DTO.
	 * 
	 * @param e
	 * @param errorCode
	 * @return
	 */
	public static ErrorDTO toDTO(Exception e, String errorCode) {
		if (e == null) {
			return null;
		}

		ErrorDTO dto = new ErrorDTO();

		dto.setCode(errorCode);
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

}
