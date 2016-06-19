package org.nb.mgm.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.nb.mgm.service.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class FrameworkResource {

	protected static Logger logger = LoggerFactory.getLogger(FrameworkResource.class);

	@Context
	protected UriInfo uriInfo;

	@Context
	protected Providers providers;

	protected ManagementService getMgmService() {
		ManagementService mgmService = this.providers.getContextResolver(ManagementService.class, MediaType.APPLICATION_JSON_TYPE).getContext(ManagementService.class);
		if (mgmService == null) {
			throw new WebApplicationException(Status.SERVICE_UNAVAILABLE);
		}
		return mgmService;
	}

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
