package org.origin.mgm.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import org.origin.mgm.model.dto.IndexItemDTO;
import org.origin.mgm.model.runtime.IndexItem;
import org.origin.mgm.service.IndexService;

/**
 * Index items resource
 *
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexservice/indexitems?indexproviderid={indexproviderid}&type={type}
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexservice/indexitems/exists?indexproviderid={indexproviderid}&type={type}&name={name}
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexservice/indexitems/single?indexproviderid={indexproviderid}&type={type}&name={name}
 * 
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexservice/indexitems (Body parameter: IndexItemDTO)
 * 
 */
@Path("/indexservice/indexitems")
@Produces(MediaType.APPLICATION_JSON)
public class IndexItemsResource extends AbstractApplicationResource {

	/**
	 * Get index items.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexservice/indexitems?indexproviderid={indexproviderid}&type={type}
	 * 
	 * e.g. http://10.98.200.137:9090/orbit/v1/indexservice/indexitems?indexproviderid=''&type=''
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIndexItems(@QueryParam("indexproviderid") String indexProviderId, @QueryParam("type") String type, @QueryParam("name") String name) {
		List<IndexItemDTO> indexItemDTOs = new ArrayList<IndexItemDTO>();

		IndexService indexService = getService(IndexService.class);
		try {
			List<IndexItem> indexItems = null;

			if (indexProviderId != null && type != null && name != null) {
				IndexItem indexItem = indexService.getIndexItem(indexProviderId, type, name);
				if (indexItem != null) {
					indexItems = new ArrayList<IndexItem>();
					indexItems.add(indexItem);
				}
			} else if (indexProviderId != null || type != null) {
				indexItems = indexService.getIndexItems(indexProviderId, type);
			} else {
				indexItems = indexService.getIndexItems();
			}

			if (indexItems != null) {
				for (IndexItem indexItem : indexItems) {
					IndexItemDTO indexItemDTO = DTOConverter.getInstance().toDTO(indexItem);
					indexItemDTOs.add(indexItemDTO);
				}
			}

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(indexItemDTOs).build();
	}

	/**
	 * Check whether an index item exists.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexservice/indexitems/exists?indexproviderid={indexproviderid}&type={type}&name={name}
	 * 
	 * e.g. http://10.98.200.137:9090/orbit/v1/indexservice/indexitems/exists?indexproviderid=''&type=''&name=''
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param indexitemid
	 * @return
	 */
	@Path("exists")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response exists(@QueryParam("indexproviderid") String indexProviderId, @QueryParam("type") String type, @QueryParam("name") String name) {
		IndexService indexService = getService(IndexService.class);
		try {
			IndexItem indexItem = indexService.getIndexItem(indexProviderId, type, name);
			Boolean exists = (indexItem != null) ? Boolean.TRUE : Boolean.FALSE;
			return Response.ok().entity(exists).build();

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
	}

	/**
	 * Get an index item.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexservice/indexitems/single?indexproviderid={indexproviderid}&type={type}&name={name}
	 * 
	 * e.g. http://10.98.200.137:9090/orbit/v1/indexservice/indexitems/single?indexproviderid=''&type=''&name=''
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @return
	 */
	@GET
	@Path("single")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIndexItem(@QueryParam("indexproviderid") String indexProviderId, @QueryParam("type") String type, @QueryParam("name") String name) {
		IndexItemDTO indexItemDTO = null;

		IndexService indexService = getService(IndexService.class);
		try {
			// get an index item by indexProviderId, type and name
			IndexItem indexItem = indexService.getIndexItem(indexProviderId, type, name);
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
	 * Add an index item.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/indexservice/indexitems (Body parameter: IndexItemDTO)
	 * 
	 * e.g. http://10.98.200.137:9090/orbit/v1/indexservice/indexitems (Body parameter: IndexItemDTO)
	 * 
	 * @param newIndexItemRequest
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addIndexItem(IndexItemDTO newIndexItemRequest) {
		if (newIndexItemRequest == null) {
			ErrorDTO nullBody = new ErrorDTO("indexItemDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullBody).build();
		}

		boolean succeed = false;
		IndexService indexService = getService(IndexService.class);
		try {
			String indexProviderId = newIndexItemRequest.getIndexProviderId();
			String type = newIndexItemRequest.getType();
			String name = newIndexItemRequest.getName();

			// Map<String, Object> properties = newIndexItemRequest.getProperties();
			String propertiesString = newIndexItemRequest.getPropertiesString();
			Map<String, Object> properties = JSONUtil.toProperties(propertiesString);

			succeed = indexService.addIndexItem(indexProviderId, type, name, properties);

		} catch (IndexServiceException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "Index item is added.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "Index item is not added.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

}
