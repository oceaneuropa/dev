package org.orbit.infra.runtime.extensionregistry.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.extensionregistry.ExtensionItemCreateRequest;
import org.orbit.infra.model.extensionregistry.ExtensionItemDTO;
import org.orbit.infra.model.extensionregistry.ExtensionItemUpdateRequest;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionItem;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionRegistryService;
import org.orbit.infra.runtime.util.ModelConverter;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;
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
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.SYSTEM_ADMIN, OrbitRoles.EXTENSIONS_ADMIN })
@Path("/extensionitems")
@Produces(MediaType.APPLICATION_JSON)
public class ExtensionItemsWSResource extends AbstractWSApplicationResource {

	protected static Logger LOG = LoggerFactory.getLogger(ExtensionItemsWSResource.class);

	@Inject
	public ExtensionRegistryService service;

	protected ExtensionRegistryService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("ExtensionRegistryService is not available.");
		}
		return this.service;
	}

	/**
	 * Get extension items.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extensionitems?platformId={platformId}&typeId={typeId}
	 * 
	 * @param platformId
	 * @param typeId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExtensionItems(@QueryParam("platformId") String platformId, @QueryParam("typeId") String typeId) {
		List<ExtensionItemDTO> DTOs = new ArrayList<ExtensionItemDTO>();

		try {
			ExtensionRegistryService service = getService();
			List<ExtensionItem> items = null;
			if (typeId == null) {
				items = service.getExtensionItems(platformId);
			} else {
				items = service.getExtensionItems(platformId, typeId);
			}
			if (items != null) {
				for (ExtensionItem item : items) {
					ExtensionItemDTO DTO = ModelConverter.Extensions.toDTO(item);
					DTOs.add(DTO);
				}
			}

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(DTOs).build();
	}

	/**
	 * Add an extension item.
	 *
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/extensionitems (Body parameter: ExtensionItemCreateRequest)
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addExtensionItem(ExtensionItemCreateRequest request) {
		if (request == null) {
			ErrorDTO nullBody = new ErrorDTO("ExtensionItemCreateRequest is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullBody).build();
		}

		ExtensionItemDTO newDTO = null;
		try {
			ExtensionRegistryService service = getService();

			String platformId = request.getPlatformId();
			String typeId = request.getTypeId();
			String extensionId = request.getExtensionId();
			String name = request.getName();
			String description = request.getDescription();
			Map<String, Object> properties = request.getProperties();

			ExtensionItem newItem = service.addExtensionItem(platformId, typeId, extensionId, name, description, properties);
			if (newItem != null) {
				newDTO = ModelConverter.Extensions.toDTO(newItem);
			}

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (newDTO == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok().entity(newDTO).build();
	}

	/**
	 * Update an extension item.
	 *
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/extensionitems (Body parameter: ExtensionItemUpdateRequest)
	 * 
	 * @param request
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateExtensionItem(ExtensionItemUpdateRequest request) {
		if (request == null) {
			ErrorDTO nullBody = new ErrorDTO("ExtensionItemUpdateRequest is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullBody).build();
		}

		boolean succeed = false;
		try {
			ExtensionRegistryService service = getService();

			String platformId = request.getPlatformId();
			String typeId = request.getTypeId();
			String extensionId = request.getExtensionId();
			String newTypeId = request.getNewTypeId();
			String newExtensionId = request.getNewExtensionId();
			String newName = request.getNewName();
			String newDescription = request.getNewDescription();
			Map<String, Object> properties = request.getProperties();

			succeed = service.updateExtensionItem(platformId, typeId, extensionId, newTypeId, newExtensionId, newName, newDescription, properties);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "ExtensionItem is updated successfully.");
			return Response.ok().entity(statusDTO).build();

		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.FAILED, "ExtensionItem is not updated.");
			return Response.ok().entity(statusDTO).build();
		}
	}

	/**
	 * Remove extension items.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/extensionitems?platformId={platformId}
	 * 
	 * @param platformId
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteExtensionItems(@QueryParam("platformId") String platformId) {
		if (platformId == null || platformId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("platformId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean succeed = false;
		try {
			ExtensionRegistryService service = getService();
			succeed = service.removeExtensionItems(platformId);

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

}
