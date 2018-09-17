package org.orbit.spirit.runtime.gaia.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.spirit.model.gaia.dto.WorldDTO;
import org.orbit.spirit.runtime.gaia.service.GaiaService;
import org.orbit.spirit.runtime.gaia.service.WorldMetadata;
import org.orbit.spirit.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class WorldCreateWSCommand extends AbstractWSCommand {

	protected GaiaService gaia;

	public WorldCreateWSCommand(GaiaService gaia) {
		this.gaia = gaia;
	}

	@Override
	public boolean isSupported(Request request) {
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String name = (request.getParameter("name") instanceof String) ? (String) request.getParameter("name") : null;
		if (name == null || name.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'name' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		if (this.gaia.worldExists(name)) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "World '" + name + "' already exists.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		WorldMetadata newWorld = this.gaia.createWorld(name);
		if (newWorld == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "World '" + name + "' cannot be created.");
			return Response.status(Status.NOT_FOUND).entity(error).build();
		}

		WorldDTO worldDTO = ModelConverter.GAIA.toDTO(newWorld);
		return Response.status(Status.OK).entity(worldDTO).build();
	}

}
