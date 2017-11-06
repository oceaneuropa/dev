package org.orbit.component.server.tier3.transferagent.ws;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.server.tier3.transferagent.editpolicy.NodeEditPolicy;
import org.orbit.component.server.tier3.transferagent.editpolicy.NodespaceEditPolicy;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.IEditingDomain;
import org.origin.common.rest.agent.CommonWSApplicationResource;

/**
 * @see HomeAgentResource
 * @see HomeWorkspacesResource
 * 
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class TransferAgentServiceResource extends CommonWSApplicationResource {

	@Inject
	public TransferAgentService service;

	protected TransferAgentService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("TransferAgentService is not available.");
		}
		return this.service;
	}

	@Override
	protected IEditingDomain getEditingDomain() {
		// return getService(TransferAgentService.class).getEditingDomain();
		TransferAgentService service = getService();
		return service.getEditingDomain();
	}

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		// TransferAgentService service = getService(TransferAgentService.class);
		TransferAgentService service = getService();
		if (service != null) {
			return Response.ok(1).build();
		}
		return Response.ok(0).build();
	}

	@Override
	protected void createEditPolicies() {
		NodespaceEditPolicy nodespaceEditPolicy = new NodespaceEditPolicy();
		NodeEditPolicy nodeEditPolicy = new NodeEditPolicy();

		installEditPolicy(nodespaceEditPolicy);
		installEditPolicy(nodeEditPolicy);
	}

}
