package org.nb.home.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
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
import org.origin.common.rest.dto.StatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/sectors")
@Produces(MediaType.APPLICATION_JSON)
public class SectorResource {

	protected static Logger logger = LoggerFactory.getLogger(SectorResource.class);

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

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createSector(String sectorName) {
		HomeService homeService = getHomeService();

		homeService.createSectorFolder(sectorName);

		return Response.ok().entity(StatusDTO.success("Sector is created successfully.")).build();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteSector(String sectorName) {
		HomeService homeService = getHomeService();

		return Response.ok().entity(StatusDTO.success("Sector is deleted successfully.")).build();
	}

}
