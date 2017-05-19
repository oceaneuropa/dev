package org.origin.mgm.ws;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import org.origin.common.util.DateUtil;
import org.origin.common.util.StringUtil;
import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.dto.DTOConverter;
import org.origin.mgm.model.dto.IndexItemDTO;
import org.origin.mgm.model.dto.IndexItemSetPropertiesRequestDTO;
import org.origin.mgm.model.dto.IndexItemSetPropertyRequestDTO;
import org.origin.mgm.model.runtime.IndexItem;
import org.origin.mgm.service.IndexService;

/*
 * IndexItem resource
 * 
 * {contextRoot} example:
 * /orbit/v1/indexservice/
 * 
 * IndexItem:
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}
 * 
 *     Not being used:
 *     URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/exists
 * 
 * IndexItem Properties:
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/properties
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/properties (Body parameter: IndexItemSetPropertiesRequestDTO)
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/property (Body parameter: IndexItemSetPropertyRequestDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/properties?propertynames={propertynames}
 * 
 */
@Path("/indexitems/{indexproviderid}/{indexitemid}")
@Produces(MediaType.APPLICATION_JSON)
public class IndexItemResource extends AbstractApplicationResource {

	protected boolean debug = true;

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
	 * Get an index item.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIndexItem(@PathParam("indexproviderid") String indexProviderId, @PathParam("indexitemid") Integer indexItemId) {
		if (indexItemId == null || indexItemId <= 0) {
			ErrorDTO nullIndexItemIdError = new ErrorDTO("indexItemId path parameter is invalid.");
			return Response.status(Status.BAD_REQUEST).entity(nullIndexItemIdError).build();
		}

		IndexItemDTO indexItemDTO = null;
		IndexService indexService = getService(IndexService.class);
		try {
			IndexItem indexItem = indexService.getIndexItem(indexProviderId, indexItemId);
			if (indexItem != null) {
				indexItemDTO = DTOConverter.getInstance().toDTO(indexItem);
			}

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (indexItemDTO == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok().entity(indexItemDTO).build();
	}

	/**
	 * Remove an index item.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeIndexItem(@PathParam("indexproviderid") String indexProviderId, @PathParam("indexitemid") Integer indexItemId) {
		if (indexItemId == null || indexItemId <= 0) {
			ErrorDTO nullIndexItemIdError = new ErrorDTO("indexItemId path parameter is invalid.");
			return Response.status(Status.BAD_REQUEST).entity(nullIndexItemIdError).build();
		}

		IndexService indexService = getService(IndexService.class);
		boolean succeed = false;
		try {
			// delete IndexItem by indexItemId
			succeed = indexService.removeIndexItem(indexProviderId, indexItemId);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, MessageFormat.format("IndexItem (indexItemId={0}) is removed successfully.", new Object[] { indexItemId }));
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, MessageFormat.format("IndexItem (indexItemId={0}) is not removed.", new Object[] { indexItemId }));
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

	// /**
	// * Check whether an index item exists.
	// *
	// * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/exists
	// *
	// * @param indexProviderId
	// * @param indexitemid
	// * @return
	// */
	// @Path("exists")
	// @GET
	// @Produces(MediaType.APPLICATION_JSON)
	// public Response exists(@PathParam("indexproviderid") String indexProviderId, @PathParam("indexitemid") Integer indexItemId) {
	// if (indexItemId == null || indexItemId <= 0) {
	// ErrorDTO nullIndexItemIdError = new ErrorDTO("indexItemId path parameter is invalid.");
	// return Response.status(Status.BAD_REQUEST).entity(nullIndexItemIdError).build();
	// }
	//
	// IndexService indexService = getService(IndexService.class);
	// try {
	// IndexItem indexItem = indexService.getIndexItem(indexProviderId, indexItemId);
	// Boolean exists = (indexItem != null) ? Boolean.TRUE : Boolean.FALSE;
	// return Response.ok().entity(exists).build();
	//
	// } catch (IndexServiceException e) {
	// ErrorDTO error = handleError(e, e.getCode(), true);
	// return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
	// }
	// }

	/**
	 * Get properties.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/properties
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @return
	 */
	@Path("properties")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProperties(@PathParam("indexproviderid") String indexProviderId, @PathParam("indexitemid") Integer indexItemId) {
		if (indexItemId == null || indexItemId <= 0) {
			ErrorDTO nullIndexItemIdError = new ErrorDTO("indexItemId path parameter is invalid.");
			return Response.status(Status.BAD_REQUEST).entity(nullIndexItemIdError).build();
		}

		Map<String, ?> properties = null;
		IndexService indexService = getService(IndexService.class);
		try {
			properties = indexService.getProperties(indexProviderId, indexItemId);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		if (properties == null) {
			properties = new Hashtable<String, Object>();
		}
		return Response.ok().entity(properties).build();
	}

	/**
	 * Set properties.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/properties (Body parameter:
	 * IndexItemSetPropertiesRequestDTO)
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param setPropertiesRequest
	 * @return
	 */
	@Path("properties")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setProperties(@PathParam("indexproviderid") String indexProviderId, @PathParam("indexitemid") Integer indexItemId, IndexItemSetPropertiesRequestDTO setPropertiesRequest) {
		if (indexItemId == null || indexItemId <= 0) {
			ErrorDTO nullIndexItemIdError = new ErrorDTO("indexItemId path parameter is invalid.");
			return Response.status(Status.BAD_REQUEST).entity(nullIndexItemIdError).build();
		}

		if (setPropertiesRequest == null) {
			ErrorDTO nullSetPropertiesRequest = new ErrorDTO("setPropertiesRequest is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullSetPropertiesRequest).build();
		}

		boolean succeed = false;
		IndexService indexService = getService(IndexService.class);
		try {
			// get the properties string from the query parameter
			// Map<String, ?> propertiesToSet = setPropertiesRequest.getProperties();
			String propertiesString = setPropertiesRequest.getPropertiesString();
			Map<String, Object> propertiesToSet = JSONUtil.toProperties(propertiesString);

			// merge the add properties to existing properties
			Map<String, Object> existingProperties = (Map<String, Object>) indexService.getProperties(indexProviderId, indexItemId);
			existingProperties.putAll(propertiesToSet);

			succeed = indexService.setProperties(indexProviderId, indexItemId, existingProperties);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, MessageFormat.format("IndexItem (indexItemId={0}) properties are set successfully.", new Object[] { indexItemId }));
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.FAILED, MessageFormat.format("IndexItem (indexItemId={0}) properties are not set.", new Object[] { indexItemId }));
			return Response.status(Status.OK).entity(statusDTO).build();
		}
	}

	/**
	 * Set property.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/property (Body parameter: IndexItemSetPropertyRequestDTO)
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param setPropertyRequest
	 * @return
	 */
	@Path("property")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setProperty(@PathParam("indexproviderid") String indexProviderId, @PathParam("indexitemid") Integer indexItemId, IndexItemSetPropertyRequestDTO setPropertyRequest) {
		if (indexItemId == null || indexItemId <= 0) {
			ErrorDTO nullIndexItemIdError = new ErrorDTO("indexItemId path parameter is invalid.");
			return Response.status(Status.BAD_REQUEST).entity(nullIndexItemIdError).build();
		}

		if (setPropertyRequest == null) {
			ErrorDTO nullSetPropertyRequest = new ErrorDTO("setPropertyRequest is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullSetPropertyRequest).build();
		}

		String propName = setPropertyRequest.getPropName();
		Object propValue = setPropertyRequest.getPropValue();
		// Optional data type of the property.
		// Supported date types are: string, boolean, integer, long, float, double, date.
		// If not specified, the default data type will be string.
		String propType = setPropertyRequest.getPropType();

		boolean succeed = false;

		boolean isString = false;
		boolean isBoolean = false;
		boolean isInteger = false;
		boolean isLong = false;
		boolean isFloat = false;
		boolean isDouble = false;
		boolean isDate = false;

		if ("string".equalsIgnoreCase(propType)) {
			isString = true;
		} else if ("boolean".equalsIgnoreCase(propType)) {
			isBoolean = true;
		} else if ("integer".equalsIgnoreCase(propType)) {
			isInteger = true;
		} else if ("long".equalsIgnoreCase(propType)) {
			isLong = true;
		} else if ("float".equalsIgnoreCase(propType)) {
			isFloat = true;
		} else if ("double".equalsIgnoreCase(propType)) {
			isDouble = true;
		} else if ("date".equalsIgnoreCase(propType)) {
			isDate = true;
		} else {
			isString = true;
		}

		IndexService indexService = getService(IndexService.class);
		try {
			// merge the add properties to existing properties
			Map<String, Object> properties = (Map<String, Object>) indexService.getProperties(indexProviderId, indexItemId);

			if (isString) {
				String stringValue = propValue instanceof String ? (String) propValue : propValue.toString();
				properties.put(propName, stringValue);

			} else if (isBoolean) {
				Boolean booleanValue = propValue instanceof Boolean ? (Boolean) propValue : Boolean.parseBoolean(propValue.toString());
				properties.put(propName, booleanValue);

			} else if (isInteger) {
				Integer integerValue = propValue instanceof Integer ? (Integer) propValue : Integer.parseInt(propValue.toString());
				properties.put(propName, integerValue);

			} else if (isLong) {
				Long longValue = propValue instanceof Long ? (Long) propValue : Long.parseLong(propValue.toString());
				properties.put(propName, longValue);

			} else if (isFloat) {
				Float floatValue = propValue instanceof Float ? (Float) propValue : Float.parseFloat(propValue.toString());
				properties.put(propName, floatValue);

			} else if (isDouble) {
				Double doubleValue = propValue instanceof Double ? (Double) propValue : Double.parseDouble(propValue.toString());
				properties.put(propName, doubleValue);

			} else if (isDate) {
				Date dateValue = propValue instanceof Date ? (Date) propValue : DateUtil.toDate(propValue.toString(), DateUtil.COMMON_DATE_FORMATS);
				properties.put(propName, dateValue);
			}

			succeed = indexService.setProperties(indexProviderId, indexItemId, properties);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, MessageFormat.format("IndexItem (indexItemId={0}) property is set successfully.", new Object[] { indexItemId }));
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, MessageFormat.format("IndexItem (indexItemId={0}) property is not set.", new Object[] { indexItemId }));
			return Response.status(Status.OK).entity(statusDTO).build();
		}
	}

	/**
	 * Remove properties.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/properties?propertynames={propertynames}
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param propertyNamesString
	 * @return
	 */
	@Path("properties")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeProperty(@PathParam("indexproviderid") String indexProviderId, @PathParam("indexitemid") Integer indexItemId, @QueryParam("propertynames") String propertyNamesString) {
		if (indexItemId == null || indexItemId <= 0) {
			ErrorDTO nullIndexItemIdError = new ErrorDTO("indexItemId path parameter is invalid.");
			return Response.status(Status.BAD_REQUEST).entity(nullIndexItemIdError).build();
		}

		if (propertyNamesString == null || propertyNamesString.isEmpty()) {
			ErrorDTO emptyPropertiesError = new ErrorDTO("properties names query parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(emptyPropertiesError).build();
		}

		boolean succeed = false;
		IndexService indexService = getService(IndexService.class);
		try {
			// List<?> propNames = JSONUtil.toList(propertyNamesString, true);
			// List<String> propNames = Arrays.asList(propertyNamesString);
			List<String> propNames = StringUtil.toList(propertyNamesString);

			indexService.removeProperty(indexProviderId, indexItemId, (List<String>) propNames);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, MessageFormat.format("IndexItem (indexItemId={0}) properties are removed successfully.", new Object[] { indexItemId }));
			return Response.ok().entity(statusDTO).build();

		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.FAILED, MessageFormat.format("IndexItem (indexItemId={0}) properties are not removed.", new Object[] { indexItemId }));
			// return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
			return Response.ok().entity(statusDTO).build();
		}
	}

}

// if (debug) {
// System.err.println(getClass().getSimpleName() + ".setProperties() indexItemId=" + indexItemId);
// System.err.println("\t indexItemid is " + indexItemId);
//
// if (setPropertiesRequest == null) {
// System.err.println("request is null");
//
// } else {
// Integer requestIndexItemId = setPropertiesRequest.getIndexItemId();
// // Map<String, Object> props = setPropertiesRequest.getProperties();
//
// String propertiesString = setPropertiesRequest.getPropertiesString();
// Map<String, Object> props = JSONUtil.toProperties(propertiesString);
//
// System.err.println("\t requestIndexItemId is " + requestIndexItemId);
// System.err.println("\t properties string is " + propertiesString);
// System.err.println("\t props:");
// for (Iterator<String> keyItor = props.keySet().iterator(); keyItor.hasNext();) {
// String propName = keyItor.next();
// Object propValue = props.get(propName);
// System.err.println("\t\t " + propName + "=" + propValue);
// }
// }
// }
