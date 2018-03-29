package org.orbit.platform.runtime.core.ws;

import org.orbit.platform.model.dto.ExtensionDTO;
import org.orbit.platform.model.dto.ProcessDTO;
import org.orbit.platform.sdk.IProcess;
import org.origin.common.extensions.core.IExtension;

public class ModelConverter {

	private static ModelConverter converter = new ModelConverter();

	public static ModelConverter getInstance() {
		return converter;
	}

	// ------------------------------------------------------------------------------------------
	// RTO to DTO
	// ------------------------------------------------------------------------------------------
	/**
	 * Convert IProgramExtension to ExtensionDTO.
	 * 
	 * @param extension
	 * @return
	 */
	public ExtensionDTO toDTO(IExtension extension) {
		if (extension == null) {
			return null;
		}
		ExtensionDTO extensionDTO = new ExtensionDTO();

		String extensionTypeId = extension.getTypeId();
		String extensionId = extension.getId();
		String name = extension.getName();
		String description = extension.getDescription();

		extensionDTO.setTypeId(extensionTypeId);
		extensionDTO.setId(extensionId);
		extensionDTO.setName(name);
		extensionDTO.setDescription(description);

		return extensionDTO;
	}

	/**
	 * Convert IProcess to ProcessDTO.
	 * 
	 * @param process
	 * @return
	 */
	public ProcessDTO toDTO(IProcess process) {
		if (process == null) {
			return null;
		}
		ProcessDTO processDTO = new ProcessDTO();

		int pid = process.getPID();
		String name = process.getName();

		processDTO.setPID(pid);
		processDTO.setName(name);

		return processDTO;
	}

}
