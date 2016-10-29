package org.origin.mgm.client.ws;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.origin.common.io.IOUtil;
import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.mgm.model.dto.IndexItemCommandRequestDTO;
import org.origin.mgm.model.dto.IndexItemDTO;

/**
 * IndexService Client
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/?indexProviderId={indexProviderId}&type={type}
 * 
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems (Body parameter: IndexItemDTO)
 * 
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/indexitems (Body parameter: IndexItemDTO)
 * 
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/indexitems?indexitemid={indexitemid}
 *
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/indexitems?indexproviderid={indexproviderid}&type={type}&name={name}
 * 
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems/commandRequest (Body parameter: IndexItemCommandRequestDTO)
 * 
 */
public class IndexServiceClient extends AbstractClient {

	/**
	 * 
	 * @param config
	 */
	public IndexServiceClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * Ping index service.
	 * 
	 * @see HomeAgentWSClient
	 * 
	 * @return
	 * @throws ClientException
	 */
	public int ping() throws ClientException {
		int result = 0;
		Response response = null;
		try {
			Builder builder = getRootPath().path("/ping").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			result = response.readEntity(Integer.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return result;
	}

	/**
	 * Get index items.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/?indexProviderId={indexProviderId}&type={type}
	 * 
	 * @param indexProviderId
	 * @param type
	 * @return
	 * @throws ClientException
	 */
	public List<IndexItemDTO> getIndexItems(String indexProviderId, String type) throws ClientException {
		List<IndexItemDTO> indexItemDTOs = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("indexitems");
			if (indexProviderId != null) {
				target.queryParam("indexProviderId", indexProviderId);
			}
			if (type != null) {
				target.queryParam("type", type);
			}
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			indexItemDTOs = response.readEntity(new GenericType<List<IndexItemDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		if (indexItemDTOs == null) {
			indexItemDTOs = Collections.emptyList();
		}
		return indexItemDTOs;
	}

	/**
	 * Add an index item.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/indexitems (Body parameter: IndexItemActionDTO)
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public IndexItemDTO addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws ClientException {
		IndexItemDTO newIndexItemDTO = null;

		IndexItemDTO newIndexItemRequest = new IndexItemDTO();
		newIndexItemRequest.setIndexProviderId(indexProviderId);
		newIndexItemRequest.setType(type);
		newIndexItemRequest.setName(name);
		newIndexItemRequest.setProperties(properties);

		Response response = null;
		try {
			Builder builder = getRootPath().path("indexitems").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<IndexItemDTO>(newIndexItemRequest) {
			}));
			checkResponse(response);

			newIndexItemDTO = response.readEntity(IndexItemDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return newIndexItemDTO;
	}

	/**
	 * Delete an index item.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/indexitems?indexitemid={indexitemid}
	 * 
	 * @see AppStoreWSClient
	 * @param indexItemId
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO removeIndexItem(Integer indexItemId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("indexitems").queryParam("indexitemid", indexItemId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Delete an index item.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/indexitems?indexproviderid={indexproviderid}&type={type}&name={name}
	 * 
	 * @see AppStoreWSClient
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO removeIndexItem(String indexProviderId, String type, String name) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("indexitems").queryParam("indexproviderid", indexProviderId).queryParam("type", type).queryParam("name", name).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Execute an action.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/indexitems/commandRequest (Body parameter: IndexItemCommandRequestDTO)
	 * 
	 * @param command
	 * @param params
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO sendCommand(String command, Map<String, Object> params) throws ClientException {
		StatusDTO status = null;
		try {
			IndexItemCommandRequestDTO commandRequest = new IndexItemCommandRequestDTO();
			commandRequest.setCommand(command);
			commandRequest.setParameters(params);

			Builder builder = getRootPath().path("indexitems").path("commandRequest").request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(Entity.json(new GenericEntity<IndexItemCommandRequestDTO>(commandRequest) {
			}));
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return status;
	}

}
