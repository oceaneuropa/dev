package org.nb.mgm.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.nb.mgm.model.dto.DTOConverter;
import org.nb.mgm.model.dto.HomeDTO;
import org.nb.mgm.model.dto.MachineDTO;
import org.nb.mgm.model.dto.MetaSectorDTO;
import org.nb.mgm.model.dto.MetaSpaceDTO;
import org.nb.mgm.model.exception.ManagementException;
import org.nb.mgm.model.query.HomeQuery;
import org.nb.mgm.model.query.HomeQuery.HomeQueryBuilder;
import org.nb.mgm.model.runtime.Home;
import org.nb.mgm.model.runtime.Machine;
import org.nb.mgm.model.runtime.MetaSector;
import org.nb.mgm.model.runtime.MetaSpace;
import org.nb.mgm.service.ManagementService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

/*
 * Home resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes?name={name}&url={url}&status={status}&filter={filter}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes (Body parameter: HomeDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes (Body parameter: HomeDTO)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}
 * 
 */
@Path("/{machineId}/homes")
@Produces(MediaType.APPLICATION_JSON)
public class HomeResource extends AbstractApplicationResource {

	/**
	 * Get Homes in a Machine by query parameters.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes?name={name}&url={url}&status={status}&filter={filter}
	 * 
	 * @param machineId
	 * @param name
	 * @param url
	 * @param status
	 * @param filter
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHomes( //
			@PathParam("machineId") String machineId, //
			@QueryParam("name") String name, //
			@QueryParam("status") String status, //
			@QueryParam("filter") String filter //
	) {
		List<HomeDTO> homeDTOs = new ArrayList<HomeDTO>();

		ManagementService mgm = getService(ManagementService.class);
		try {
			// 1. Find Machine by machineId
			MachineDTO machineDTO = null;
			Machine machine = mgm.getMachine(machineId);
			if (machine != null) {
				machineDTO = DTOConverter.getInstance().toDTO(machine);
			}

			// 2. Get Homes in the Machine and matched by query.
			List<Home> homes = null;
			if (name != null || status != null || filter != null) {
				HomeQueryBuilder builder = HomeQuery.newBuilder();
				if (name != null) {
					builder.withName(name);
				}
				if (status != null) {
					builder.withStatus(status);
				}
				if (filter != null) {
					builder.withFilter(filter);
				}
				HomeQuery homeQuery = builder.build();
				homes = mgm.getHomes(machineId, homeQuery);
			} else {
				homes = mgm.getHomes(machineId);
			}

			for (Home home : homes) {
				HomeDTO homeDTO = DTOConverter.getInstance().toDTO(home);

				// 3. Set Machine DTO
				homeDTO.setMachine(machineDTO);

				// 4. Set joined MetaSectors DTO
				List<MetaSectorDTO> joinedMetaSectorDTOs = new ArrayList<MetaSectorDTO>();
				List<String> metaSectorIds = home.getJoinedMetaSectorIds();
				for (String metaSectorId : metaSectorIds) {
					MetaSector metaSector = mgm.getMetaSector(metaSectorId);
					if (metaSector != null) {
						MetaSectorDTO metaSectorDTO = DTOConverter.getInstance().toDTO(metaSector);
						joinedMetaSectorDTOs.add(metaSectorDTO);
					}
				}
				homeDTO.setJoinedMetaSectors(joinedMetaSectorDTOs);

				// 5. Set joined MetaSpaces DTO
				List<MetaSpaceDTO> joinedMetaSpaceDTOs = new ArrayList<MetaSpaceDTO>();
				List<String> metaSpaceIds = home.getJoinedMetaSpaceIds();
				for (String metaSpaceId : metaSpaceIds) {
					MetaSpace metaSpace = mgm.getMetaSpace(metaSpaceId);
					if (metaSpace != null) {
						MetaSpaceDTO metaSpaceDTO = DTOConverter.getInstance().toDTO(metaSpace);
						joinedMetaSpaceDTOs.add(metaSpaceDTO);
					}
				}
				homeDTO.setJoinedMetaSpaces(joinedMetaSpaceDTOs);

				homeDTOs.add(homeDTO);
			}

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(homeDTOs).build();
	}

	/**
	 * Get Home by home Id.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}
	 * 
	 * @param homeId
	 * @return
	 */
	@GET
	@Path("{homeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHome( //
			@PathParam("machineId") String machineId, //
			@PathParam("homeId") String homeId //
	) {
		HomeDTO homeDTO = null;

		ManagementService mgm = getService(ManagementService.class);
		try {
			// 1. Find Home by homeId and convert to DTO
			Home home = mgm.getHome(homeId);
			if (home == null) {
				ErrorDTO homeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Home cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(homeNotFoundError).build();
			}
			if (machineId != null && (home.getMachine() == null || !machineId.equals(home.getMachine().getId()))) {
				ErrorDTO machineIdNotMatchError = new ErrorDTO(String.valueOf(Status.INTERNAL_SERVER_ERROR.getStatusCode()), "The machineId that the Home has does not match the given machineId.");
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(machineIdNotMatchError).build();
			}
			homeDTO = DTOConverter.getInstance().toDTO(home);

			// 2. Set Machine DTO
			Machine machine = mgm.getMachine(machineId);
			if (machine != null) {
				MachineDTO machineDTO = DTOConverter.getInstance().toDTO(machine);
				homeDTO.setMachine(machineDTO);
			}

			// 3. Set joined MetaSectors DTO
			List<MetaSectorDTO> joinedMetaSectorDTOs = new ArrayList<MetaSectorDTO>();
			List<String> metaSectorIds = home.getJoinedMetaSectorIds();
			for (String metaSectorId : metaSectorIds) {
				MetaSector metaSector = mgm.getMetaSector(metaSectorId);
				if (metaSector != null) {
					MetaSectorDTO metaSectorDTO = DTOConverter.getInstance().toDTO(metaSector);
					joinedMetaSectorDTOs.add(metaSectorDTO);
				}
			}
			homeDTO.setJoinedMetaSectors(joinedMetaSectorDTOs);

			// 4. Set joined MetaSpaces DTO
			List<MetaSpaceDTO> joinedMetaSpaceDTOs = new ArrayList<MetaSpaceDTO>();
			List<String> metaSpaceIds = home.getJoinedMetaSpaceIds();
			for (String metaSpaceId : metaSpaceIds) {
				MetaSpace metaSpace = mgm.getMetaSpace(metaSpaceId);
				if (metaSpace != null) {
					MetaSpaceDTO metaSpaceDTO = DTOConverter.getInstance().toDTO(metaSpace);
					joinedMetaSpaceDTOs.add(metaSpaceDTO);
				}
			}
			homeDTO.setJoinedMetaSpaces(joinedMetaSpaceDTOs);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(homeDTO).build();
	}

	/**
	 * Add a Home to a Machine.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes
	 * 
	 * Body parameter: HomeDTO
	 * 
	 * @param machineId
	 * @param newHomeRequestDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createHome(@PathParam("machineId") String machineId, HomeDTO newHomeRequestDTO) {
		// 1. Validate parameters.
		if (newHomeRequestDTO == null) {
			ErrorDTO nullHomeDTOError = new ErrorDTO("homeDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullHomeDTOError).build();
		}

		// 2. Always get management service first.
		ManagementService mgm = getService(ManagementService.class);

		HomeDTO newHomeDTO = null;
		try {
			// 3. Create Home runtime model with parameters.
			Home newHomeRequest = new Home();

			String id = newHomeRequestDTO.getId();
			String name = newHomeRequestDTO.getName();
			String description = newHomeRequestDTO.getDescription();

			newHomeRequest.setId(id);
			newHomeRequest.setName(name);
			newHomeRequest.setDescription(description);

			// 4. Add Home to Machine.
			Home newHome = mgm.addHome(machineId, newHomeRequest);
			if (newHome == null) {
				ErrorDTO homeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "New Home cannot be added.");
				return Response.status(Status.NOT_FOUND).entity(homeNotFoundError).build();
			}
			newHomeDTO = DTOConverter.getInstance().toDTO(newHome);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(newHomeDTO).build();
	}

	/**
	 * Update Home information.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes
	 * 
	 * Body parameter: HomeDTO
	 * 
	 * @param homeDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateHome(HomeDTO homeDTO) {
		// 1. Validate parameters.
		if (homeDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("homeDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		// 2. Always get management service first.
		ManagementService mgm = getService(ManagementService.class);

		try {
			// 3. Create Home runtime model with parameters.
			Home home = new Home();

			String id = homeDTO.getId();
			String name = homeDTO.getName();
			String description = homeDTO.getDescription();

			home.setId(id);
			home.setName(name);
			home.setDescription(description);

			// 4. Update Home.
			mgm.updateHome(home);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO("200", "success", "Home is updated successfully.");
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Delete a Home from a Machine.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}
	 * 
	 * @param machineId
	 * @param homeId
	 * @return
	 */
	@DELETE
	@Path("/{homeId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteHome( //
			@PathParam(value = "machineId") String machineId, //
			@PathParam(value = "homeId") String homeId //
	) {
		if (homeId == null || homeId.isEmpty()) {
			ErrorDTO nullHomeIdError = new ErrorDTO("homeId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullHomeIdError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			mgm.deleteHome(homeId);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO("200", "success", "Home is deleted successfully.");
		return Response.ok().entity(statusDTO).build();
	}

}
