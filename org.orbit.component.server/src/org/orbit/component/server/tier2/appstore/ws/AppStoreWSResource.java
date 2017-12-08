package org.orbit.component.server.tier2.appstore.ws;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.server.tier2.appstore.service.AppStoreService;
import org.origin.common.rest.server.AbstractWSApplicationResource;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class AppStoreWSResource extends AbstractWSApplicationResource {

	@Inject
	public AppStoreService service;

	protected AppStoreService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("AppStoreService is not available.");
		}
		return this.service;
	}

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		// AppStoreService service = getService(AppStoreService.class);
		AppStoreService service = getService();
		if (service != null) {
			return Response.ok(1).build();
		}
		return Response.ok(0).build();
	}

}
