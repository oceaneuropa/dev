package org.orbit.component.server.tier3.domain.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.server.tier3.domain.editpolicy.MachineConfigEditPolicy;
import org.orbit.component.server.tier3.domain.editpolicy.NodeConfigEditPolicy;
import org.orbit.component.server.tier3.domain.editpolicy.TransferAgentConfigEditPolicy;
import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.origin.common.rest.agent.CommonWSApplicationResource;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DomainMgmtServiceResource extends CommonWSApplicationResource {

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		DomainManagementService service = getService(DomainManagementService.class);
		if (service != null) {
			return Response.ok(1).build();
		}
		return Response.ok(0).build();
	}

	@Override
	protected void createEditPolicies() {
		MachineConfigEditPolicy machineConfigEditPolicy = new MachineConfigEditPolicy();
		TransferAgentConfigEditPolicy taConfigEditPolicy = new TransferAgentConfigEditPolicy();
		NodeConfigEditPolicy nodeConfigEditPolicy = new NodeConfigEditPolicy();

		installEditPolicy(machineConfigEditPolicy.getRole(), machineConfigEditPolicy);
		installEditPolicy(taConfigEditPolicy.getRole(), taConfigEditPolicy);
		installEditPolicy(nodeConfigEditPolicy.getRole(), nodeConfigEditPolicy);
	}

}
