package other.orbit.component.runtime.tier1.session.ws;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.runtime.tier1.session.service.OAuth2Service;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.server.AbstractWSApplicationResource;

@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.SYSTEM_ADMIN })
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class OAuth2ServiceWSResource extends AbstractWSApplicationResource {

	@Inject
	public OAuth2Service service;

	public OAuth2Service getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("OAuth2Service is not available.");
		}
		return this.service;
	}

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		OAuth2Service service = getService();
		if (service != null) {
			return Response.ok(1).build();
		}
		return Response.ok(0).build();
	}

}

// OAuth2Service service = getService(OAuth2Service.class);