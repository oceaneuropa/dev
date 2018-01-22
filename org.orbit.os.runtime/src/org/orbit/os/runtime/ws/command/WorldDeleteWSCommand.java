package org.orbit.os.runtime.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.os.runtime.service.GAIA;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class WorldDeleteWSCommand extends AbstractWSCommand {

	protected GAIA gaia;

	public WorldDeleteWSCommand(GAIA gaia) {
		this.gaia = gaia;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String name = (request.getParameter("name") instanceof String) ? (String) request.getParameter("name") : null;
		if (name == null || name.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'name' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean succeed = this.gaia.getWorlds().delete(name);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
