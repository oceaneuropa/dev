package org.orbit.component.server.tier3.domain.ws;

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

import org.orbit.component.model.tier3.domain.DomainMgmtException;
import org.orbit.component.model.tier3.domain.ModelConverter;
import org.orbit.component.model.tier3.domain.TransferAgentConfigRTO;
import org.orbit.component.model.tier3.domain.dto.TransferAgentConfigDTO;
import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/*
 * DomainManagement TransferAgents resource.
 * 
 * {contextRoot} example:
 * /orbit/v1/domain
 * 
 * TransferAgents
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents (Body parameter: TransferAgentConfigDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents (Body parameter: TransferAgentConfigDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}
 * 
 */
@Path("/machines/{machineId}/transferagents")
@Produces(MediaType.APPLICATION_JSON)
public class DomainMgmtWSTransferAgentsResource extends AbstractWSApplicationResource {

	@Inject
	public DomainManagementService service;

	protected DomainManagementService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("DomainManagementService is not available.");
		}
		return this.service;
	}

	/**
	 * Get transfer agent configurations.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents
	 * 
	 * @param machineId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTransferAgents(@PathParam("machineId") String machineId) {
		// DomainManagementService service = getService(DomainManagementService.class);
		DomainManagementService service = getService();

		List<TransferAgentConfigDTO> transferAgentConfigDTOs = new ArrayList<TransferAgentConfigDTO>();
		try {
			List<TransferAgentConfigRTO> transferAgentConfigs = service.getTransferAgentConfigs(machineId);
			if (transferAgentConfigs != null) {
				for (TransferAgentConfigRTO transferAgentConfig : transferAgentConfigs) {
					TransferAgentConfigDTO transferAgentConfigDTO = ModelConverter.getInstance().toDTO(transferAgentConfig);
					transferAgentConfigDTOs.add(transferAgentConfigDTO);
				}
			}
		} catch (DomainMgmtException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(transferAgentConfigDTOs).build();
	}

	/**
	 * Get a transfer agent configuration.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}
	 * 
	 * @param machineId
	 * @return
	 */
	@GET
	@Path("{transferAgentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTransferAgent(@PathParam("machineId") String machineId, @PathParam("transferAgentId") String transferAgentId) {
		TransferAgentConfigDTO transferAgentConfigDTO = null;

		// DomainManagementService service = getService(DomainManagementService.class);
		DomainManagementService service = getService();
		try {
			TransferAgentConfigRTO transferAgentConfig = service.getTransferAgentConfig(machineId, transferAgentId);
			if (transferAgentConfig == null) {
				ErrorDTO notFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), String.format("TransferAgent with id '%s' cannot be found.", transferAgentId));
				return Response.status(Status.NOT_FOUND).entity(notFoundError).build();
			}
			transferAgentConfigDTO = ModelConverter.getInstance().toDTO(transferAgentConfig);

		} catch (DomainMgmtException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(transferAgentConfigDTO).build();
	}

	/**
	 * Add a transfer agent configuration.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents (Body parameter: TransferAgentConfigDTO)
	 *
	 * @param machineId
	 * @param addTransferAgentRequestDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addTransferAgent(@PathParam("machineId") String machineId, TransferAgentConfigDTO addTransferAgentRequestDTO) {
		if (addTransferAgentRequestDTO == null) {
			ErrorDTO nullRequestDTOError = new ErrorDTO("addTransferAgentRequestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullRequestDTOError).build();
		}

		boolean succeed = false;
		// DomainManagementService service = getService(DomainManagementService.class);
		DomainManagementService service = getService();
		try {
			String transferAgentId = addTransferAgentRequestDTO.getId();
			if (transferAgentId == null || transferAgentId.isEmpty()) {
				ErrorDTO emptyIdError = new ErrorDTO("transferAgentId Id is empty.");
				return Response.status(Status.BAD_REQUEST).entity(emptyIdError).build();
			}
			if (service.transferAgentConfigExists(machineId, transferAgentId)) {
				ErrorDTO alreadyExistsError = new ErrorDTO(String.format("TransferAgent with Id '%s' already exists.", transferAgentId));
				return Response.status(Status.BAD_REQUEST).entity(alreadyExistsError).build();
			}

			TransferAgentConfigRTO addTransferAgentRequest = ModelConverter.getInstance().toRTO(addTransferAgentRequestDTO);
			succeed = service.addTransferAgentConfig(machineId, addTransferAgentRequest);

		} catch (DomainMgmtException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "TransferAgent is added.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "TransferAgent is not added.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

	/**
	 * Update a transfer agent configuration.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents (Body parameter: TransferAgentConfigDTO)
	 * 
	 * @param machineId
	 * @param updateTransferAgentRequestDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTransferAgent(@PathParam("machineId") String machineId, TransferAgentConfigDTO updateTransferAgentRequestDTO) {
		if (updateTransferAgentRequestDTO == null) {
			ErrorDTO nullRequestDTOError = new ErrorDTO("updateTransferAgentRequestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullRequestDTOError).build();
		}

		boolean succeed = false;
		// DomainManagementService service = getService(DomainManagementService.class);
		DomainManagementService service = getService();
		try {
			TransferAgentConfigRTO updateTransferAgentRequest = ModelConverter.getInstance().toRTO(updateTransferAgentRequestDTO);
			List<String> fieldsToUpdate = updateTransferAgentRequestDTO.getFieldsToUpdate();
			succeed = service.updateTransferAgentConfig(machineId, updateTransferAgentRequest, fieldsToUpdate);

		} catch (DomainMgmtException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "TransferAgent is updated successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "TransferAgent is not updated.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

	/**
	 * Remove a transfer agent configuration.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @return
	 */
	@DELETE
	@Path("/{transferAgentId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeTransferAgent(@PathParam("machineId") String machineId, @PathParam("transferAgentId") String transferAgentId) {
		if (transferAgentId == null || transferAgentId.isEmpty()) {
			ErrorDTO emptyIdError = new ErrorDTO("transferAgentId is null.");
			return Response.status(Status.BAD_REQUEST).entity(emptyIdError).build();
		}

		boolean succeed = false;
		// DomainManagementService service = getService(DomainManagementService.class);
		DomainManagementService service = getService();
		try {
			succeed = service.deleteTransferAgentConfig(machineId, transferAgentId);

		} catch (DomainMgmtException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "TransferAgent is removed successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "TransferAgent is not removed.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

}
