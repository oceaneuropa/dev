package org.orbit.os.runtime.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.os.model.gaia.dto.WorldDTO;
import org.orbit.os.runtime.gaia.GAIA;
import org.orbit.os.runtime.util.WorldModelConverter;
import org.orbit.os.runtime.world.World;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.Request;

public class WorldListWSCommand extends AbstractWSCommand {

	protected GAIA gaia;

	public WorldListWSCommand(GAIA gaia) {
		this.gaia = gaia;
	}

	@Override
	public Response execute(Request request) throws Exception {
		List<WorldDTO> worldDTOs = new ArrayList<WorldDTO>();
		List<World> worlds = this.gaia.getWorlds().getWorlds();
		for (World world : worlds) {
			WorldDTO worldDTO = WorldModelConverter.getInstance().toDTO(world);
			worldDTOs.add(worldDTO);
		}
		return Response.status(Status.OK).entity(worldDTOs).build();
	}

}
