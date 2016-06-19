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

import org.nb.mgm.exception.MgmException;
import org.nb.mgm.model.runtime.Home;
import org.nb.mgm.service.ManagementService;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

/**
 * Get home properties.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties
 * 
 * Set or update a home property.
 * 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties?properties={propertiesString}
 * 
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties?properties={propertiesString}
 * 
 * Update a home property.
 *
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties?propertynames={propertyNamesString}
 * 
 * 
 * @see http://stackoverflow.com/questions/13750010/jersey-client-how-to-add-a-list-as-query-parameter
 */
@Path("/{machineId}/homes/{homeId}/properties")
@Produces(MediaType.APPLICATION_JSON)
public class HomePropertiesResource extends AbstractApplicationResource {

	protected void handleSave(ManagementService mgm) {
		if (!mgm.isAutoSave()) {
			mgm.save();
		}
	}

	/**
	 * Get properties of a home.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties
	 * 
	 * @param machineId
	 * @param homeId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProperties(@PathParam("machineId") String machineId, @PathParam("homeId") String homeId) {
		if (homeId == null || homeId.isEmpty()) {
			ErrorDTO nullHomeIdError = new ErrorDTO("homeId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullHomeIdError).build();
		}

		Map<String, Object> properties = null;

		ManagementService mgm = getService(ManagementService.class);
		try {
			Home home = mgm.getHome(homeId);
			if (home == null) {
				ErrorDTO nullHomeIdError = new ErrorDTO("home cannot be found.");
				return Response.status(Status.BAD_REQUEST).entity(nullHomeIdError).build();
			}
			properties = home.getProperties();

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return Response.ok().entity(properties).build();
	}

	/**
	 * Set Home properties.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties?properties={propertiesString}
	 * 
	 * @param machineId
	 * @param homeId
	 * @param propertiesString
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setProperties(@PathParam("machineId") String machineId, @PathParam("homeId") String homeId, @QueryParam("properties") String propertiesString) {
		if (homeId == null || homeId.isEmpty()) {
			ErrorDTO nullHomeIdError = new ErrorDTO("homeId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullHomeIdError).build();
		}

		boolean succeed = false;

		ManagementService mgm = getService(ManagementService.class);
		try {
			Home home = mgm.getHome(homeId);
			if (home == null) {
				ErrorDTO nullHomeIdError = new ErrorDTO("home cannot be found.");
				return Response.status(Status.BAD_REQUEST).entity(nullHomeIdError).build();
			}

			Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

			succeed = mgm.setHomeProperties(homeId, properties);

		} catch (MgmException e) {
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
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties?properties={propertiesString}
	 * 
	 * @param indexItemId
	 * @param propertiesString
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putProperties(@PathParam("machineId") String machineId, @PathParam("homeId") String homeId, @QueryParam("properties") String propertiesString) {
		return setProperties(machineId, homeId, propertiesString);
	}

	/**
	 * Delete Home properties.
	 * 
	 * URL (DELETE):
	 * {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties?propertyName={propertyName1}&propertyName={propertyName2}&
	 * propertyName={propertyName3}&...
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

		boolean succeed = false;

		ManagementService mgm = getService(ManagementService.class);
		try {
			Home home = mgm.getHome(homeId);
			if (home == null) {
				ErrorDTO nullHomeIdError = new ErrorDTO("home cannot be found.");
				return Response.status(Status.BAD_REQUEST).entity(nullHomeIdError).build();
			}

			succeed = mgm.removeHomeProperties(homeId, propertyNames);

		} catch (MgmException e) {
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
