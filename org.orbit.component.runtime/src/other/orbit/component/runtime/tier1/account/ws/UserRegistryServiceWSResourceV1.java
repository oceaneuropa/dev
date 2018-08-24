package other.orbit.component.runtime.tier1.account.ws;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.origin.common.rest.server.AbstractWSApplicationResource;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class UserRegistryServiceWSResourceV1 extends AbstractWSApplicationResource {

	@Inject
	public UserRegistryService service;

	public UserRegistryService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("UserRegistryService is not available.");
		}
		return this.service;
	}

}

// UserRegistryService service = getService(UserRegistryService.class);

// @GET
// @Path("ping")
// @Produces(MediaType.APPLICATION_JSON)
// public Response ping() {
// UserRegistryService service = getService();
// if (service != null) {
// return Response.ok(1).build();
// }
// return Response.ok(0).build();
// }
