package org.orbit.os.runtime.util;

import org.orbit.os.model.world.dto.WorldDTO;
import org.orbit.os.model.world.rto.World;

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
