package org.orbit.component.server.tier1.account.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.server.tier1.account.service.UserRegistryService;
import org.origin.common.rest.server.AbstractApplicationResource;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class UserRegistryServiceResource extends AbstractApplicationResource {

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		UserRegistryService userRegistryService = getService(UserRegistryService.class);
		if (userRegistryService != null) {
			return Response.ok(1).build();
		}
		return Response.ok(0).build();
	}

}
