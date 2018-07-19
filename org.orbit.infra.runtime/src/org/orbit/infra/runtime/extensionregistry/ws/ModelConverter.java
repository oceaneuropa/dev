package org.orbit.infra.runtime.extensionregistry.ws;

import org.orbit.infra.model.extensionregistry.ExtensionItemDTO;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionItem;

public class ModelConverter {

	public static ModelConverter INSTANCE = new ModelConverter();

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
