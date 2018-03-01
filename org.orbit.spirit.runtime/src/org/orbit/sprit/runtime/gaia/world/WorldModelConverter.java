package org.orbit.sprit.runtime.gaia.world;

import org.orbit.spirit.model.gaia.dto.WorldDTO;

public class WorldModelConverter {

	private static WorldModelConverter converter = new WorldModelConverter();

	public static WorldModelConverter getInstance() {
		return converter;
	}

	public WorldDTO toDTO(World world) {
		WorldDTO dto = new WorldDTO();
		dto.setName(world.getName());
		return dto;
	}

}
