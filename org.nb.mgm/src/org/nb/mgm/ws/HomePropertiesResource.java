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

import org.nb.mgm.exception.ManagementException;
import org.nb.mgm.model.runtime.Home;
import org.nb.mgm.service.ManagementService;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

/*
 * Home properties resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties?useJsonString=false
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties (Body parameter "properties": string)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties (Body parameter "properties": string)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties?propertyName={propertyName1}&propertyName={propertyName2}
 * 
 * @see http://stackoverflow.com/questions/13750010/jersey-client-how-to-add-a-list-as-query-parameter
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 * 
 */
@Path("/{machineId}/homes/{homeId}/properties")
@Produces(MediaType.APPLICATION_JSON)
public class HomePropertiesResource extends AbstractApplicationResource {

	/**
	 * Get Home properties.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties?useJsonString=false
	 * 
	 * @param machineId
	 * @param homeId
	 * @param useJsonString
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProperties(@PathParam("machineId") String machineId, @PathParam("homeId") String homeId, @QueryParam("useJsonString") boolean useJsonString) {
		if (homeId == null || homeId.isEmpty()) {
			ErrorDTO nullHomeIdError = new ErrorDTO("homeId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullHomeIdError).build();
		}

		Map<String, Object> properties = null;

		ManagementService mgm = getService(ManagementService.class);
		try {
			Home home = mgm.getHome(homeId);
			if (home == null) {
				ErrorDTO homeNotFoundError = new ErrorDTO("Home cannot be found.");
				return Response.status(Status.BAD_REQUEST).entity(homeNotFoundError).build();
			}

			properties = mgm.getHomeProperties(homeId);

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
	 * Set Home properties.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties (Body parameter "properties": string)
	 * 
	 * @param machineId
	 * @param homeId
	 * @param propertiesString
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setProperties(@PathParam("machineId") String machineId, @PathParam("homeId") String homeId, String propertiesString) {
		if (homeId == null || homeId.isEmpty()) {
			ErrorDTO nullHomeIdError = new ErrorDTO("homeId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullHomeIdError).build();
		}

		boolean succeed = false;

		ManagementService mgm = getService(ManagementService.class);
		try {
			Home home = mgm.getHome(homeId);
			if (home == null) {
				ErrorDTO homeNotFoundError = new ErrorDTO("Home cannot be found.");
				return Response.status(Status.BAD_REQUEST).entity(homeNotFoundError).build();
			}

			Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);
			succeed = mgm.setHomeProperties(homeId, properties);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = null;
		if (succeed) {
			statusDTO = new StatusDTO("200", "success", MessageFormat.format("Home (id={0}) properties are set successfully.", new Object[] { homeId }));
		} else {
			statusDTO = new StatusDTO("200", "fail", MessageFormat.format("Home (id={0}) properties are not set.", new Object[] { homeId }));
		}
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Set Home properties.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties (Body parameter "properties": string)
	 * 
	 * @param machineId
	 * @param homeId
	 * @param propertiesString
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putProperties(@PathParam("machineId") String machineId, @PathParam("homeId") String homeId, String propertiesString) {
		return setProperties(machineId, homeId, propertiesString);
	}

	/**
	 * Delete Home properties.
	 * 
	 * URL (DELETE):
	 * {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties?propertyName={propertyName1}&propertyName={propertyName2}
	 * 
	 * @param machineId
	 * @param homeId
	 * @param propertyNames
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeProperties(@PathParam("machineId") String machineId, @PathParam("homeId") String homeId, @QueryParam("propertyName") List<String> propertyNames) {
		if (homeId == null || homeId.isEmpty()) {
			ErrorDTO nullHomeIdError = new ErrorDTO("homeId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullHomeIdError).build();
		}
		if (propertyNames.isEmpty()) {
			ErrorDTO emptyPropertyNamesError = new ErrorDTO("Property names are empty.");
			return Response.status(Status.BAD_REQUEST).entity(emptyPropertyNamesError).build();
		}

		boolean succeed = false;

		ManagementService mgm = getService(ManagementService.class);
		try {
			Home home = mgm.getHome(homeId);
			if (home == null) {
				ErrorDTO homeNotFoundError = new ErrorDTO("Home cannot be found.");
				return Response.status(Status.BAD_REQUEST).entity(homeNotFoundError).build();
			}

			succeed = mgm.removeHomeProperties(homeId, propertyNames);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = null;
		if (succeed) {
			statusDTO = new StatusDTO("200", "success", MessageFormat.format("Home (id={0}) properties are removed successfully.", new Object[] { homeId }));
		} else {
			statusDTO = new StatusDTO("200", "fail", MessageFormat.format("Home (id={0}) properties are not removed.", new Object[] { homeId }));
		}
		return Response.ok().entity(statusDTO).build();
	}

}
