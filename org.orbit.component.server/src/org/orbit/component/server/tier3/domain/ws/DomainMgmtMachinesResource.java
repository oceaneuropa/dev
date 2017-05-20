package org.orbit.component.server.tier3.domain.ws;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier3.domain.DomainMgmtException;
import org.orbit.component.model.tier3.domain.MachineConfigDTO;
import org.orbit.component.model.tier3.domain.MachineConfigRTO;
import org.orbit.component.model.tier3.domain.ModelConverter;
import org.orbit.component.server.tier3.domain.service.DomainMgmtService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

/*
 * DomainManagement Machines resource.
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
 */
@Path("/machines")
@Produces(MediaType.APPLICATION_JSON)
public class DomainMgmtMachinesResource extends AbstractApplicationResource {

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
		DomainMgmtService domainMgmtService = getService(DomainMgmtService.class);

		List<MachineConfigDTO> machinesDTOs = new ArrayList<MachineConfigDTO>();
		try {
			List<MachineConfigRTO> machines = domainMgmtService.getMachineConfigs();
			if (machines != null) {
				for (MachineConfigRTO machine : machines) {
					MachineConfigDTO machineDTO = ModelConverter.getInstance().toDTO(machine);
					machinesDTOs.add(machineDTO);
				}
			}
		} catch (DomainMgmtException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(machinesDTOs).build();
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
		MachineConfigDTO machineDTO = null;

		DomainMgmtService domainMgmtService = getService(DomainMgmtService.class);
		try {
			MachineConfigRTO machine = domainMgmtService.getMachineConfig(machineId);
			if (machine == null) {
				ErrorDTO notFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), String.format("Machine with id '%s' cannot be found.", machineId));
				return Response.status(Status.NOT_FOUND).entity(notFoundError).build();
			}
			machineDTO = ModelConverter.getInstance().toDTO(machine);

		} catch (DomainMgmtException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(machineDTO).build();
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
		DomainMgmtService domainMgmtService = getService(DomainMgmtService.class);
		try {
			String machineId = addMachineRequestDTO.getId();
			if (machineId == null || machineId.isEmpty()) {
				ErrorDTO emptyIdError = new ErrorDTO("machine Id is empty.");
				return Response.status(Status.BAD_REQUEST).entity(emptyIdError).build();
			}
			if (domainMgmtService.machineConfigExists(machineId)) {
				ErrorDTO alreadyExistsError = new ErrorDTO(String.format("Machine with Id '%s' already exists.", machineId));
				return Response.status(Status.BAD_REQUEST).entity(alreadyExistsError).build();
			}

			MachineConfigRTO addMachineRequest = ModelConverter.getInstance().toRTO(addMachineRequestDTO);
			succeed = domainMgmtService.addMachineConfig(addMachineRequest);

		} catch (DomainMgmtException e) {
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
		DomainMgmtService domainMgmtService = getService(DomainMgmtService.class);
		try {
			MachineConfigRTO updateMachineRequest = ModelConverter.getInstance().toRTO(updateMachineRequestDTO);
			succeed = domainMgmtService.updateMachineConfig(updateMachineRequest);

		} catch (DomainMgmtException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "Machine is updated successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "Machine is not updated.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
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
		DomainMgmtService domainMgmtService = getService(DomainMgmtService.class);
		try {
			succeed = domainMgmtService.deleteMachineConfig(machineId);

		} catch (DomainMgmtException e) {
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
