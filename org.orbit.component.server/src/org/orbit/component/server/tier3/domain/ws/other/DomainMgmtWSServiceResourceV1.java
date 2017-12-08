package org.orbit.component.server.tier3.domain.ws.other;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.server.tier3.domain.editpolicy.MachineConfigEditPolicy;
import org.orbit.component.server.tier3.domain.editpolicy.NodeConfigEditPolicy;
import org.orbit.component.server.tier3.domain.editpolicy.TransferAgentConfigEditPolicy;
import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DomainMgmtWSServiceResourceV1 extends EditpolicyWSApplicationResource {

	@Inject
	public DomainManagementService service;

	protected DomainManagementService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("DomainManagementService is not available.");
		}
		return this.service;
	}

	@Override
	public <T> T getService(Class<T> serviceClass) {
		if (DomainManagementService.class.isAssignableFrom(serviceClass)) {
			return (T) getService();
		}
		return null;
	}

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		// DomainManagementService service = getService(DomainManagementService.class);
		DomainManagementService service = getService();
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

		installEditPolicy(machineConfigEditPolicy);
		installEditPolicy(taConfigEditPolicy);
		installEditPolicy(nodeConfigEditPolicy);
	}

}
