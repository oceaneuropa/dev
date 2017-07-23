package org.orbit.component.server.tier2.appstore.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.server.tier2.appstore.service.AppStoreService;
import org.origin.common.rest.server.AbstractWSApplicationResource;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class AppStoreServiceResource extends AbstractWSApplicationResource {

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		AppStoreService appStoreService = getService(AppStoreService.class);
		if (appStoreService != null) {
			return Response.ok(1).build();
		}
		return Response.ok(0).build();
	}

}
