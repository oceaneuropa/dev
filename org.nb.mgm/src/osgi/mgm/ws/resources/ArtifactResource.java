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

import osgi.mgm.common.util.Util;
import osgi.mgm.service.MgmException;
import osgi.mgm.service.MgmService;
import osgi.mgm.service.model.Artifact;
import osgi.mgm.service.model.ArtifactQuery;
import osgi.mgm.service.model.MetaSector;
import osgi.mgm.ws.dto.ArtifactDTO;
import osgi.mgm.ws.dto.DTOConverter;
import osgi.mgm.ws.dto.ErrorDTO;
import osgi.mgm.ws.dto.MetaSectorDTO;
import osgi.mgm.ws.dto.StatusDTO;

/**
 * Artifact web service server resource
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts?filter={filter}
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts/{artifactId}
 * 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts (Body parameter: ArtifactDTO)
 * 
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts (Body parameter: ArtifactDTO)
 * 
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts/{artifactId}
 * 
 */
@Path("{metaSectorId}/artifacts")
@Produces(MediaType.APPLICATION_JSON)
public class ArtifactResource {

	protected static Logger logger = LoggerFactory.getLogger(ArtifactResource.class);

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

	/**
	 * Get all Artifacts in a MetaSector.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts
	 * 
	 * @return
	 */
	public Response getArtifacts( //
			@PathParam("metaSectorId") String metaSectorId //
	) {
		List<ArtifactDTO> artifactDTOs = new ArrayList<ArtifactDTO>();

		MgmService mgm = getMgmService();
		try {
			// Find MetaSector by metaSectorId.
			MetaSectorDTO metaSectorDTO = null;
			MetaSector metaSector = mgm.getMetaSector(metaSectorId);
			if (metaSector != null) {
				metaSectorDTO = DTOConverter.getInstance().toDTO(metaSector);
			}

			// Get all Artifacts in the MetaSector.
			for (Artifact artifact : mgm.getArtifacts(metaSectorId)) {
				ArtifactDTO artifactDTO = DTOConverter.getInstance().toDTO(artifact);

				// Set MetaSector DTO
				artifactDTO.setMetaSector(metaSectorDTO);

				artifactDTOs.add(artifactDTO);
			}

		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(artifactDTOs).build();
	}

	/**
	 * Get all Artifacts in a MetaSector by filter.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts?filter={filter}
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArtifacts( //
			@PathParam("metaSectorId") String metaSectorId, //
			@QueryParam("filter") String filter //
	) {
		List<ArtifactDTO> artifactDTOs = new ArrayList<ArtifactDTO>();

		MgmService mgm = getMgmService();
		try {
			// 1. Find MetaSector by metaSectorId.
			MetaSectorDTO metaSectorDTO = null;
			MetaSector metaSector = mgm.getMetaSector(metaSectorId);
			if (metaSector != null) {
				metaSectorDTO = DTOConverter.getInstance().toDTO(metaSector);
			}

			// 2. Get all Artifacts in the MetaSector and matched by query.
			List<Artifact> artifacts = null;
			if (filter != null) {
				ArtifactQuery artifactQuery = ArtifactQuery.newBuilder().withFilter(filter).build();
				artifacts = mgm.getArtifacts(metaSectorId, artifactQuery);
			} else {
				artifacts = mgm.getArtifacts(metaSectorId);
			}

			for (Artifact artifact : artifacts) {
				ArtifactDTO artifactDTO = DTOConverter.getInstance().toDTO(artifact);

				// 3. Set MetaSector DTO
				artifactDTO.setMetaSector(metaSectorDTO);

				artifactDTOs.add(artifactDTO);
			}

		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(artifactDTOs).build();
	}

	/**
	 * Get Artifact by Artifact Id.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts/{artifactId}
	 * 
	 * @param artifactId
	 * @return
	 */
	@GET
	@Path("/{artifactId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArtifact( //
			@PathParam("metaSectorId") String metaSectorId, //
			@PathParam("artifactId") String artifactId //
	) {
		ArtifactDTO artifactDTO = null;

		MgmService mgm = getMgmService();
		try {
			// 1. Find Artifact by artifactId and convert to DTO
			Artifact artifact = mgm.getArtifact(artifactId);
			if (artifact == null) {
				ErrorDTO artifactNotFoundError = new ErrorDTO("Artifact cannot be found.");
				return Response.ok().entity(artifactNotFoundError).build();
			}
			if (metaSectorId != null) {
				if (artifact.getMetaSector() == null || !metaSectorId.equals(artifact.getMetaSector().getId())) {
					ErrorDTO metaSectorIdNotMatchError = new ErrorDTO("The metaSectorId that the Artifact has does not match the given metaSectorId.");
					return Response.ok().entity(metaSectorIdNotMatchError).build();
				}
			}
			artifactDTO = DTOConverter.getInstance().toDTO(artifact);

			// 2. Set MetaSector DTO
			MetaSector metaSector = mgm.getMetaSector(metaSectorId);
			if (metaSector != null) {
				MetaSectorDTO metaSectorDTO = DTOConverter.getInstance().toDTO(metaSector);
				artifactDTO.setMetaSector(metaSectorDTO);
			}

		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(artifactDTO).build();
	}

	/**
	 * Add a Artifact to a MetaSector.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts
	 * 
	 * Body parameter: ArtifactDTO
	 * 
	 * @param artifactDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createArtifact( //
			@PathParam("metaSectorId") String metaSectorId, //
			ArtifactDTO artifactDTO //
	) {
		// 1. Validate parameters.
		if (artifactDTO == null) {
			ErrorDTO nullArtifactDTOError = new ErrorDTO("artifactDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullArtifactDTOError).build();
		}

		// 2. Always get management service first.
		MgmService mgm = getMgmService();

		// 3. Create Artifact runtime model with parameters.
		Artifact newArtifact = new Artifact();

		String id = artifactDTO.getId();
		String name = artifactDTO.getName();
		String description = artifactDTO.getDescription();

		String version = artifactDTO.getVersion();
		String filePath = artifactDTO.getFilePath();
		String fileName = artifactDTO.getFileName();
		long fileSize = artifactDTO.getFileSize();
		long checksum = artifactDTO.getChecksum();

		newArtifact.setId(id);
		newArtifact.setName(name);
		newArtifact.setDescription(description);

		newArtifact.setVersion(version);
		newArtifact.setFilePath(filePath);
		newArtifact.setFileName(fileName);
		newArtifact.setFileSize(fileSize);
		newArtifact.setChecksum(checksum);

		// 4. Add Artifact to MetaSector.
		try {
			mgm.addArtifact(metaSectorId, newArtifact);

			if (Util.compare(id, newArtifact.getId()) != 0) {
				artifactDTO.setId(newArtifact.getId());
			}
		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(artifactDTO).build();
	}

	/**
	 * Update Artifact information.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts
	 * 
	 * Body parameter: MetaSpaceDTO
	 * 
	 * @param artifactDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateArtifact(ArtifactDTO artifactDTO) {
		// 1. Validate parameters.
		if (artifactDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("artifactDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		// 2. Always get management service first.
		MgmService mgm = getMgmService();

		try {
			// 3. Create MetaSpace runtime model with parameters.
			Artifact artifact = new Artifact();

			String id = artifactDTO.getId();
			String name = artifactDTO.getName();
			String description = artifactDTO.getDescription();

			String version = artifactDTO.getVersion();
			String filePath = artifactDTO.getFilePath();
			String fileName = artifactDTO.getFileName();
			long fileSize = artifactDTO.getFileSize();
			long checksum = artifactDTO.getChecksum();

			artifact.setId(id);
			artifact.setName(name);
			artifact.setDescription(description);

			artifact.setVersion(version);
			artifact.setFilePath(filePath);
			artifact.setFileName(fileName);
			artifact.setFileSize(fileSize);
			artifact.setChecksum(checksum);

			// 4. Update Artifact.
			mgm.updateArtifact(artifact);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO("200", "success", "Artifact is updated successfully.");
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Delete a Artifact from a MetaSector
	 * 
	 * 
	 * 
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts/{artifactId}
	 * 
	 * @param metaSectorId
	 * @param artifactId
	 * @return
	 */
	@DELETE
	@Path("/{artifactId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteArtifact( //
			@PathParam(value = "metaSectorId") String metaSectorId, //
			@PathParam(value = "artifactId") String artifactId //
	) {
		if (artifactId == null || artifactId.isEmpty()) {
			ErrorDTO nullArtifactIdError = new ErrorDTO("artifactId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullArtifactIdError).build();
		}

		MgmService mgm = getMgmService();
		try {
			mgm.deleteArtifact(artifactId);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO("200", "success", "Artifact is deleted successfully.");
		return Response.ok().entity(statusDTO).build();
	}

}
