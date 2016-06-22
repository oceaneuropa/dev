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

import org.nb.mgm.exception.MgmException;
import org.nb.mgm.model.dto.DTOConverter;
import org.nb.mgm.model.dto.HomeDTO;
import org.nb.mgm.model.dto.MachineDTO;
import org.nb.mgm.model.query.MachineQuery;
import org.nb.mgm.model.query.MachineQuery.MachineQueryBuilder;
import org.nb.mgm.model.runtime.Home;
import org.nb.mgm.model.runtime.Machine;
import org.nb.mgm.service.ManagementService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;
import org.origin.common.util.Util;

/*
 * Machine resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines?name={name}&ipaddress={ipaddress}&filter={filter} 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/machines (Body parameter: MachineDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines (Body parameter: MachineDTO)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
 * 
 */
@Path("/machines")
@Produces(MediaType.APPLICATION_JSON)
public class MachineResource extends AbstractApplicationResource {

	protected void handleSave(ManagementService mgm) {
		if (!mgm.isAutoSave()) {
			mgm.save();
		}
	}

	/**
	 * Get Machines by query parameters.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines?name={name}&ipaddress={ipaddress}&filter={filter}
	 * 
	 * e.g. http://127.0.0.1:9090/mgm/machines
	 * 
	 * @param name
	 * @param ipaddress
	 * @param filter
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMachines(@QueryParam("name") String name, @QueryParam("ipaddress") String ipaddress, @QueryParam("filter") String filter) {
		List<MachineDTO> machineDTOs = new ArrayList<MachineDTO>();

		ManagementService mgm = getService(ManagementService.class);
		try {
			// Get Machines matched by query.
			List<Machine> machines = null;
			if (name != null || ipaddress != null || filter != null) {
				MachineQueryBuilder builder = MachineQuery.newBuilder();
				if (name != null) {
					builder.withName(name);
				}
				if (ipaddress != null) {
					builder.withIpAddress(ipaddress);
				}
				if (filter != null) {
					builder.withFilter(filter);
				}
				MachineQuery machineQuery = builder.build();
				machines = mgm.getMachines(machineQuery);
			} else {
				machines = mgm.getMachines();
			}

			for (Machine machine : machines) {
				MachineDTO machineDTO = DTOConverter.getInstance().toDTO(machine);

				List<HomeDTO> homeDTOs = new ArrayList<HomeDTO>();
				for (Home home : mgm.getHomes(machine.getId())) {
					HomeDTO homeDTO = DTOConverter.getInstance().toDTO(home);
					// homeDTO.setMachine(machineDTO);
					homeDTOs.add(homeDTO);
				}
				machineDTO.setHomes(homeDTOs);

				machineDTOs.add(machineDTO);
			}

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(machineDTOs).build();
	}

	/**
	 * Get Machine by machine Id.
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
		MachineDTO machineDTO = null;

		ManagementService mgm = getService(ManagementService.class);
		try {
			Machine machine = mgm.getMachine(machineId);
			if (machine == null) {
				ErrorDTO machineNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Machine cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(machineNotFoundError).build();
			}
			machineDTO = DTOConverter.getInstance().toDTO(machine);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(machineDTO).build();
	}

	/**
	 * Add a Machine to the cluster.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/machines
	 * 
	 * Body parameter: MachineDTO
	 * 
	 * @param machineDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createMachine(MachineDTO machineDTO) {
		if (machineDTO == null) {
			ErrorDTO nullMachineDTOError = new ErrorDTO("machineDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullMachineDTOError).build();
		}

		ManagementService mgm = getService(ManagementService.class);

		String id = machineDTO.getId();
		String name = machineDTO.getName();
		String description = machineDTO.getDescription();
		String ipAddress = machineDTO.getIpAddress();

		Machine newMachine = new Machine(mgm.getRoot());
		newMachine.setId(id);
		newMachine.setName(name);
		newMachine.setDescription(description);
		newMachine.setIpAddress(ipAddress);

		try {
			mgm.addMachine(newMachine);

			if (Util.compare(id, newMachine.getId()) != 0) {
				machineDTO.setId(newMachine.getId());
			}
			if (Util.compare(name, newMachine.getName()) != 0) {
				machineDTO.setName(newMachine.getName());
			}
		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		handleSave(mgm);

		return Response.ok().entity(machineDTO).build();
	}

	/**
	 * Update Machine information.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines
	 * 
	 * Body parameter: MachineDTO
	 * 
	 * @param machineDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMachine(MachineDTO machineDTO) {
		if (machineDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("machineDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			String id = machineDTO.getId();
			String name = machineDTO.getName();
			String description = machineDTO.getDescription();
			String ipAddress = machineDTO.getIpAddress();

			Machine machine = new Machine();
			machine.setId(id);
			machine.setName(name);
			machine.setDescription(description);
			machine.setIpAddress(ipAddress);

			mgm.updateMachine(machine);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		handleSave(mgm);

		StatusDTO statusDTO = new StatusDTO("200", "success", "Machine is updated successfully.");
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Delete a Machine from the cluster.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
	 * 
	 * @param machineId
	 * @return
	 */
	@DELETE
	@Path("/{machineId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteMachine(@PathParam(value = "machineId") String machineId) {
		if (machineId == null || machineId.isEmpty()) {
			ErrorDTO nullMachineIdError = new ErrorDTO("machineId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullMachineIdError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			mgm.deleteMachine(machineId);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		handleSave(mgm);

		StatusDTO statusDTO = new StatusDTO("200", "success", "Machine is deleted successfully.");
		return Response.ok().entity(statusDTO).build();
	}

}
