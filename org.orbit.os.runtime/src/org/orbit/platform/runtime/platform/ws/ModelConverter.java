package org.orbit.platform.runtime.platform.ws;

import org.orbit.platform.model.platform.dto.ServiceExtensionInfoDTO;
import org.orbit.platform.sdk.extension.IProgramExtension;

public class ModelConverter {

	private static ModelConverter converter = new ModelConverter();

	public static ModelConverter getInstance() {
		return converter;
	}

	// ------------------------------------------------------------------------------------------
	// RTO to DTO
	// ------------------------------------------------------------------------------------------
	/**
	 * Convert IProgramExtension to ServiceExtensionInfoDTO.
	 * 
	 * @param extensionDesc
	 * @return
	 */
	public ServiceExtensionInfoDTO toDTO(IProgramExtension extensionDesc) {
		if (extensionDesc == null) {
			return null;
		}
		ServiceExtensionInfoDTO dto = new ServiceExtensionInfoDTO();

		String extensionTypeId = extensionDesc.getTypeId();
		String extensionId = extensionDesc.getId();
		String name = extensionDesc.getName();
		String description = extensionDesc.getDescription();

		dto.setExtensionTypeId(extensionTypeId);
		dto.setExtensionId(extensionId);
		dto.setName(name);
		dto.setDescription(description);

		return dto;
	}

}
