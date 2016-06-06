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

import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.mgm.model.dto.IndexItemActionDTO;
import org.origin.mgm.model.dto.IndexItemDTO;

/**
 * IndexService Client
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/?indexProviderId={indexProviderId}&namespace={namespace}
 * 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/indexitems (Body parameter: IndexItemActionDTO)
 * 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/indexitems/action (Body parameter: IndexItemActionDTO)
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
	 * Ping the Home management server.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public int ping() throws ClientException {
		return 1;
	}

	/**
	 * Get index items.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/?indexProviderId={indexProviderId}&namespace={namespace}
	 * 
	 * @param indexProviderId
	 * @param namespace
	 * @return
	 * @throws ClientException
	 */
	public List<IndexItemDTO> getIndexItems(String indexProviderId, String namespace) throws ClientException {
		List<IndexItemDTO> indexItemDTOs = null;
		try {
			WebTarget target = getRootPath().path("indexitems");
			if (indexProviderId != null) {
				target.queryParam("indexProviderId", indexProviderId);
			}
			if (namespace != null) {
				target.queryParam("namespace", namespace);
			}
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			indexItemDTOs = response.readEntity(new GenericType<List<IndexItemDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
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
	 * @param namespace
	 * @param name
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public IndexItemDTO addIndexItem(String indexProviderId, String namespace, String name, Map<String, Object> properties) throws ClientException {
		IndexItemDTO newIndexItemDTO = null;

		IndexItemDTO newIndexItemRequest = new IndexItemDTO();
		newIndexItemRequest.setIndexProviderId(indexProviderId);
		newIndexItemRequest.setNamespace(namespace);
		newIndexItemRequest.setName(name);
		newIndexItemRequest.setProperties(properties);

		try {
			Builder builder = getRootPath().path("indexitems").request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(Entity.json(new GenericEntity<IndexItemDTO>(newIndexItemRequest) {
			}));
			checkResponse(response);

			newIndexItemDTO = response.readEntity(IndexItemDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return newIndexItemDTO;
	}

	/**
	 * Execute an action.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/indexitems/action (Body parameter: IndexItemActionDTO)
	 * 
	 * @param action
	 * @param parameters
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO action(String action, Map<String, Object> parameters) throws ClientException {
		StatusDTO status = null;
		try {
			IndexItemActionDTO actionRequest = new IndexItemActionDTO();
			actionRequest.setAction(action);
			actionRequest.setParameters(parameters);

			Builder builder = getRootPath().path("indexitems").path("action").request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(Entity.json(new GenericEntity<IndexItemActionDTO>(actionRequest) {
			}));
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return status;
	}

}
