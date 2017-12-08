package org.nb.drive.rest.dto;

import java.io.File;

import org.nb.drive.api.DriveException;
import org.origin.common.rest.model.ErrorDTO;

public class DTOConverter {

	private static DTOConverter converter = new DTOConverter();

	public static DTOConverter getInstance() {
		return converter;
	}

	/**
	 * Convert DriveException object to Error DTO.
	 * 
	 * @param e
	 * @return
	 */
	public ErrorDTO toDTO(DriveException e) {
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
	 * Convert java.io.File to File DTO.
	 * 
	 * @param machine
	 * @return
	 */
	public FileDTO toDTO(File file) {
		if (file == null) {
			return null;
		}
		FileDTO dto = new FileDTO();

		// dto.setId(machine.getId());
		// dto.setName(machine.getName());
		// dto.setDescription(machine.getDescription());
		// dto.setIpAddress(machine.getIpAddress());

		return dto;
	}

}
