package org.orbit.platform.runtime.gaia.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.os.model.gaia.dto.WorldDTO;
import org.orbit.os.model.gaia.rto.WorldException;
import org.orbit.platform.runtime.gaia.service.GAIA;
import org.orbit.platform.runtime.gaia.world.World;
import org.orbit.platform.runtime.gaia.world.WorldModelConverter;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/*
 * Worlds web service resource.
 * 
 * {contextRoot} example: 
 * /orbit/v1/gaia
 * 
 * Worlds:
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/worlds 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/worlds/{name}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/worlds/{name}/exists
 */
@Path("/worlds")
@Produces(MediaType.APPLICATION_JSON)
public class WorldsWSResource extends AbstractWSApplicationResource {

	@Inject
	public GAIA service;

	public GAIA getService() throws RuntimeException {
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
		GAIA gaia = getService();
		try {
			List<World> worlds = gaia.getWorlds().getWorlds();
			for (World world : worlds) {
				WorldDTO worldDTO = WorldModelConverter.getInstance().toDTO(world);
				worldDTOs.add(worldDTO);
			}

		} catch (WorldException e) {
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
		GAIA gaia = getService();
		try {
			World world = gaia.getWorlds().get(name);
			if (world == null) {
				ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), String.format("World '%s' does not exist.", name));
				return Response.status(Status.NOT_FOUND).entity(error).build();
			}
			worldDTO = WorldModelConverter.getInstance().toDTO(world);

		} catch (WorldException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(worldDTO).build();
	}

	/**
	 * Check whether a world exists.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/worlds/{name}/exists
	 * 
	 * @param name
	 * @return
	 */
	@GET
	@Path("{name}/exists")
	@Produces(MediaType.APPLICATION_JSON)
	public Response userAccountExists(@PathParam("name") String name) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();

		GAIA gaia = getService();
		try {
			boolean exists = gaia.getWorlds().exists(name);
			result.put("exists", exists);

		} catch (WorldException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(result).build();
	}

}
