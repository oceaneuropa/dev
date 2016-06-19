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
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems?indexproviderid={indexproviderid}&namespace={namespace}
 *
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/indexitems
 *
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/indexitems?indexitemid={indexitemid}
 *
 */
@javax.ws.rs.Path("/indexitems")
@Produces(MediaType.APPLICATION_JSON)
public class IndexItemsResource extends AbstractApplicationResource {

	/**
	 * Get index items.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems?indexproviderid={indexproviderid}&namespace={namespace}
	 * 
	 * @param indexProviderId
	 * @param namespace
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIndexItems(@QueryParam("indexproviderid") String indexProviderId, @QueryParam("namespace") String namespace) {
		List<IndexItemDTO> indexItemDTOs = new ArrayList<IndexItemDTO>();

		IndexService indexService = getService(IndexService.class);
		try {
			List<IndexItem> indexItems = null;
			if (indexProviderId != null && namespace != null) {
				indexItems = indexService.getIndexItems(indexProviderId, namespace);

			} else if (indexProviderId != null && namespace == null) {
				indexItems = indexService.getIndexItemsByIndexProvider(indexProviderId);

			} else if (indexProviderId == null && namespace != null) {
				indexItems = indexService.getIndexItemsByNamespace(namespace);

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
	 * Add an index item.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/indexitems
	 * 
	 * Body parameter: IndexItemDTO
	 * 
	 * @param indexItemDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addIndexItem(IndexItemDTO indexItemDTO) {
		if (indexItemDTO == null) {
			ErrorDTO nullBody = new ErrorDTO("Body parameter (IndexItemDTO) is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullBody).build();
		}

		IndexService indexService = getService(IndexService.class);
		try {
			String indexProviderId = indexItemDTO.getIndexProviderId();
			String namespace = indexItemDTO.getNamespace();
			String name = indexItemDTO.getName();
			Map<String, Object> properties = indexItemDTO.getProperties();

			indexService.addIndexItem(indexProviderId, namespace, name, properties);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(indexItemDTO).build();
	}

	/**
	 * Remove an index item.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/indexitems?indexitemid={indexitemid}
	 * 
	 * @param indexItemId
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeIndexItem(@QueryParam("indexitemid") Integer indexItemId) {
		if (indexItemId == null) {
			ErrorDTO nullIndexItemIdError = new ErrorDTO("indexItemId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullIndexItemIdError).build();
		}

		IndexService indexService = getService(IndexService.class);
		boolean succeed = false;
		try {
			succeed = indexService.removeIndexItem(indexItemId);
		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = null;
		if (succeed) {
			statusDTO = new StatusDTO("200", "success", MessageFormat.format("IndexItem (indexItemId={0}) is removed successfully.", new Object[] { indexItemId }));
		} else {
			statusDTO = new StatusDTO("200", "fail", MessageFormat.format("IndexItem (indexItemId={0}) is not removed.", new Object[] { indexItemId }));
		}
		return Response.ok().entity(statusDTO).build();
	}

}
