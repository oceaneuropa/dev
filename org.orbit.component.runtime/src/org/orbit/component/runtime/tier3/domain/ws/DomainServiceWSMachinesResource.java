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

import org.orbit.component.model.tier3.domain.DomainException;
import org.orbit.component.model.tier3.domain.MachineConfigRTO;
import org.orbit.component.model.tier3.domain.ModelConverter;
import org.orbit.component.model.tier3.domain.dto.MachineConfigDTO;
import org.orbit.component.runtime.tier3.domain.service.DomainService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/*
 * DomainService Machines resource.
 * 
 * {contextRoot} example:
 * /orbit/v1/domain
 * 
 * Machines
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines (Body parameter: MachineConfigDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines (Body parameter: MachineConfigDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
 * 
 * @see TransferAgentServiceResource
 * 
 */
@Path("/machines")
@Produces(MediaType.APPLICATION_JSON)
public class DomainServiceWSMachinesResource extends AbstractWSApplicationResource {

	@Inject
	public DomainService service;

	protected DomainService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("DomainService is not available.");
		}
		return this.service;
	}

	/**
	 * Get machine configurations.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMachines() {
		DomainService service = getService();

		List<MachineConfigDTO> machineConfigDTOs = new ArrayList<MachineConfigDTO>();
		try {
			List<MachineConfigRTO> machineConfigs = service.getMachineConfigs();
			if (machineConfigs != null) {
				for (MachineConfigRTO machineConfig : machineConfigs) {
					MachineConfigDTO machineConfigDTO = ModelConverter.getInstance().toDTO(machineConfig);
					machineConfigDTOs.add(machineConfigDTO);
				}
			}
		} catch (DomainException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(machineConfigDTOs).build();
	}

	/**
	 * Get a machine configuration.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
	 * 
	 * @param machineId
	 * @return
	 */
	@GET
	@Path("{machineId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMachine(@PathParam("machineId") String machineId) {
		MachineConfigDTO machineConfigDTO = null;

		DomainService service = getService();
		try {
			MachineConfigRTO machineConfig = service.getMachineConfig(machineId);
			if (machineConfig == null) {
				ErrorDTO notFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), String.format("Machine with id '%s' cannot be found.", machineId));
				return Response.status(Status.NOT_FOUND).entity(notFoundError).build();
			}
			machineConfigDTO = ModelConverter.getInstance().toDTO(machineConfig);

		} catch (DomainException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(machineConfigDTO).build();
	}

	/**
	 * Add a machine configuration.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines (Body parameter: MachineConfigDTO)
	 * 
	 * @param addMachineRequestDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addMachine(MachineConfigDTO addMachineRequestDTO) {
		if (addMachineRequestDTO == null) {
			ErrorDTO nullRequestDTOError = new ErrorDTO("addMachineRequestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullRequestDTOError).build();
		}

		boolean succeed = false;
		DomainService service = getService();
		try {
			String machineId = addMachineRequestDTO.getId();
			if (machineId == null || machineId.isEmpty()) {
				ErrorDTO emptyIdError = new ErrorDTO("machine Id is empty.");
				return Response.status(Status.BAD_REQUEST).entity(emptyIdError).build();
			}
			if (service.machineConfigExists(machineId)) {
				ErrorDTO alreadyExistsError = new ErrorDTO(String.format("Machine with Id '%s' already exists.", machineId));
				return Response.status(Status.BAD_REQUEST).entity(alreadyExistsError).build();
			}

			MachineConfigRTO addMachineRequest = ModelConverter.getInstance().toRTO(addMachineRequestDTO);
			succeed = service.addMachineConfig(addMachineRequest);

		} catch (DomainException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "Machine is added.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "Machine is not added.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

	/**
	 * Update a machine configuration.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines (Body parameter: MachineConfigDTO)
	 * 
	 * @param updateMachineRequestDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMachine(MachineConfigDTO updateMachineRequestDTO) {
		if (updateMachineRequestDTO == null) {
			ErrorDTO nullRequestDTOError = new ErrorDTO("updateMachineRequestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullRequestDTOError).build();
		}

		boolean succeed = false;
		DomainService service = getService();
		try {
			MachineConfigRTO updateMachineRequest = ModelConverter.getInstance().toRTO(updateMachineRequestDTO);
			List<String> fieldsToUpdate = updateMachineRequestDTO.getFieldsToUpdate();
			succeed = service.updateMachineConfig(updateMachineRequest, fieldsToUpdate);

		} catch (DomainException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "Machine is updated successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			// StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "Machine is not updated.");
			// return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.FAILED, "Machine is not updated.");
			return Response.ok().entity(statusDTO).build();
		}
	}

	/**
	 * Remove a machine configuration.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
	 * 
	 * @param machineId
	 * @return
	 */
	@DELETE
	@Path("/{machineId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeMachine(@PathParam("machineId") String machineId) {
		if (machineId == null || machineId.isEmpty()) {
			ErrorDTO emptyIdError = new ErrorDTO("machineId is null.");
			return Response.status(Status.BAD_REQUEST).entity(emptyIdError).build();
		}

		boolean succeed = false;
		DomainService service = getService();
		try {
			if (!service.machineConfigExists(machineId)) {
				StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.FAILED, "Machine does not exist.");
				return Response.ok().entity(statusDTO).build();
			}
			succeed = service.deleteMachineConfig(machineId);

		} catch (DomainException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "Machine is removed successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "Machine is not removed.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

}
