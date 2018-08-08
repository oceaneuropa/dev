package org.orbit.spirit.runtime.gaia.util;

import org.orbit.spirit.model.gaia.dto.WorldDTO;
import org.orbit.spirit.runtime.gaia.world.World;

public class ModelConverter {

	public static GAIA GAIA = new GAIA();

	public static class GAIA {
		public WorldDTO toDTO(World world) {
			WorldDTO dto = new WorldDTO();
			dto.setName(world.getName());
			return dto;
		}
	}

}
