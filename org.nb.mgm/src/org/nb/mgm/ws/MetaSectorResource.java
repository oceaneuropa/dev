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
import org.nb.mgm.model.dto.MetaSectorDTO;
import org.nb.mgm.model.dto.MetaSpaceDTO;
import org.nb.mgm.model.exception.ManagementException;
import org.nb.mgm.model.query.MetaSectorQuery;
import org.nb.mgm.model.query.MetaSectorQuery.MetaSectorQueryBuilder;
import org.nb.mgm.model.runtime.MetaSector;
import org.nb.mgm.model.runtime.MetaSpace;
import org.nb.mgm.service.ManagementService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.util.Util;

/*
 * MetaSector web service resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors/?name={name}&filter={filter} 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors/{metaSectorId}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/metasectors (Body parameter: MetaSectorDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/metasectors (Body parameter: MetaSectorDTO)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/metasectors/{metaSectorId}
 * 
 */
@Path("/metasectors")
@Produces(MediaType.APPLICATION_JSON)
public class MetaSectorResource extends AbstractWSApplicationResource {

	/**
	 * Get MetaSectors by query parameters.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors/?name={name}&filter={filter}
	 * 
	 * @param name
	 * @param filter
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMetaSectors( //
			@QueryParam("name") String name, //
			@QueryParam("filter") String filter //
	) {
		List<MetaSectorDTO> metaSectorDTOs = new ArrayList<MetaSectorDTO>();

		ManagementService mgm = getService(ManagementService.class);
		try {
			// Get MetaSectors matched by query.
			List<MetaSector> metaSectors = null;
			if (name != null || filter != null) {
				MetaSectorQueryBuilder builder = MetaSectorQuery.newBuilder();
				if (name != null) {
					builder.withName(name);
				}
				if (filter != null) {
					builder.withFilter(filter);
				}
				MetaSectorQuery metaSectorQuery = builder.build();
				metaSectors = mgm.getMetaSectors(metaSectorQuery);
			} else {
				metaSectors = mgm.getMetaSectors();
			}

			for (MetaSector metaSector : metaSectors) {
				MetaSectorDTO metaSectorDTO = DTOConverter.getInstance().toDTO(metaSector);

				List<MetaSpaceDTO> metaSpaceDTOs = new ArrayList<MetaSpaceDTO>();
				for (MetaSpace metaSpace : mgm.getMetaSpaces(metaSector.getId())) {
					MetaSpaceDTO metaSpaceDTO = DTOConverter.getInstance().toDTO(metaSpace);
					// metaSpaceDTO.setMetaSector(metaSectorDTO);
					metaSpaceDTOs.add(metaSpaceDTO);
				}
				metaSectorDTO.setMetaSpaces(metaSpaceDTOs);

				metaSectorDTOs.add(metaSectorDTO);
			}
		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(metaSectorDTOs).build();
	}

	/**
	 * Get MetaSector by Id.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors/{metaSectorId}
	 * 
	 * @param metaSectorId
	 * @return
	 */
	@GET
	@Path("{metaSectorId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMetaSector(@PathParam("metaSectorId") String metaSectorId) {
		MetaSectorDTO metaSectorDTO = null;

		ManagementService mgm = getService(ManagementService.class);
		try {
			MetaSector metaSector = mgm.getMetaSector(metaSectorId);
			if (metaSector == null) {
				ErrorDTO metaSectorNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "MetaSector cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(metaSectorNotFoundError).build();
			}
			metaSectorDTO = DTOConverter.getInstance().toDTO(metaSector);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(metaSectorDTO).build();
	}

	/**
	 * Add a MetaSector to the cluster.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/metasectors
	 * 
	 * Body parameter: MetaSectorDTO
	 * 
	 * @param metaSectorDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createMetaSector(MetaSectorDTO metaSectorDTO) {
		if (metaSectorDTO == null) {
			ErrorDTO nullMetaSectorDTOError = new ErrorDTO("metaSectorDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullMetaSectorDTOError).build();
		}

		ManagementService mgm = getService(ManagementService.class);

		MetaSector newMetaSector = new MetaSector(mgm.getRoot());

		String id = metaSectorDTO.getId();
		String name = metaSectorDTO.getName();
		String description = metaSectorDTO.getDescription();

		newMetaSector.setId(id);
		newMetaSector.setName(name);
		newMetaSector.setDescription(description);

		try {
			mgm.addMetaSector(newMetaSector);

			if (Util.compare(id, newMetaSector.getId()) != 0) {
				metaSectorDTO.setId(newMetaSector.getId());
			}
			if (Util.compare(name, newMetaSector.getName()) != 0) {
				metaSectorDTO.setName(newMetaSector.getName());
			}
		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(metaSectorDTO).build();
	}

	/**
	 * Update MetaSector information.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/metasectors
	 * 
	 * Body parameter: MetaSectorDTO
	 * 
	 * @param metaSectorDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMetaSector(MetaSectorDTO metaSectorDTO) {
		if (metaSectorDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("metaSectorDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			String id = metaSectorDTO.getId();
			String name = metaSectorDTO.getName();
			String description = metaSectorDTO.getDescription();

			MetaSector metaSector = new MetaSector();
			metaSector.setId(id);
			metaSector.setName(name);
			metaSector.setDescription(description);

			mgm.updateMetaSector(metaSector);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "MetaSector is updated successfully.");
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Delete a MetaSector from the cluster.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/metasectors/{metaSectorId}
	 * 
	 * @param metaSectorId
	 * @return
	 */
	@DELETE
	@Path("/{metaSectorId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteMetaSector(@PathParam(value = "metaSectorId") String metaSectorId) {
		if (metaSectorId == null || metaSectorId.isEmpty()) {
			ErrorDTO nullMetaSectorIdError = new ErrorDTO("metaSectorId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullMetaSectorIdError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			mgm.deleteMetaSector(metaSectorId);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "MetaSector is deleted successfully.");
		return Response.ok().entity(statusDTO).build();
	}

}
