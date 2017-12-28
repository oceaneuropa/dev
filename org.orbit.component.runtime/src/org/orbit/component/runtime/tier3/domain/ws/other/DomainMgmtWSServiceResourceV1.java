package org.orbit.component.runtime.tier3.domain.ws.other;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.runtime.tier3.domain.editpolicy.other.MachineConfigEditPolicy;
import org.orbit.component.runtime.tier3.domain.editpolicy.other.NodeConfigEditPolicy;
import org.orbit.component.runtime.tier3.domain.editpolicy.other.TransferAgentConfigEditPolicy;
import org.orbit.component.runtime.tier3.domain.service.DomainService;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DomainMgmtWSServiceResourceV1 extends EditpolicyWSApplicationResource {

	@Inject
	public DomainService service;

	protected DomainService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("DomainService is not available.");
		}
		return this.service;
	}

	@Override
	public <T> T getService(Class<T> serviceClass) {
		if (DomainService.class.isAssignableFrom(serviceClass)) {
			return (T) getService();
		}
		return null;
	}

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		DomainService service = getService();
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