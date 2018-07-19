package org.orbit.infra.runtime.extensionregistry.ws;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.extensionregistry.ExtensionItemDTO;
import org.orbit.infra.model.extensionregistry.ExtensionItemSetPropertiesRequest;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionItem;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionRegistryService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;
import org.origin.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Extension Registry Server
 * 
 * contextRoot:  /orbit/v1/extensionregistry/
 * 
 * The service
 * URL (GET):    {scheme}://{host}:{port}/{contextRoot}/ping
 * URL (POST):   {scheme}://{host}:{port}/{contextRoot}/echo?message={message}
 * 
 * ExtensionItems
 * URL (GET):    {scheme}://{host}:{port}/{contextRoot}/extensionitems?platformId={platformId}&typeId={typeId}
 * URL (POST):   {scheme}://{host}:{port}/{contextRoot}/extensionitems (Body parameter: ExtensionItemCreateRequest)
 * URL (PUT):    {scheme}://{host}:{port}/{contextRoot}/extensionitems (Body parameter: ExtensionItemUpdateRequest)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/extensionitems?platformId={platformId}
 * 
 * ExtensionItem
 * URL (GET):    {scheme}://{host}:{port}/{contextRoot}/extensionitem?platformId={platformId}&typeId={typeId}&extensionId={extensionId}
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/extensionitem?platformId={platformId}&typeId={typeId}&extensionId={extensionId}
 * 
 * ExtensionItem Properties
 * URL (GET):    {scheme}://{host}:{port}/{contextRoot}/extensionitem/properties?platformId={platformId}&typeId={typeId}&extensionId={extensionId}
 * URL (POST):   {scheme}://{host}:{port}/{contextRoot}/extensionitem/properties (Body parameter: ExtensionItemSetPropertiesRequest)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/extensionitem/properties?platformId={platformId}&typeId={typeId}&extensionId={extensionId}&propertyNames={propertyNames}
 * 
 */
@Path("/extensionitem")
@Produces(MediaType.APPLICATION_JSON)
public class ExtensionItemWSResource extends AbstractWSApplicationResource {

	protected static Logger LOG = LoggerFactory.getLogger(ExtensionItemWSResource.class);

	@Inject
	public ExtensionRegistryService service;

	protected ExtensionRegistryService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("ExtensionRegistryService is not available.");
		}
		return this.service;
	}

	/**
	 * Get an extension item.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extensionitem/?platformId={platformId}&typeId={typeId}&extensionId={extensionId}
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExtensionItem(@QueryParam("platformId") String platformId, @QueryParam("typeId") String typeId, @QueryParam("extensionId") String extensionId) {
		if (platformId == null || platformId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("platformId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (typeId == null || typeId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("typeId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (extensionId == null || extensionId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("extensionId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ExtensionItemDTO DTO = null;
		try {
			ExtensionRegistryService service = getService();

			ExtensionItem item = service.getExtensionItem(platformId, typeId, extensionId);
			if (item != null) {
				DTO = ModelConverter.INSTANCE.toDTO(item);
			}

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (DTO == null) {
			// return Response.status(Status.NOT_FOUND).build();
			Response.ok().build();
		}
		return Response.ok().entity(DTO).build();
	}

	/**
	 * Remove an extension item.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/extensionitem?platformId={platformId}&typeId={typeId}&extensionId={extensionId}
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteExtensionItem(@QueryParam("platformId") String platformId, @QueryParam("typeId") String typeId, @QueryParam("extensionId") String extensionId) {
		if (platformId == null || platformId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("platformId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (typeId == null || typeId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("typeId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (extensionId == null || extensionId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("extensionId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean succeed = false;
		try {
			ExtensionRegistryService service = getService();

			succeed = service.removeExtensionItem(platformId, typeId, extensionId);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "ExtensionItem is deleted successfully.");
			return Response.ok().entity(statusDTO).build();

		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.FAILED, "ExtensionItem is not deleted.");
			return Response.ok().entity(statusDTO).build();
		}
	}

	/**
	 * Get extension item's properties.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extensionitem/properties?platformId={platformId}&typeId={typeId}&extensionId={extensionId}
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @return
	 */
	@Path("properties")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProperties(@QueryParam("platformId") String platformId, @QueryParam("typeId") String typeId, @QueryParam("extensionId") String extensionId) {
		if (platformId == null || platformId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("platformId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (typeId == null || typeId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("typeId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (extensionId == null || extensionId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("extensionId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		Map<String, Object> properties = null;
		try {
			ExtensionRegistryService service = getService();

			properties = service.getProperties(platformId, typeId, extensionId);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		if (properties == null) {
			properties = new Hashtable<String, Object>();
		}
		return Response.ok().entity(properties).build();
	}

	/**
	 * Set extension item's properties.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/extensionitem/properties (Body parameter: ExtensionItemSetPropertiesRequest)
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param request
	 * @return
	 */
	@Path("properties")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setProperties(ExtensionItemSetPropertiesRequest request) {
		if (request == null) {
			ErrorDTO error = new ErrorDTO("ExtensionItemSetPropertiesRequest is null.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean succeed = false;
		try {
			ExtensionRegistryService service = getService();

			String platformId = request.getPlatformId();
			String typeId = request.getTypeId();
			String extensionId = request.getExtensionId();
			Map<String, Object> properties = request.getProperties();

			if (platformId == null || platformId.isEmpty()) {
				ErrorDTO error = new ErrorDTO("platformId is empty.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}
			if (typeId == null || typeId.isEmpty()) {
				ErrorDTO error = new ErrorDTO("typeId is empty.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}
			if (extensionId == null || extensionId.isEmpty()) {
				ErrorDTO error = new ErrorDTO("extensionId is empty.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

			ExtensionItem item = service.getExtensionItem(platformId, typeId, extensionId);
			if (item == null) {
				return Response.status(Status.NOT_FOUND).build();
			}

			succeed = service.setProperties(platformId, typeId, extensionId, properties);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "ExtensionItem properties are updated successfully.");
			return Response.ok().entity(statusDTO).build();

		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.FAILED, "ExtensionItem properties are not updated.");
			return Response.ok().entity(statusDTO).build();
		}
	}

	/**
	 * Delete extension item's properties.
	 * 
	 * URL (DELETE):
	 * {scheme}://{host}:{port}/{contextRoot}/extensionitem/properties?platformId={platformId}&typeId={typeId}&extensionId={extensionId}&propertyNames={
	 * propertynames}
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @param propertyNamesString
	 * @return
	 */
	@Path("properties")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteProperties(@QueryParam("platformId") String platformId, @QueryParam("typeId") String typeId, @QueryParam("extensionId") String extensionId, @QueryParam("propertynames") String propertyNamesString) {
		if (platformId == null || platformId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("platformId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (typeId == null || typeId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("typeId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (extensionId == null || extensionId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("extensionId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (propertyNamesString == null || propertyNamesString.isEmpty()) {
			ErrorDTO emptyPropertiesError = new ErrorDTO("properties names query parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(emptyPropertiesError).build();
		}

		boolean succeed = false;
		try {
			ExtensionRegistryService service = getService();
			List<String> propNames = StringUtil.toList(propertyNamesString);
			succeed = service.removeProperties(platformId, typeId, extensionId, propNames);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "ExtensionItem properties are deleted successfully.");
			return Response.ok().entity(statusDTO).build();

		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.FAILED, "ExtensionItem properties are delete updated.");
			return Response.ok().entity(statusDTO).build();
		}
	}

}
