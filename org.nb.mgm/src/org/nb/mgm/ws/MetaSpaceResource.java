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

import org.nb.mgm.exception.ManagementException;
import org.nb.mgm.model.dto.ArtifactDTO;
import org.nb.mgm.model.dto.DTOConverter;
import org.nb.mgm.model.dto.MetaSectorDTO;
import org.nb.mgm.model.dto.MetaSpaceDTO;
import org.nb.mgm.model.query.MetaSpaceQuery;
import org.nb.mgm.model.query.MetaSpaceQuery.MetaSpaceQueryBuilder;
import org.nb.mgm.model.runtime.Artifact;
import org.nb.mgm.model.runtime.MetaSector;
import org.nb.mgm.model.runtime.MetaSpace;
import org.nb.mgm.service.ManagementService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;
import org.origin.common.util.Util;

/*
 * MetaSpace web service resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces?name={name}&filter={filter}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces/{metaSpaceId}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces (Body parameter: MetaSpaceDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces (Body parameter: MetaSpaceDTO)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces/{metaSpaceId}
 * 
 */
@Path("{metaSectorId}/metaspaces")
@Produces(MediaType.APPLICATION_JSON)
public class MetaSpaceResource extends AbstractApplicationResource {

	/**
	 * Get MetaSpaces in a MetaSector by query parameters.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces?name={name}&filter={filter}
	 * 
	 * @param metaSectorId
	 * @param name
	 * @param filter
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMetaSpaces( //
			@PathParam("metaSectorId") String metaSectorId, //
			@QueryParam("name") String name, //
			@QueryParam("filter") String filter //
	) {
		List<MetaSpaceDTO> metaSpaceDTOs = new ArrayList<MetaSpaceDTO>();

		ManagementService mgm = getService(ManagementService.class);
		try {
			// 1. Find MetaSector by metaSectorId.
			MetaSectorDTO metaSectorDTO = null;
			MetaSector metaSector = mgm.getMetaSector(metaSectorId);
			if (metaSector != null) {
				metaSectorDTO = DTOConverter.getInstance().toDTO(metaSector);
			}

			// 2. Get all MetaSpaces in the MetaSector and matched by query.
			List<MetaSpace> metaSpaces = null;
			if (name != null || filter != null) {
				MetaSpaceQueryBuilder builder = MetaSpaceQuery.newBuilder();
				if (name != null) {
					builder.withName(name);
				}
				if (filter != null) {
					builder.withFilter(filter);
				}
				MetaSpaceQuery metaSpaceQuery = builder.build();
				metaSpaces = mgm.getMetaSpaces(metaSectorId, metaSpaceQuery);
			} else {
				metaSpaces = mgm.getMetaSpaces(metaSectorId);
			}

			if (metaSpaces != null) {
				for (MetaSpace metaSpace : metaSpaces) {
					MetaSpaceDTO metaSpaceDTO = DTOConverter.getInstance().toDTO(metaSpace);

					// 3. Set MetaSector DTO
					metaSpaceDTO.setMetaSector(metaSectorDTO);

					// 4. Set deployed Artifacts DTO
					List<ArtifactDTO> deployedArtifactDTOs = new ArrayList<ArtifactDTO>();
					List<String> artifactIds = metaSpace.getDeployedArtifactIds();
					for (String artifactId : artifactIds) {
						Artifact artifact = mgm.getArtifact(artifactId);
						if (artifact != null) {
							ArtifactDTO artifactDTO = DTOConverter.getInstance().toDTO(artifact);
							deployedArtifactDTOs.add(artifactDTO);
						}
					}
					metaSpaceDTO.setDeployedArtifacts(deployedArtifactDTOs);

					metaSpaceDTOs.add(metaSpaceDTO);
				}
			}

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(metaSpaceDTOs).build();
	}

	/**
	 * Get MetaSpace by MetaSpace Id.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces/{metaSpaceId}
	 * 
	 * @param metaSectorId
	 * @param metaSpaceId
	 * @return
	 */
	@GET
	@Path("{metaSpaceId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMetaSpace( //
			@PathParam("metaSectorId") String metaSectorId, //
			@PathParam("metaSpaceId") String metaSpaceId //
	) {
		MetaSpaceDTO metaSpaceDTO = null;

		ManagementService mgm = getService(ManagementService.class);
		try {
			// 1. Find MetaSpace by metaSpaceId and convert to DTO
			MetaSpace metaSpace = mgm.getMetaSpace(metaSpaceId);
			if (metaSpace == null) {
				ErrorDTO metaSpaceNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "MetaSpace cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(metaSpaceNotFoundError).build();
			}
			if (metaSectorId != null && (metaSpace.getMetaSector() == null || !metaSectorId.equals(metaSpace.getMetaSector().getId()))) {
				ErrorDTO metaSectorIdNotMatchError = new ErrorDTO(String.valueOf(Status.INTERNAL_SERVER_ERROR.getStatusCode()), "The metaSectorId that the MetaSpace has does not match the given metaSectorId.");
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(metaSectorIdNotMatchError).build();
			}
			metaSpaceDTO = DTOConverter.getInstance().toDTO(metaSpace);

			// 2. Set MetaSector DTO
			MetaSector metaSector = mgm.getMetaSector(metaSectorId);
			if (metaSector != null) {
				MetaSectorDTO metaSectorDTO = DTOConverter.getInstance().toDTO(metaSector);
				metaSpaceDTO.setMetaSector(metaSectorDTO);
			}

			// 3. Set deployed Artifacts DTO
			List<ArtifactDTO> deployedArtifactDTOs = new ArrayList<ArtifactDTO>();
			List<String> artifactIds = metaSpace.getDeployedArtifactIds();
			for (String artifactId : artifactIds) {
				Artifact artifact = mgm.getArtifact(artifactId);
				if (artifact != null) {
					ArtifactDTO artifactDTO = DTOConverter.getInstance().toDTO(artifact);
					deployedArtifactDTOs.add(artifactDTO);
				}
			}
			metaSpaceDTO.setDeployedArtifacts(deployedArtifactDTOs);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(metaSpaceDTO).build();
	}

	/**
	 * Add a MetaSpace to a MetaSector.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces
	 * 
	 * Body parameter: MetaSpaceDTO
	 * 
	 * @param metaSectorId
	 * @param metaSpaceDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createMetaSpace( //
			@PathParam("metaSectorId") String metaSectorId, //
			MetaSpaceDTO metaSpaceDTO //
	) {
		// 1. Validate parameters.
		if (metaSpaceDTO == null) {
			ErrorDTO nullMetaSpaceDTOError = new ErrorDTO("metaSpaceDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullMetaSpaceDTOError).build();
		}

		// 2. Always get management service first.
		ManagementService mgm = getService(ManagementService.class);

		// 3. Create MetaSpace runtime model with parameters.
		MetaSpace newMetaSpace = new MetaSpace();

		String id = metaSpaceDTO.getId();
		String name = metaSpaceDTO.getName();
		String description = metaSpaceDTO.getDescription();

		newMetaSpace.setId(id);
		newMetaSpace.setName(name);
		newMetaSpace.setDescription(description);

		// 4. Add MetaSpace to MetaSector.
		try {
			mgm.addMetaSpace(metaSectorId, newMetaSpace);

			if (Util.compare(id, newMetaSpace.getId()) != 0) {
				metaSpaceDTO.setId(newMetaSpace.getId());
			}
			if (Util.compare(name, newMetaSpace.getName()) != 0) {
				metaSpaceDTO.setName(newMetaSpace.getName());
			}
		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(metaSpaceDTO).build();
	}

	/**
	 * Update MetaSpace information.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces
	 * 
	 * Body parameter: MetaSpaceDTO
	 * 
	 * @param metaSpaceDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMetaSpace(MetaSpaceDTO metaSpaceDTO) {
		// 1. Validate parameters.
		if (metaSpaceDTO == null) {
			ErrorDTO nullMetaSpaceDTOError = new ErrorDTO("metaSpaceDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullMetaSpaceDTOError).build();
		}

		// 2. Always get management service first.
		ManagementService mgm = getService(ManagementService.class);

		try {
			// 3. Create MetaSpace runtime model with parameters.
			MetaSpace metaSpace = new MetaSpace();

			String id = metaSpaceDTO.getId();
			String name = metaSpaceDTO.getName();
			String description = metaSpaceDTO.getDescription();

			metaSpace.setId(id);
			metaSpace.setName(name);
			metaSpace.setDescription(description);

			// 4. Update MetaSpace.
			mgm.updateMetaSpace(metaSpace);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO("200", "success", "MetaSpace is updated successfully.");
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Delete a MetaSpace from MetaSector.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces/{metaSpaceId}
	 * 
	 * @param metaSectorId
	 * @param metaSpaceId
	 * @return
	 */
	@DELETE
	@Path("/{metaSpaceId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteMetaSpace( //
			@PathParam(value = "metaSectorId") String metaSectorId, //
			@PathParam(value = "metaSpaceId") String metaSpaceId //
	) {
		if (metaSpaceId == null || metaSpaceId.isEmpty()) {
			ErrorDTO nullMetaSpaceIdError = new ErrorDTO("metaSpaceId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullMetaSpaceIdError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			mgm.deleteMetaSpace(metaSpaceId);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO("200", "success", "MetaSpace is deleted successfully.");
		return Response.ok().entity(statusDTO).build();
	}

}
