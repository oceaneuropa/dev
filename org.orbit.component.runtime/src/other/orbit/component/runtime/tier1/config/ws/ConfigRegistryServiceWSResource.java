package other.orbit.component.runtime.tier1.config.ws;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceV0;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.server.AbstractWSApplicationResource;

@Secured(roles = { OrbitRoles.USER })
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigRegistryServiceWSResource extends AbstractWSApplicationResource {

	@Inject
	public ConfigRegistryServiceV0 service;

	public ConfigRegistryServiceV0 getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("ConfigRegistryService is not available.");
		}
		return this.service;
	}

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		// ConfigRegistryService configRegistryService = getService(ConfigRegistryService.class);
		ConfigRegistryServiceV0 configRegistryService = getService();
		if (configRegistryService != null) {
			return Response.ok(1).build();
		}
		return Response.ok(0).build();
	}

}
