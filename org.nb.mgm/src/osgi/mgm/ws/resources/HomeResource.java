package osgi.mgm.ws.resources;

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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import osgi.mgm.runtime.model.Home;
import osgi.mgm.runtime.model.Machine;
import osgi.mgm.runtime.model.MetaSector;
import osgi.mgm.runtime.model.MetaSpace;
import osgi.mgm.runtime.query.HomeQuery;
import osgi.mgm.runtime.query.HomeQuery.HomeQueryBuilder;
import osgi.mgm.service.MgmException;
import osgi.mgm.service.MgmService;
import osgi.mgm.util.Util;
import osgi.mgm.ws.dto.DTOConverter;
import osgi.mgm.ws.dto.ErrorDTO;
import osgi.mgm.ws.dto.HomeDTO;
import osgi.mgm.ws.dto.MachineDTO;
import osgi.mgm.ws.dto.MetaSectorDTO;
import osgi.mgm.ws.dto.MetaSpaceDTO;
import osgi.mgm.ws.dto.StatusDTO;

/**
 * Home web service server resource
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes?name={name}&url={url}&status={status}&filter={filter}
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}
 * 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes (Body parameter: HomeDTO)
 * 
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes (Body parameter: HomeDTO)
 * 
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}
 * 
 */
@Path("/{machineId}/homes")
@Produces(MediaType.APPLICATION_JSON)
public class HomeResource {

	protected static Logger logger = LoggerFactory.getLogger(HomeResource.class);

	@Context
	protected Providers providers;

	@Context
	protected UriInfo uriInfo;

	protected MgmService getMgmService() {
		MgmService mgm = this.providers.getContextResolver(MgmService.class, MediaType.APPLICATION_JSON_TYPE).getContext(MgmService.class);
		if (mgm == null) {
			throw new WebApplicationException(Status.SERVICE_UNAVAILABLE);
		}
		return mgm;
	}

	/**
	 * Handle MgmException and create ErrorDTO from it.
	 * 
	 * @param e
	 * @return
	 */
	protected ErrorDTO handleError(MgmException e) {
		e.printStackTrace();
		logger.error(e.getMessage());
		return DTOConverter.getInstance().toDTO(e);
	}

	protected void handleSave(MgmService mgm) {
		if (!mgm.isAutoSave()) {
			mgm.save();
		}
	}

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
			@QueryParam("url") String url, //
			@QueryParam("status") String status, //
			@QueryParam("filter") String filter //
	) {
		List<HomeDTO> homeDTOs = new ArrayList<HomeDTO>();

		MgmService mgm = getMgmService();
		try {
			// 1. Find Machine by machineId
			MachineDTO machineDTO = null;
			Machine machine = mgm.getMachine(machineId);
			if (machine != null) {
				machineDTO = DTOConverter.getInstance().toDTO(machine);
			}

			// 2. Get Homes in the Machine and matched by query.
			List<Home> homes = null;
			if (name != null || url != null || status != null || filter != null) {
				HomeQueryBuilder builder = HomeQuery.newBuilder();
				if (name != null) {
					builder.withName(name);
				}
				if (url != null) {
					builder.withUrl(url);
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

		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
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

		MgmService mgm = getMgmService();
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

		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
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
	 * @param homeDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createHome( //
			@PathParam("machineId") String machineId, //
			HomeDTO homeDTO //
	) {
		// 1. Validate parameters.
		if (homeDTO == null) {
			ErrorDTO nullHomeDTOError = new ErrorDTO("homeDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullHomeDTOError).build();
		}

		// 2. Always get management service first.
		MgmService mgm = getMgmService();

		// 3. Create Home runtime model with parameters.
		Home newHome = new Home();

		String id = homeDTO.getId();
		String name = homeDTO.getName();
		String description = homeDTO.getDescription();
		String url = homeDTO.getUrl();

		newHome.setId(id);
		newHome.setName(name);
		newHome.setDescription(description);
		newHome.setUrl(url);

		// 4. Add Home to Machine.
		try {
			mgm.addHome(machineId, newHome);

			if (Util.compare(id, newHome.getId()) != 0) {
				homeDTO.setId(newHome.getId());
			}
			if (Util.compare(name, newHome.getName()) != 0) {
				homeDTO.setName(newHome.getName());
			}
		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		handleSave(mgm);

		return Response.ok().entity(homeDTO).build();
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
		MgmService mgm = getMgmService();

		try {
			// 3. Create Home runtime model with parameters.
			Home home = new Home();

			String id = homeDTO.getId();
			String name = homeDTO.getName();
			String description = homeDTO.getDescription();
			String url = homeDTO.getUrl();

			home.setId(id);
			home.setName(name);
			home.setDescription(description);
			home.setUrl(url);

			// 4. Update Home.
			mgm.updateHome(home);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		handleSave(mgm);

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

		MgmService mgm = getMgmService();
		try {
			mgm.deleteHome(homeId);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		handleSave(mgm);

		StatusDTO statusDTO = new StatusDTO("200", "success", "Home is deleted successfully.");
		return Response.ok().entity(statusDTO).build();
	}

}
