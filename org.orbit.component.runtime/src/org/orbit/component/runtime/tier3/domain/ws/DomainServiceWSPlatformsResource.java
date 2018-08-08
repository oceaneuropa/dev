package org.orbit.component.runtime.tier3.domain.ws;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier3.domain.PlatformConfigDTO;
import org.orbit.component.runtime.model.domain.PlatformConfig;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.orbit.component.runtime.util.ModelConverter;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;

/*
 * DomainService Platforms resource.
 * 
 * {contextRoot} example:
 * /orbit/v1/domain
 * 
 * Platforms
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms (Body parameter: PlatformConfigDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms (Body parameter: PlatformConfigDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}
 * 
 */
@Path("/machines/{machineId}/platforms")
@Produces(MediaType.APPLICATION_JSON)
public class DomainServiceWSPlatformsResource extends AbstractWSApplicationResource {

	@Inject
	public DomainManagementService service;

	protected DomainManagementService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("DomainService is not available.");
		}
		return this.service;
	}

	/**
	 * Get platform configurations.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms
	 * 
	 * @param machineId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlatformConfigs(@PathParam("machineId") String machineId) {
		DomainManagementService service = getService();

		List<PlatformConfigDTO> platformConfigDTOs = new ArrayList<PlatformConfigDTO>();
		try {
			List<PlatformConfig> platformConfigs = service.getPlatformConfigs(machineId);
			if (platformConfigs != null) {
				for (PlatformConfig platformConfig : platformConfigs) {
					PlatformConfigDTO platformConfigDTO = ModelConverter.Domain.toDTO(platformConfig);
					platformConfigDTOs.add(platformConfigDTO);
				}
			}
		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(platformConfigDTOs).build();
	}

	/**
	 * Get a platform configuration.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}
	 * 
	 * @param machineId
	 * @return
	 */
	@GET
	@Path("{platformId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlatformConfig(@PathParam("machineId") String machineId, @PathParam("platformId") String platformId) {
		PlatformConfigDTO platformConfigDTO = null;

		DomainManagementService service = getService();
		try {
			PlatformConfig platformConfig = service.getPlatformConfig(machineId, platformId);
			if (platformConfig == null) {
				ErrorDTO notFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), String.format("Platform with id '%s' cannot be found.", platformId));
				return Response.status(Status.NOT_FOUND).entity(notFoundError).build();
			}
			platformConfigDTO = ModelConverter.Domain.toDTO(platformConfig);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(platformConfigDTO).build();
	}

	/**
	 * Add a platform configuration.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms (Body parameter: PlatformConfigDTO)
	 *
	 * @param machineId
	 * @param addPlatformConfigDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addPlatformConfig(@PathParam("machineId") String machineId, PlatformConfigDTO addPlatformConfigDTO) {
		if (addPlatformConfigDTO == null) {
			ErrorDTO nullRequestDTOError = new ErrorDTO("addPlatformConfigDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullRequestDTOError).build();
		}

		boolean succeed = false;
		DomainManagementService service = getService();
		try {
			String platformId = addPlatformConfigDTO.getId();
			if (platformId == null || platformId.isEmpty()) {
				ErrorDTO emptyIdError = new ErrorDTO("platformId Id is empty.");
				return Response.status(Status.BAD_REQUEST).entity(emptyIdError).build();
			}
			if (service.platformConfigExists(machineId, platformId)) {
				ErrorDTO alreadyExistsError = new ErrorDTO(String.format("PlatformConfig with Id '%s' already exists.", platformId));
				return Response.status(Status.BAD_REQUEST).entity(alreadyExistsError).build();
			}

			PlatformConfig addPlatformConfig = ModelConverter.Domain.toRTO(addPlatformConfigDTO);
			succeed = service.addPlatformConfig(machineId, addPlatformConfig);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "PlatformConfig is added.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "PlatformConfig is not added.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

	/**
	 * Update a platform configuration.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms (Body parameter: PlatformConfigDTO)
	 * 
	 * @param machineId
	 * @param updatePlatformConfigDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePlatform(@PathParam("machineId") String machineId, PlatformConfigDTO updatePlatformConfigDTO) {
		if (updatePlatformConfigDTO == null) {
			ErrorDTO nullRequestDTOError = new ErrorDTO("updatePlatformConfigDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullRequestDTOError).build();
		}

		boolean succeed = false;
		DomainManagementService service = getService();
		try {
			PlatformConfig updatePlatformConfig = ModelConverter.Domain.toRTO(updatePlatformConfigDTO);
			List<String> fieldsToUpdate = updatePlatformConfigDTO.getFieldsToUpdate();
			succeed = service.updatePlatformConfig(machineId, updatePlatformConfig, fieldsToUpdate);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "PlatformConfig is updated successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "PlatformConfig is not updated.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

	/**
	 * Remove a platform configuration.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}
	 * 
	 * @param machineId
	 * @param platformId
	 * @return
	 */
	@DELETE
	@Path("/{platformId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removePlatformConfig(@PathParam("machineId") String machineId, @PathParam("platformId") String platformId) {
		if (platformId == null || platformId.isEmpty()) {
			ErrorDTO emptyIdError = new ErrorDTO("platformId is null.");
			return Response.status(Status.BAD_REQUEST).entity(emptyIdError).build();
		}

		boolean succeed = false;
		DomainManagementService service = getService();
		try {
			succeed = service.deletePlatformConfig(machineId, platformId);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "PlatformConfig is removed successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "PlatformConfig is not removed.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

}
