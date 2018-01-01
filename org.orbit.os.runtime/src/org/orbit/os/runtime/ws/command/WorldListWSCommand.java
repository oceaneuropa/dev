package org.orbit.os.runtime.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.os.model.world.dto.WorldDTO;
import org.orbit.os.runtime.service.GAIA;
import org.orbit.os.runtime.util.ModelConverter;
import org.orbit.os.runtime.world.WorldException;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.Request;
import org.spirit.world.api.World;

public class WorldListWSCommand extends AbstractWSCommand {

	protected GAIA gaia;

	public WorldListWSCommand(GAIA gaia) {
		this.gaia = gaia;
	}

	@Override
	public Response execute(Request request) {
		List<WorldDTO> worldDTOs = new ArrayList<WorldDTO>();

		try {
			List<World> worlds = this.gaia.getWorlds().getWorld();
			for (World world : worlds) {
				WorldDTO worldDTO = ModelConverter.getInstance().toDTO(world);
				worldDTOs.add(worldDTO);
			}
		} catch (WorldException e) {
			e.printStackTrace();
		}

		return Response.status(Status.OK).entity(worldDTOs).build();
	}

}
