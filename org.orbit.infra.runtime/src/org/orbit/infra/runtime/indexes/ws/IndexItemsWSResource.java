package org.orbit.infra.runtime.indexes.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.model.indexes.IndexItemDTO;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.orbit.infra.runtime.util.ModelConverter;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;
import org.origin.common.util.Printer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Index items resource
 *
 * {contextRoot} example:
 * /orbit/v1/indexservice/
 *
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}?type={type}&name={name}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid} (Body parameter: IndexItemDTO)
 * 
 *     Not being used:
 *     URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/exists?type={type}&name={name}
 *     URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/indexitem?type={type}&name={name}
 * 
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.SYSTEM_ADMIN, OrbitRoles.INDEX_ADMIN })
@Path("/indexitems/{indexproviderid}")
@Produces(MediaType.APPLICATION_JSON)
public class IndexItemsWSResource extends AbstractWSApplicationResource {

	protected static Logger LOG = LoggerFactory.getLogger(IndexItemsWSResource.class);

	@Inject
	public IndexService service;

	public IndexService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("IndexService is not available.");
		}
		return this.service;
	}

	/**
	 * Get index items.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}?type={type}&name={name}
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIndexItems(@PathParam("indexproviderid") String indexProviderId, @QueryParam("type") String type, @QueryParam("name") String name) {
		List<IndexItemDTO> indexItemDTOs = new ArrayList<IndexItemDTO>();

		IndexService indexService = getService();
		try {
			List<IndexItem> indexItems = null;

			if (indexProviderId != null) {
				if (type == null) {
					// type is not specified, get all index items from a index provider.
					indexItems = indexService.getIndexItems(indexProviderId);

				} else {
					// type is specified
					if (name != null) {
						// name is specified, get one index item by type and name from a index provider.
						IndexItem indexItem = indexService.getIndexItem(indexProviderId, type, name);
						if (indexItem != null) {
							indexItems = new ArrayList<IndexItem>();
							indexItems.add(indexItem);
						}

					} else {
						// name is not specified, get all index items by type from a index provider.
						indexItems = indexService.getIndexItems(indexProviderId, type);
					}
				}
			}

			// System.out.println(getClass().getSimpleName() + ".getIndexItems()");
			// System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			// for (IndexItem indexItem : indexItems) {
			// System.out.println(indexItem.getIndexItemId() + " - " + indexItem.getIndexProviderId() + " - " + indexItem.getType() + " - " +
			// indexItem.getName());
			// Printer.pl(indexItem.getProperties());
			// System.out.println();
			// }
			// System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

			// System.out.println("-------------------------------------------------------------------------------------------");
			if (indexItems != null) {
				for (IndexItem indexItem : indexItems) {
					IndexItemDTO indexItemDTO = ModelConverter.Indexes.toDTO(indexItem);
					indexItemDTOs.add(indexItemDTO);
					// System.out.println(indexItemDTO.getIndexItemId() + " - " + indexItemDTO.getIndexProviderId() + " - " + indexItemDTO.getType() + " - " +
					// indexItemDTO.getName());
					// System.out.println(indexItemDTO.getPropertiesString());
					// System.out.println();
				}
			}
			// System.out.println("-------------------------------------------------------------------------------------------");

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(indexItemDTOs).build();
	}

	/**
	 * Add an index item.
	 *
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid} (Body parameter: IndexItemDTO)
	 *
	 * @param indexProviderId
	 * @param newIndexItemRequest
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addIndexItem(@PathParam("indexproviderid") String indexProviderId, IndexItemDTO newIndexItemRequest) {
		if (newIndexItemRequest == null) {
			ErrorDTO nullBody = new ErrorDTO("indexItemDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullBody).build();
		}

		// boolean succeed = false;
		IndexItemDTO newIndexItemDTO = null;
		IndexService indexService = getService();
		try {
			// String indexProviderId = newIndexItemRequest.getIndexProviderId();
			String type = newIndexItemRequest.getType();
			String name = newIndexItemRequest.getName();

			// Map<String, Object> properties = newIndexItemRequest.getProperties();
			String propertiesString = newIndexItemRequest.getPropertiesString();
			Map<String, Object> properties = JSONUtil.toProperties(propertiesString);

			System.out.println(getClass().getSimpleName() + ".addIndexItem()");
			System.out.println(propertiesString);
			Printer.pl(properties);

			IndexItem newIndexItem = indexService.addIndexItem(indexProviderId, type, name, properties);
			if (newIndexItem != null) {
				newIndexItemDTO = ModelConverter.Indexes.toDTO(newIndexItem);
			}
		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		// if (succeed) {
		// StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "Index item is added.");
		// return Response.ok().entity(statusDTO).build();
		// } else {
		// StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "Index item is not added.");
		// return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		// }
		if (newIndexItemDTO == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok().entity(newIndexItemDTO).build();
	}

}

// /**
// * Check whether an index item exists.
// *
// * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/exists?type={type}&name={name}
// *
// * @param indexProviderId
// * @param type
// * @param name
// * @param indexitemid
// * @return
// */
// @Path("exists")
// @GET
// @Produces(MediaType.APPLICATION_JSON)
// public Response exists(@PathParam("indexproviderid") String indexProviderId, @QueryParam("type") String type, @QueryParam("name") String name) {
// IndexService indexService = getService(IndexService.class);
// try {
// IndexItem indexItem = indexService.getIndexItem(indexProviderId, type, name);
// Boolean exists = (indexItem != null) ? Boolean.TRUE : Boolean.FALSE;
// return Response.ok().entity(exists).build();
//
// } catch (IndexServiceException e) {
// ErrorDTO error = handleError(e, e.getCode(), true);
// return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
// }
// }
//
// /**
// * Get an index item.
// *
// * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/indexitem?type={type}&name={name}
// *
// * @param indexProviderId
// * @param type
// * @param name
// * @return
// */
// @GET
// @Path("indexitem")
// @Produces(MediaType.APPLICATION_JSON)
// public Response getIndexItem(@PathParam("indexproviderid") String indexProviderId, @QueryParam("type") String type, @QueryParam("name") String name) {
// IndexItemDTO indexItemDTO = null;
//
// IndexService indexService = getService(IndexService.class);
// try {
// // get an index item by indexProviderId, type and name
// IndexItem indexItem = indexService.getIndexItem(indexProviderId, type, name);
// if (indexItem != null) {
// indexItemDTO = DTOConverter.getInstance().toDTO(indexItem);
// }
//
// } catch (IndexServiceException e) {
// ErrorDTO error = handleError(e, e.getCode(), true);
// return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
// }
//
// if (indexItemDTO == null) {
// return Response.status(Status.NOT_FOUND).build();
// }
// return Response.ok().entity(indexItemDTO).build();
// }
