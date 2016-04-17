package org.nb.mgm.ws.resources;

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

import org.nb.common.rest.dto.ErrorDTO;
import org.nb.common.util.Util;
import org.nb.mgm.model.MetaSector;
import org.nb.mgm.model.MetaSpace;
import org.nb.mgm.query.MetaSectorQuery;
import org.nb.mgm.query.MetaSectorQuery.MetaSectorQueryBuilder;
import org.nb.mgm.service.MgmException;
import org.nb.mgm.service.MgmService;
import org.nb.mgm.ws.dto.DTOConverter;
import org.nb.mgm.ws.dto.MetaSectorDTO;
import org.nb.mgm.ws.dto.MetaSpaceDTO;
import org.nb.mgm.ws.dto.StatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MetaSector web service resource
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors/?name={name}&filter={filter}
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors/{metaSectorId}
 * 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/metasectors (Body parameter: MetaSectorDTO)
 * 
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/metasectors (Body parameter: MetaSectorDTO)
 * 
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/metasectors/{metaSectorId}
 * 
 */
@Path("/metasectors")
@Produces(MediaType.APPLICATION_JSON)
public class MetaSectorResource {

	protected static Logger logger = LoggerFactory.getLogger(MetaSectorResource.class);

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

		MgmService mgm = getMgmService();
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
		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
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

		MgmService mgm = getMgmService();
		try {
			MetaSector metaSector = mgm.getMetaSector(metaSectorId);
			if (metaSector == null) {
				ErrorDTO metaSectorNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "MetaSector cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(metaSectorNotFoundError).build();
			}
			metaSectorDTO = DTOConverter.getInstance().toDTO(metaSector);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
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

		MgmService mgm = getMgmService();

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
		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		handleSave(mgm);

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

		MgmService mgm = getMgmService();
		try {
			String id = metaSectorDTO.getId();
			String name = metaSectorDTO.getName();
			String description = metaSectorDTO.getDescription();

			MetaSector metaSector = new MetaSector();
			metaSector.setId(id);
			metaSector.setName(name);
			metaSector.setDescription(description);

			mgm.updateMetaSector(metaSector);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		handleSave(mgm);

		StatusDTO statusDTO = new StatusDTO("200", "success", "MetaSector is updated successfully.");
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

		MgmService mgm = getMgmService();
		try {
			mgm.deleteMetaSector(metaSectorId);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		handleSave(mgm);

		StatusDTO statusDTO = new StatusDTO("200", "success", "MetaSector is deleted successfully.");
		return Response.ok().entity(statusDTO).build();
	}

}
