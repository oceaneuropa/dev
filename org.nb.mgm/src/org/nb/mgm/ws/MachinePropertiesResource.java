package org.nb.mgm.ws;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.nb.mgm.model.exception.ManagementException;
import org.nb.mgm.model.runtime.Machine;
import org.nb.mgm.service.ManagementService;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/*
 * Machine properties resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/properties?useJsonString=false
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{machineId}/properties (Body parameter "properties": string)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{machineId}/properties (Body parameter "properties": string)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{machineId}/properties?propertyName={propertyName1}&propertyName={propertyName2}
 *
 */
@Path("/{machineId}/properties")
@Produces(MediaType.APPLICATION_JSON)
public class MachinePropertiesResource extends AbstractWSApplicationResource {

	/**
	 * Get Machine properties.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/properties?useJsonString=false
	 * 
	 * @param machineId
	 * @param useJsonString
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProperties(@PathParam("machineId") String machineId, @QueryParam("useJsonString") boolean useJsonString) {
		if (machineId == null || machineId.isEmpty()) {
			ErrorDTO nullMachineIdError = new ErrorDTO("machineId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullMachineIdError).build();
		}

		Map<String, Object> properties = null;

		ManagementService mgm = getService(ManagementService.class);
		try {
			Machine machine = mgm.getMachine(machineId);
			if (machine == null) {
				ErrorDTO machineNotFoundError = new ErrorDTO("Machine cannot be found.");
				return Response.status(Status.BAD_REQUEST).entity(machineNotFoundError).build();
			}
			properties = mgm.getMachineProperties(machineId);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (properties == null) {
			properties = new HashMap<String, Object>();
		}

		if (useJsonString) {
			String propertiesString = JSONUtil.toJsonString(properties);
			return Response.ok().entity(propertiesString).build();
		} else {
			return Response.ok().entity(properties).build();
		}
	}

	/**
	 * Set Machine properties.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{machineId}/properties (Body parameter "properties": string)
	 * 
	 * @param machineId
	 * @param propertiesString
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setProperties(@PathParam("machineId") String machineId, String propertiesString) {
		if (machineId == null || machineId.isEmpty()) {
			ErrorDTO nullMachineIdError = new ErrorDTO("machineId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullMachineIdError).build();
		}

		boolean succeed = false;

		ManagementService mgm = getService(ManagementService.class);
		try {
			Machine machine = mgm.getMachine(machineId);
			if (machine == null) {
				ErrorDTO machineNotFoundError = new ErrorDTO("Machine cannot be found.");
				return Response.status(Status.BAD_REQUEST).entity(machineNotFoundError).build();
			}

			Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);
			succeed = mgm.setMachineProperties(machineId, properties);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, MessageFormat.format("Machine (id={0}) properties are set successfully.", new Object[] { machineId }));
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, MessageFormat.format("Machine (id={0}) properties are not set.", new Object[] { machineId }));
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

	/**
	 * Set Machine properties.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{machineId}/properties (Body parameter "properties": string)
	 * 
	 * @param machineId
	 * @param propertiesString
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putProperties(@PathParam("machineId") String machineId, String propertiesString) {
		return setProperties(machineId, propertiesString);
	}

	/**
	 * Delete Machine properties.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{machineId}/properties?propertyName={propertyName1}&propertyName={propertyName2}
	 * 
	 * @param machineId
	 * @param propertyNames
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeProperties(@PathParam("machineId") String machineId, @QueryParam("propertyName") List<String> propertyNames) {
		if (machineId == null || machineId.isEmpty()) {
			ErrorDTO nullMachineIdError = new ErrorDTO("machineId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullMachineIdError).build();
		}
		if (propertyNames.isEmpty()) {
			ErrorDTO emptyPropertyNamesError = new ErrorDTO("Property names are empty.");
			return Response.status(Status.BAD_REQUEST).entity(emptyPropertyNamesError).build();
		}

		boolean succeed = false;

		ManagementService mgm = getService(ManagementService.class);
		try {
			Machine machine = mgm.getMachine(machineId);
			if (machine == null) {
				ErrorDTO machineNotFoundError = new ErrorDTO("Machine cannot be found.");
				return Response.status(Status.BAD_REQUEST).entity(machineNotFoundError).build();
			}

			succeed = mgm.removeMachineProperties(machineId, propertyNames);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, MessageFormat.format("Machine (id={0}) properties are removed successfully.", new Object[] { machineId }));
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, MessageFormat.format("Machine (id={0}) properties are not removed.", new Object[] { machineId }));
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

}
