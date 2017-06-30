package org.orbit.component.server.tier3.transferagent.ws;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.IEditingDomain;
import org.origin.common.rest.agent.AbstractAgentResource;
import org.origin.common.rest.model.Request;

/**
 * @see HomeAgentResource
 * @see HomeWorkspacesResource
 * 
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class TransferAgentServiceResource extends AbstractAgentResource {

	@Override
	protected IEditingDomain getEditingDomain() {
		return getService(TransferAgentService.class).getEditingDomain();
	}

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		TransferAgentService service = getService(TransferAgentService.class);
		if (service != null) {
			return Response.ok(1).build();
		}
		return Response.ok(0).build();
	}

	/**
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/request (body parameter: Request)
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Path("/request")
	@Produces(MediaType.APPLICATION_JSON)
	public Response onRequest(Request request) {
		return super.onRequest(request);
	}

}
