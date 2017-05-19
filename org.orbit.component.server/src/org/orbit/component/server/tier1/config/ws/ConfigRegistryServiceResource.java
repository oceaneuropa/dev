package org.orbit.component.server.tier1.config.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.server.tier1.config.service.ConfigRegistryService;
import org.origin.common.rest.server.AbstractApplicationResource;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigRegistryServiceResource extends AbstractApplicationResource {

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		ConfigRegistryService configRegistryService = getService(ConfigRegistryService.class);
		if (configRegistryService != null) {
			return Response.ok(1).build();
		}
		return Response.ok(0).build();
	}

}
