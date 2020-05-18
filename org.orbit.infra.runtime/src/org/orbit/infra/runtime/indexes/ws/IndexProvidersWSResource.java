package org.orbit.infra.runtime.indexes.ws;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

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

import org.orbit.infra.model.indexes.IndexProviderItem;
import org.orbit.infra.model.indexes.IndexProviderItemDTO;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.orbit.infra.runtime.util.RuntimeModelConverter;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;

/*-
 * Index providers web service resource
 *
 * {contextRoot}: /orbit/v1/indexservice/
 *
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexproviders
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexproviders (Body parameter: IndexProviderDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/indexproviders?id={id}
 * 
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.SYSTEM_ADMIN, OrbitRoles.INDEX_ADMIN })
@Path("/indexproviders")
@Produces(MediaType.APPLICATION_JSON)
public class IndexProvidersWSResource extends AbstractWSApplicationResource {

	@Inject
	public IndexService service;

	public IndexService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("IndexService is not available.");
		}
		return this.service;
	}

	/**
	 * Get index providers.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexproviders
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIndexProviders() {
		List<IndexProviderItemDTO> DTOs = new ArrayList<IndexProviderItemDTO>();

		try {
			IndexService indexService = getService();
			List<IndexProviderItem> indexProviderItems = indexService.getIndexProviders();

			for (IndexProviderItem indexItem : indexProviderItems) {
				IndexProviderItemDTO dto = RuntimeModelConverter.INDEX_SERVICE.toDTO(indexItem);
				DTOs.add(dto);
			}

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(DTOs).build();
	}

	/**
	 * Add an index provider.
	 *
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/indexproviders (Body parameter: IndexProviderDTO)
	 *
	 * @param indexProviderId
	 * @param newIndexItemRequest
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addIndexProvider(IndexProviderItemDTO newItemRequest) {
		if (newItemRequest == null) {
			ErrorDTO nullBody = new ErrorDTO("Request is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullBody).build();
		}

		IndexProviderItemDTO newItemDTO = null;
		try {
			String id = newItemRequest.getId();
			String name = newItemRequest.getName();
			String description = newItemRequest.getDescription();

			IndexService indexService = getService();
			IndexProviderItem indexProviderItem = indexService.getIndexProvider(id);
			if (indexProviderItem != null) {
				indexProviderItem.setName(name);
				indexProviderItem.setDescription(description);
			} else {
				indexProviderItem = indexService.addIndexProvider(id, name, description);
			}
			newItemDTO = RuntimeModelConverter.INDEX_SERVICE.toDTO(indexProviderItem);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(newItemDTO).build();
	}

	/**
	 * Delete index provider.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/indexproviders?id={id}
	 * 
	 * @param ud
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteIndexProvider(@QueryParam("id") String id) {
		if (id == null) {
			ErrorDTO nullIdError = new ErrorDTO("id parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(nullIdError).build();
		}

		boolean succeed = false;
		try {
			IndexService indexService = getService();
			succeed = indexService.deleteIndexProvider(id);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, MessageFormat.format("IndexProvider ({0}) is deleted.", new Object[] { id }));
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, MessageFormat.format("IndexProvider ({0}) is not deleted.", new Object[] { id }));
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

}
