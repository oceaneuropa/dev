package org.origin.mgm.ws;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;
import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.dto.DTOConverter;
import org.origin.mgm.model.dto.IndexItemDTO;
import org.origin.mgm.model.runtime.IndexItem;
import org.origin.mgm.service.IndexService;

/**
 * Index items resource
 * 
 * URL (GET): {scheme}://{host}:{port}/indexservice/v1/indexitems
 *
 * URL (POST): {scheme}://{host}:{port}/indexservice/v1/indexitems
 *
 * URL (DELETE): {scheme}://{host}:{port}/indexservice/v1/serviceregistry?namespace={namespace}&name={name}
 *
 */
@javax.ws.rs.Path("/indexitems")
@Produces(MediaType.APPLICATION_JSON)
public class IndexItemsResource extends AbstractApplicationResource {

	/**
	 * Get index items.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/indexservice/v1/indexitems
	 * 
	 * @param namespace
	 *            type of an index item.
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIndexItems(@QueryParam("indexproviderid") String indexProviderId, @QueryParam("namespace") String namespace) {
		List<IndexItemDTO> indexItemDTOs = new ArrayList<IndexItemDTO>();

		IndexService indexService = getService(IndexService.class);
		try {
			List<IndexItem> indexItems = null;
			if (namespace != null) {
				indexItems = indexService.getIndexItems(namespace);
			} else {
				indexItems = indexService.getIndexItems();
			}

			for (IndexItem indexItem : indexItems) {
				IndexItemDTO indexItemDTO = DTOConverter.getInstance().toDTO(indexItem);
				indexItemDTOs.add(indexItemDTO);
			}

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(indexItemDTOs).build();
	}

	/**
	 * Register a service to the service registry.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/management/v1/serviceregistry
	 * 
	 * Body parameter: ServiceEntryDTO
	 * 
	 * @param indexItemDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addIndexItem(IndexItemDTO indexItemDTO) {
		IndexService indexService = getService(IndexService.class);
		try {
			String type = indexItemDTO.getNamespace();
			String name = indexItemDTO.getName();
			Map<String, Object> props = indexItemDTO.getProperties();

			// indexService.createIndexItem(indexProviderId, type, name);

		} catch (Exception e) {
			// ErrorDTO error = handleError(e, e.getCode(), true);
			// return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(indexItemDTO).build();
	}

	/**
	 * Unregister a service from the service registry.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/management/v1/serviceregistry?namespace={namespace}&name={name}
	 * 
	 * @param namespace
	 * @param name
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeService(@QueryParam("namespace") String namespace, @QueryParam("name") String name) {
		if (namespace == null) {
			namespace = IndexItem.DEFAULT_TYPE;
		}
		if (name == null || name.isEmpty()) {
			ErrorDTO nullNameError = new ErrorDTO("name is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullNameError).build();
		}

		IndexService indexService = getService(IndexService.class);
		try {
			// if (indexService.isIndexed(namespace, name)) {
			// StatusDTO statusDTO = new StatusDTO("201", "serviceNotFound", MessageFormat.format("Service ''{0}'' is not found.", new Object[] {
			// IndexItem.getFullName(namespace, name) }));
			// return Response.ok().entity(statusDTO).build();
			// }

			indexService.removeIndexItem(namespace, name);
		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO("200", "success", MessageFormat.format("Service ''{0}'' is unregistered successfully.", new Object[] { IndexItem.getFullName(namespace, name) }));
		return Response.ok().entity(statusDTO).build();
	}

}
