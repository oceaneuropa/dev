package org.origin.common.rest.server;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@javax.ws.rs.Path("/ping")
@Produces(MediaType.APPLICATION_JSON)
public class PingableResource extends AbstractWSApplicationResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		return Response.ok(String.valueOf(1)).build();
	}

}
