package org.origin.mgm.ws;

import java.text.MessageFormat;
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

import org.origin.common.json.JSONUtil;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;
import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.dto.DTOConverter;
import org.origin.mgm.service.IndexService;

/**
 * Index item properties resource
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexitemid}/properties
 *
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexitemid}/properties?properties={propertiesString}
 * 
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexitemid}/properties?properties={propertiesString}
 *
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexitemid}/properties?propertynames={propertyNamesString}
 *
 */
@Path("/indexitems/{indexitemid}/properties")
@Produces(MediaType.APPLICATION_JSON)
public class IndexItemPropertiesResource extends AbstractApplicationResource {

	/**
	 * Handle IndexServiceException and create ErrorDTO from it.
	 * 
	 * @param e
	 * @return
	 */
	protected ErrorDTO handleError(IndexServiceException e) {
		e.printStackTrace();
		this.logger.error(e.getMessage());
		return DTOConverter.getInstance().toDTO(e);
	}

	/**
	 * Get properties of an index item.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexitemid}/properties
	 * 
	 * @param indexItemId
	 * @param name
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProperties(@PathParam("indexitemid") Integer indexItemId) {
		Map<String, ?> properties = null;

		IndexService indexService = getService(IndexService.class);
		try {
			properties = indexService.getProperties(indexItemId);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(properties).build();
	}

	/**
	 * Set properties to an index item.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexitemid}/properties?properties={propertiesString}
	 * 
	 * @param indexItemId
	 * @param propertiesString
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProperty(@PathParam("indexitemid") Integer indexItemId, @QueryParam("properties") String propertiesString) {
		boolean succeed = false;
		IndexService indexService = getService(IndexService.class);
		try {
			Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);
			succeed = indexService.setProperty(indexItemId, properties);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = null;
		if (succeed) {
			statusDTO = new StatusDTO("200", "success", MessageFormat.format("IndexItem (indexItemId={0}) properties are added successfully.", new Object[] { indexItemId }));
		} else {
			statusDTO = new StatusDTO("200", "fail", MessageFormat.format("IndexItem (indexItemId={0}) properties are not added.", new Object[] { indexItemId }));
		}
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Update properties of an index item.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexitemid}/properties?properties={propertiesString}
	 * 
	 * Body parameter: properties
	 * 
	 * @param homeDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProperties(@PathParam("indexitemid") Integer indexItemId, @QueryParam("properties") String propertiesString) {
		boolean succeed = false;
		IndexService indexService = getService(IndexService.class);
		try {
			Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);
			succeed = indexService.setProperty(indexItemId, properties);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = null;
		if (succeed) {
			statusDTO = new StatusDTO("200", "success", MessageFormat.format("IndexItem (indexItemId={0}) properties are updated successfully.", new Object[] { indexItemId }));
		} else {
			statusDTO = new StatusDTO("200", "fail", MessageFormat.format("IndexItem (indexItemId={0}) properties are not updated.", new Object[] { indexItemId }));
		}
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Remove properties from an index item.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexitemid}/properties?propertynames={propertyNamesString}
	 * 
	 * @param indexitemid
	 * @param propertyNamesString
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeProperty(@PathParam("indexitemid") Integer indexItemId, @QueryParam("propertynames") String propertyNamesString) {
		boolean succeed = false;
		IndexService indexService = getService(IndexService.class);
		try {
			List<?> propNames = JSONUtil.toList(propertyNamesString, true);
			indexService.removeProperty(indexItemId, (List<String>) propNames);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = null;
		if (succeed) {
			statusDTO = new StatusDTO("200", "success", MessageFormat.format("IndexItem (indexItemId={0}) properties are removed successfully.", new Object[] { indexItemId }));
		} else {
			statusDTO = new StatusDTO("200", "fail", MessageFormat.format("IndexItem (indexItemId={0}) properties are not removed.", new Object[] { indexItemId }));
		}
		return Response.ok().entity(statusDTO).build();
	}

}
