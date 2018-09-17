package org.orbit.spirit.runtime.earth.ws;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.platform.sdk.http.OrbitRoles;
import org.orbit.spirit.model.gaia.dto.WorldDTO;
import org.orbit.spirit.runtime.earth.service.EarthService;
import org.orbit.spirit.runtime.earth.service.World;
import org.orbit.spirit.runtime.util.ModelConverter;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;

/*
 * Worlds web service resource.
 * 
 * {contextRoot} example: 
 * /orbit/v1/earth
 * 
 * Worlds:
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/worlds 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/worlds/{name}
 * 
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.SYSTEM_ADMIN })
@Path("/worlds")
@Produces(MediaType.APPLICATION_JSON)
public class WorldsWSResource extends AbstractWSApplicationResource {

	@Inject
	public EarthService service;

	public EarthService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("GAIA is not available.");
		}
		return this.service;
	}

	/**
	 * Get user accounts.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/worlds
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWorlds() {
		List<WorldDTO> worldDTOs = new ArrayList<WorldDTO>();
		EarthService earth = getService();
		try {
			World[] worlds = earth.getWorlds();
			for (World world : worlds) {
				WorldDTO worldDTO = ModelConverter.Earth.toDTO(world);
				worldDTOs.add(worldDTO);
			}

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(worldDTOs).build();
	}

	/**
	 * Get a world
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/worlds/{name}
	 * 
	 * @param name
	 * @return
	 */
	@GET
	@Path("{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserAccount(@PathParam("name") String name) {
		WorldDTO worldDTO = null;
		EarthService earth = getService();
		try {
			World world = earth.getWorld(name);
			if (world == null) {
				ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), String.format("World '%s' does not exist.", name));
				return Response.status(Status.NOT_FOUND).entity(error).build();
			}
			worldDTO = ModelConverter.Earth.toDTO(world);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(worldDTO).build();
	}

}

// /**
// * Check whether a world exists.
// *
// * URL (GET): {scheme}://{host}:{port}/{contextRoot}/worlds/{name}/exists
// *
// * @param name
// * @return
// */
// @GET
// @Path("{name}/exists")
// @Produces(MediaType.APPLICATION_JSON)
// public Response userAccountExists(@PathParam("name") String name) {
// Map<String, Boolean> result = new HashMap<String, Boolean>();
//
// GaiaService gaia = getService();
// try {
// boolean exists = gaia.worldExists(name);
// result.put("exists", exists);
//
// } catch (ServerException e) {
// ErrorDTO error = handleError(e, e.getCode(), true);
// return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
// }
// return Response.ok().entity(result).build();
// }
