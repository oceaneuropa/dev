package org.orbit.component.server.tier1.session.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.server.tier1.session.service.OAuth2Service;
import org.origin.common.rest.server.AbstractWSApplicationResource;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class OAuth2ServiceResource extends AbstractWSApplicationResource {

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		OAuth2Service oauth2Service = getService(OAuth2Service.class);
		if (oauth2Service != null) {
			return Response.ok(1).build();
		}
		return Response.ok(0).build();
	}

}
