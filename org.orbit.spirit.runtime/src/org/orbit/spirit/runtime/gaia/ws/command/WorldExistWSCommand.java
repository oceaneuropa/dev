package org.orbit.spirit.runtime.gaia.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.spirit.runtime.gaia.service.GaiaService;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class WorldExistWSCommand extends AbstractWSCommand {

	protected GaiaService gaia;

	public WorldExistWSCommand(GaiaService gaia) {
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

		boolean exists = this.gaia.worldExists(name);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("exists", exists);
		return Response.status(Status.OK).entity(result).build();
	}

}
