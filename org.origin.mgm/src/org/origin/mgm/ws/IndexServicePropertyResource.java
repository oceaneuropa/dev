package org.origin.mgm.ws;

import java.text.MessageFormat;
import java.util.Iterator;
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

import org.origin.common.rest.dto.ErrorDTO;
import org.origin.common.rest.dto.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;
import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.dto.DTOConverter;
import org.origin.mgm.model.runtime.IndexItem;
import org.origin.mgm.service.IndexService;

/**
 * Service registry properties resource
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/serviceregistry/{namespace}/{name}/properties
 *
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/serviceregistry/{namespace}/{name}/properties
 *
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/serviceregistry/{namespace}/{name}/properties
 *
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/serviceregistry/{namespace}/{name}/properties
 *
 */
@Path("/serviceregistry/{namespace}/{name}/properties")
@Produces(MediaType.APPLICATION_JSON)
public class IndexServicePropertyResource extends AbstractApplicationResource {

	/**
	 * Handle MgmException and create ErrorDTO from it.
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
	 * Get properties of a service.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/serviceregistry/{namespace}/{name}/properties
	 * 
	 * @param namespace
	 * @param name
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProperties(@PathParam("namespace") String namespace, @PathParam("name") String name) {
		Map<String, Object> properties = null;

		IndexService serviceRegistry = getService(IndexService.class);
		try {
			properties = serviceRegistry.getProperties(namespace, name);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(properties).build();
	}

	/**
	 * Set a property to a service.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/serviceregistry/{namespace}/{name}/properties?propName={propName}&propValue={propValue}
	 * 
	 * @param namespace
	 * @param name
	 * @param propName
	 * @param propValue
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProperty( //
			@PathParam("namespace") String namespace, //
			@PathParam("name") String name, //
			@QueryParam("propName") String propName, //
			@QueryParam("propValue") Object propValue) {
		IndexService indexService = getService(IndexService.class);
		try {
			indexService.setProperty(namespace, name, propName, propValue);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO("200", "success", MessageFormat.format("Service ''{0}'' property ''{1}'' is set to ''{2}'' successfully.", new Object[] { IndexItem.getFullName(namespace, name), propName, propValue }));
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Update properties of a service.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/serviceregistry/{namespace}/{name}/properties
	 * 
	 * Body parameter: properties
	 * 
	 * @param homeDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProperties(@PathParam("namespace") String namespace, @PathParam("name") String name, Map<String, Object> properties) {
		if (properties == null || properties.isEmpty()) {
			ErrorDTO nullDTOError = new ErrorDTO("properties is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		IndexService indexService = getService(IndexService.class);
		try {
			for (Iterator<String> propNameItor = properties.keySet().iterator(); propNameItor.hasNext();) {
				String propName = propNameItor.next();
				Object propValue = properties.get(propName);
				indexService.setProperty(namespace, namespace, propName, propValue);
			}
		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO("200", "success", MessageFormat.format("Service ''{0}'' properties are updated successfully.", new Object[] { IndexItem.getFullName(namespace, name) }));
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Remove a property from a service.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/serviceregistry/{namespace}/{name}/properties?propName={propName}
	 * 
	 * @param namespace
	 * @param name
	 * @param propName
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeProperty(@PathParam("namespace") String namespace, @PathParam("name") String name, @QueryParam("propName") String propName) {
		if (propName == null || propName.isEmpty()) {
			ErrorDTO nullPropNameError = new ErrorDTO("propName is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullPropNameError).build();
		}

		IndexService indexService = getService(IndexService.class);
		try {
			if (!indexService.hasProperty(namespace, name, propName)) {
				StatusDTO statusDTO = new StatusDTO("201", "servicePropertyNotFound", MessageFormat.format("Service ''{0}'''s property ''{1}'' is not found.", new Object[] { IndexItem.getFullName(namespace, name), propName }));
				return Response.ok().entity(statusDTO).build();
			}

			indexService.removeProperty(namespace, name, propName);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO("200", "success", MessageFormat.format("Service ''{0}'' is unregistered successfully.", new Object[] { IndexItem.getFullName(namespace, name) }));
		return Response.ok().entity(statusDTO).build();
	}

}
