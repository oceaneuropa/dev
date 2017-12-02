package org.orbit.component.server.tier3.transferagent.ws;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier3.transferagent.TransferAgentException;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/**
 * Transfer agent web service resource.
 * 
 * {contextRoot} example: /orbit/v1/ta
 *
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/request (body parameter: Request)
 *
 * @see HomeAgentResource
 * @see HomeWorkspacesResource
 * 
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class TransferAgentServiceResource extends AbstractWSApplicationResource {

	@Inject
	public TransferAgentService service;

	public TransferAgentService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("TransferAgentService is not available.");
		}
		return this.service;
	}

	@PUT
	@Path("/request")
	@Produces(MediaType.APPLICATION_JSON)
	public Response request(Request request) {
		TransferAgentService service = getService();

		WSCommand command = service.getCommand(request);
		if (command != null) {
			try {
				return command.execute(request);

			} catch (Exception e) {
				String statusCode = String.valueOf(Status.INTERNAL_SERVER_ERROR.getStatusCode());
				if (e instanceof TransferAgentException) {
					statusCode = ((TransferAgentException) e).getCode();
				}
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
