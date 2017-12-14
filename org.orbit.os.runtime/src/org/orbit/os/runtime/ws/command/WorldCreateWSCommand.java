package org.orbit.os.runtime.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.os.runtime.service.GAIA;
import org.orbit.os.runtime.world.WorldException;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class WorldCreateWSCommand extends AbstractWSCommand {

	protected GAIA gaia;

	public WorldCreateWSCommand(GAIA gaia) {
		this.gaia = gaia;
	}

	@Override
	public Response execute(Request request) {
		String name = (request.getParameter("name") instanceof String) ? (String) request.getParameter("name") : null;
		if (name == null || name.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'name' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		try {
			if (this.gaia.getWorlds().exists(name)) {
				ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "World '" + name + "' already exists.", null);
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

		} catch (WorldException e) {
			e.printStackTrace();
		}

		return null;
	}

}
