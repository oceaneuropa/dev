package org.orbit.spirit.runtime.gaia.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.spirit.runtime.gaia.service.GaiaService;
import org.orbit.spirit.runtime.gaia.service.WorldMetadata;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class WorldStatusWSCommand extends AbstractWSCommand {

	protected GaiaService gaia;

	public WorldStatusWSCommand(GaiaService gaia) {
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
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'name' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		WorldMetadata world = this.gaia.getWorld(name);
		if (world == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "World '" + name + "' does not exist.");
			return Response.status(Status.NOT_FOUND).entity(error).build();
		}

		// Map<String, String> status = world.getStatus();

		return Response.status(Status.OK).build();
	}

}
