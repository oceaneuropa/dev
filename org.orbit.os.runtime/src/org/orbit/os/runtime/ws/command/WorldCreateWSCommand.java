package org.orbit.os.runtime.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.os.model.world.dto.WorldDTO;
import org.orbit.os.model.world.rto.World;
import org.orbit.os.runtime.gaia.GAIA;
import org.orbit.os.runtime.util.WorldModelConverter;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class WorldCreateWSCommand extends AbstractWSCommand {

	protected GAIA gaia;

	public WorldCreateWSCommand(GAIA gaia) {
		this.gaia = gaia;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String name = (request.getParameter("name") instanceof String) ? (String) request.getParameter("name") : null;
		if (name == null || name.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'name' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		if (this.gaia.getWorlds().exists(name)) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "World '" + name + "' already exists.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		World newWorld = this.gaia.getWorlds().create(name);
		if (newWorld == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "World '" + name + "' cannot be created.");
			return Response.status(Status.NOT_FOUND).entity(error).build();
		}

		WorldDTO worldDTO = WorldModelConverter.getInstance().toDTO(newWorld);
		return Response.status(Status.OK).entity(worldDTO).build();
	}

}
