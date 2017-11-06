package org.orbit.component.server.tier1.account.ws;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.server.tier1.account.service.UserRegistryService;
import org.origin.common.rest.server.AbstractWSApplicationResource;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class UserRegistryServiceResource extends AbstractWSApplicationResource {

	@Inject
	public UserRegistryService service;

	protected UserRegistryService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("UserRegistryService is not available.");
		}
		return this.service;
	}

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		// UserRegistryService service = getService(UserRegistryService.class);
		UserRegistryService service = getService();
		if (service != null) {
			return Response.ok(1).build();
		}
		return Response.ok(0).build();
	}

}
