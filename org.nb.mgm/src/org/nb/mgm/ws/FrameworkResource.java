package org.nb.mgm.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.origin.common.rest.server.AbstractApplicationResource;

@Path("/")
public class FrameworkResource extends AbstractApplicationResource {

	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		logger.debug("/ping -> FrameworkResource.ping()");

		return Response.ok("1").build();
	}

	@GET
	@Path("/version")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVersion() {
		logger.debug("/version -> FrameworkResource.getVersion()");

		return Response.ok("1.0.0").build();
	}

}
