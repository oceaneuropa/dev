package org.nb.home.ws;

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

import org.nb.home.service.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class HomeApiResource {

	protected static Logger logger = LoggerFactory.getLogger(HomeApiResource.class);

	@Context
	protected UriInfo uriInfo;

	@Context
	protected Providers providers;

	protected HomeService getHomeService() {
		HomeService homeService = this.providers.getContextResolver(HomeService.class, MediaType.APPLICATION_JSON_TYPE).getContext(HomeService.class);
		if (homeService == null) {
			throw new WebApplicationException(Status.SERVICE_UNAVAILABLE);
		}
		return homeService;
	}

	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		System.out.println("/ping -> HomeApiResource.ping()");
		return Response.ok(String.valueOf(getHomeService().ping())).build();
	}

}
