package org.orbit.infra.model.channel;

import org.origin.common.rest.model.ErrorDTO;

public class ChannelConverter {

	private static ChannelConverter converter = new ChannelConverter();

	public static ChannelConverter getInstance() {
		return converter;
	}

	// ------------------------------------------------------------------------------------------
	// RTO -> DTO
	// ------------------------------------------------------------------------------------------
	/**
	 * Convert ChannelException object to Error DTO.
	 * 
	 * @param e
	 * @return
	 */
	public ErrorDTO toDTO(ChannelException e) {
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

}
