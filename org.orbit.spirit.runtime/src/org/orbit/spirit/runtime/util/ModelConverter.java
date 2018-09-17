package org.orbit.spirit.runtime.util;

import org.orbit.spirit.model.gaia.dto.WorldDTO;
import org.orbit.spirit.runtime.earth.service.World;
import org.orbit.spirit.runtime.gaia.service.WorldMetadata;

public class ModelConverter {

	public static GAIA GAIA = new GAIA();
	public static Earth Earth = new Earth();

	public static class GAIA {
		public WorldDTO toDTO(WorldMetadata world) {
			WorldDTO dto = new WorldDTO();
			dto.setName(world.getName());
			return dto;
		}
	}

	public static class Earth {
		public WorldDTO toDTO(World world) {
			WorldDTO dto = new WorldDTO();
			dto.setName(world.getName());
			return dto;
		}
	}

}
