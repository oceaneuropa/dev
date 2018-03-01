package org.orbit.platform.runtime.command.ws;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.platform.runtime.command.service.CommandService;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.server.AbstractWSApplicationResource;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class CommandWSResource extends AbstractWSApplicationResource {

	@Inject
	public CommandService service;

	public CommandService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("CommandService is not available.");
		}
		return this.service;
	}

	@POST
	@Path("request")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response request(Request request) {
		CommandService service = getService();

		WSCommand command = service.getEditPolicies().getCommand(request);
		if (command != null) {
			try {
				return command.execute(request);

			} catch (Exception e) {
				String statusCode = String.valueOf(Status.INTERNAL_SERVER_ERROR.getStatusCode());
				ErrorDTO error = handleError(e, statusCode, true);
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
			}
		} else {
			// no command is found for the request
			// - return error response
			ErrorDTO error = new ErrorDTO("Request '" + request.getRequestName() + "' is not supported.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
	}

}
