package org.origin.mgm.ws;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems?indexproviderid={indexproviderid}&type={type}
 *
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems (Body parameter: IndexItemDTO)
 *
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/indexitems (Body parameter: IndexItemDTO)
 *
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/indexitems?indexitemid={indexitemid}
 *
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/indexitems?indexproviderid={indexproviderid}&type={type}&name={name}
 * 
 */
@javax.ws.rs.Path("/indexitems")
@Produces(MediaType.APPLICATION_JSON)
public class IndexItemsResource extends AbstractApplicationResource {

	/**
	 * Get index items.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems?indexproviderid={indexproviderid}&type={type}
	 * 
	 * @param indexProviderId
	 * @param type
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIndexItems(@QueryParam("indexproviderid") String indexProviderId, @QueryParam("type") String type) {
		List<IndexItemDTO> indexItemDTOs = new ArrayList<IndexItemDTO>();

		IndexService indexService = getService(IndexService.class);
		try {
			List<IndexItem> indexItems = null;
			if (indexProviderId != null || type != null) {
				indexItems = indexService.getIndexItems(indexProviderId, type);
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
			ErrorDTO nullBody = new ErrorDTO("indexItemDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullBody).build();
		}

		IndexService indexService = getService(IndexService.class);
		try {
			String indexProviderId = indexItemDTO.getIndexProviderId();
			String type = indexItemDTO.getType();
			String name = indexItemDTO.getName();
			Map<String, Object> properties = indexItemDTO.getProperties();

			indexService.addIndexItem(indexProviderId, type, name, properties);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(indexItemDTO).build();
	}

	/**
	 * Update an index item.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/indexitems (Body parameter: IndexItemDTO)
	 * 
	 * @param indexItemDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateIndexItem(IndexItemDTO indexItemDTO) {
		if (indexItemDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("indexItemDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		boolean succeed = false;
		IndexService indexService = getService(IndexService.class);
		try {
			Integer indexItemId = indexItemDTO.getIndexItemId();
			if (indexItemId <= 0) {
				String indexProviderId = indexItemDTO.getIndexProviderId();
				String type = indexItemDTO.getType();
				String name = indexItemDTO.getName();
				IndexItem indexItem = indexService.getIndexItem(indexProviderId, type, name);
				if (indexItem != null) {
					indexItemId = indexItem.getIndexItemId();
				}
			}

			Map<String, Object> properties = indexItemDTO.getProperties();
			succeed = indexService.setProperty(indexItemId, properties);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO("200", "success", "IndexItem properties are updated successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO("200", "not changed", "IndexItem properties are not updated.");
			return Response.ok().entity(statusDTO).build();
		}
	}

	/**
	 * Remove an index item.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/indexitems?indexitemid={indexitemid}
	 *
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/indexitems?indexproviderid={indexproviderid}&type={type}&name={name}
	 * 
	 * @param indexItemId
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeIndexItem(@QueryParam("indexitemid") Integer indexItemId, @QueryParam("indexproviderid") String indexProviderId, @QueryParam("type") String type, @QueryParam("name") String name) {
		if (indexItemId == null) {
			ErrorDTO nullIndexItemIdError = new ErrorDTO("indexItemId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullIndexItemIdError).build();
		}

		IndexService indexService = getService(IndexService.class);
		boolean succeed = false;
		try {
			// if indexItemId is not specified, use combination of indexProviderId, type and name to find the IndexItem and its indexItemId
			if (indexItemId <= 0 && indexProviderId != null && !indexProviderId.isEmpty() && type != null && !type.isEmpty() && name != null && !name.isEmpty()) {
				IndexItem indexItem = indexService.getIndexItem(indexProviderId, type, name);
				if (indexItem != null) {
					indexItemId = indexItem.getIndexItemId();
				}
			}
			// delete IndexItem by indexItemId
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
