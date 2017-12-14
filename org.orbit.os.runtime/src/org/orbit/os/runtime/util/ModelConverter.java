package org.orbit.os.runtime.util;

import org.orbit.os.model.world.dto.WorldDTO;
import org.origin.world.api.World;

public class ModelConverter {

	private static ModelConverter converter = new ModelConverter();

	public static ModelConverter getInstance() {
		return converter;
	}

	public WorldDTO toDTO(World world) {
		WorldDTO dto = new WorldDTO();
		dto.setName(world.getName());
		return dto;
	}

}
