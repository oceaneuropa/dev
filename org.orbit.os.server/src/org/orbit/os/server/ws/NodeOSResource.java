package org.orbit.os.server.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.os.server.service.NodeOS;
import org.origin.common.rest.server.AbstractWSApplicationResource;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class NodeOSResource extends AbstractWSApplicationResource {

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		NodeOS nodeOS = getService(NodeOS.class);
		if (nodeOS != null) {
			return Response.ok(1).build();
		}
		return Response.ok(0).build();
	}

}
