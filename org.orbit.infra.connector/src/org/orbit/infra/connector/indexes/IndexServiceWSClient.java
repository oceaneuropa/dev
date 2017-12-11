package org.orbit.infra.connector.indexes;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.infra.model.indexes.IndexItemCommandRequestDTO;
import org.orbit.infra.model.indexes.IndexItemDTO;
import org.orbit.infra.model.indexes.IndexItemSetPropertiesRequestDTO;
import org.orbit.infra.model.indexes.IndexItemSetPropertyRequestDTO;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.client.AbstractWSClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.util.ResponseUtil;
import org.origin.common.util.StringUtil;

/*
 * IndexService Client
 * 
 * {contextRoot} example:
 * /orbit/v1/indexservice/
 * 
 * 1. IndexItem service itself
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/commandrequest (Body parameter: IndexItemCommandRequestDTO)
 * 
 * 2. IndexItems
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}?type={type}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}?type={type}&name={name}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid} (Body parameter: IndexItemDTO)
 * 
 *   not implemented:
 *   URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/exists?type={type}&name={name}
 *   URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/indexitem?type={type}&name={name}
 *
 * 3. IndexItem:
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}
 * 
 *   Not implemented:
 *   URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexitemid}/exists
 *   URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems (Body parameter: IndexItemDTO)
 *   URL (PUT): {scheme}://{host}:{port}/{contextRoot}/indexitems (Body parameter: IndexItemDTO)
 *
 * 4. IndexItem Properties:
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/properties
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/properties (Body parameter: IndexItemSetPropertiesRequestDTO)
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/property (Body parameter: IndexItemSetPropertyRequestDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/properties?propertynames={propertynames}
 * 
 * @see HomeAgentWSClient
 * @see AppStoreWSClient
 */
public class IndexServiceWSClient extends AbstractWSClient {

	/**
	 * 
	 * @param config
	 */
	public IndexServiceWSClient(ClientConfiguration config) {
		super(config);
	}

	// ----------------------------------------------------------------------
	// Methods about IndexService itself
	// ----------------------------------------------------------------------
	/**
	 * Ping index service.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
	 * 
	 * @return
	 * @throws ClientException
	 */
	public int ping() throws ClientException {
		int result = 0;
		Response response = null;
		try {
			Builder builder = getRootPath().path("ping").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			result = response.readEntity(Integer.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return result;
	}

	/**
	 * Execute an action.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/commandrequest (Body parameter: IndexItemCommandRequestDTO)
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

			Builder builder = getRootPath().path("commandrequest").request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(Entity.json(new GenericEntity<IndexItemCommandRequestDTO>(commandRequest) {
			}));
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		}
		return status;
	}

	// ----------------------------------------------------------------------
	// Methods about IndexItems
	// ----------------------------------------------------------------------
	/**
	 * Get index items.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}?type={type}
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
			WebTarget target = getRootPath().path("indexitems").path(indexProviderId);
			if (type != null) {
				target = target.queryParam("type", type);
			}
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			indexItemDTOs = response.readEntity(new GenericType<List<IndexItemDTO>>() {
			});

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (indexItemDTOs == null) {
			indexItemDTOs = Collections.emptyList();
		}
		return indexItemDTOs;
	}

	/**
	 * Get an index item.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}?type={type}&name={name}
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public IndexItemDTO getIndexItem(String indexProviderId, String type, String name) throws ClientException {
		IndexItemDTO indexItemDTO = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("indexitems").path(indexProviderId);
			target = target.queryParam("type", type);
			target = target.queryParam("name", name);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			// indexItemDTO = response.readEntity(IndexItemDTO.class);
			List<IndexItemDTO> indexItemDTOs = response.readEntity(new GenericType<List<IndexItemDTO>>() {
			});
			if (indexItemDTOs != null && !indexItemDTOs.isEmpty()) {
				indexItemDTO = indexItemDTOs.get(0);
			}

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return indexItemDTO;
	}

	/**
	 * Add an index item.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid} (Body parameter: IndexItemDTO)
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
			Builder builder = getRootPath().path("indexitems").path(indexProviderId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<IndexItemDTO>(newIndexItemRequest) {
			}));
			checkResponse(response);

			newIndexItemDTO = response.readEntity(IndexItemDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return newIndexItemDTO;
	}

	// ----------------------------------------------------------------------
	// Methods about IndexItem
	// ----------------------------------------------------------------------
	/**
	 * Get an index item.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @return
	 * @throws ClientException
	 */
	public IndexItemDTO getIndexItem(String indexProviderId, Integer indexItemId) throws ClientException {
		IndexItemDTO indexItemDTO = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("indexitems").path(indexProviderId).path(String.valueOf(indexItemId));
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			indexItemDTO = response.readEntity(IndexItemDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return indexItemDTO;
	}

	/**
	 * Remove an index item.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO removeIndexItem(String indexProviderId, Integer indexItemId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("indexitems").path(indexProviderId).path(String.valueOf(indexItemId)).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	// ----------------------------------------------------------------------
	// Methods about IndexItem properties
	// ----------------------------------------------------------------------
	/**
	 * Get properties.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/properties
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @return
	 * @throws ClientException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, ?> getProperties(String indexProviderId, Integer indexItemId) throws ClientException {
		Map<String, ?> properties = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("indexitems").path(indexProviderId).path(String.valueOf(indexItemId)).path("properties").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			properties = response.readEntity(Map.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (properties == null) {
			properties = new Hashtable<String, Object>();
		}
		return properties;
	}

	/**
	 * Set properties.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/properties (Body parameter:
	 * IndexItemSetPropertiesRequestDTO)
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws ClientException {
		if (properties == null) {
			throw new IllegalArgumentException("properties is null.");
		}

		StatusDTO status = null;
		Response response = null;
		try {
			String propertiesString = JSONUtil.toJsonString(properties);
			IndexItemSetPropertiesRequestDTO setPropertiesRequest = new IndexItemSetPropertiesRequestDTO(indexItemId, propertiesString);

			WebTarget target = getRootPath().path("indexitems").path(indexProviderId).path(String.valueOf(indexItemId)).path("properties");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<IndexItemSetPropertiesRequestDTO>(setPropertiesRequest) {
			}));
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Set property.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/property (Body parameter: IndexItemSetPropertyRequestDTO)
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param property
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO setProperty(String indexProviderId, Integer indexItemId, String propName, Object propValue, String propType) throws ClientException {
		if (propName == null) {
			throw new IllegalArgumentException("propName is null.");
		}
		if (propValue == null) {
			throw new IllegalArgumentException("propValue is null.");
		}

		StatusDTO status = null;
		Response response = null;
		try {
			IndexItemSetPropertyRequestDTO setPropertyRequest = new IndexItemSetPropertyRequestDTO(indexItemId, propName, propValue, propType);

			WebTarget target = getRootPath().path("indexitems").path(indexProviderId).path(String.valueOf(indexItemId)).path("property");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<IndexItemSetPropertyRequestDTO>(setPropertyRequest) {
			}));
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Remove property.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/indexitems/{indexproviderid}/{indexitemid}/properties?propertynames={propertynames}
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param propertyNames
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO removeProperties(String indexProviderId, Integer indexItemId, List<String> propertyNames) throws ClientException {
		if (propertyNames == null) {
			throw new IllegalArgumentException("propName is null.");
		}
		if (propertyNames.isEmpty()) {
			throw new IllegalArgumentException("propValue is empty.");
		}

		StatusDTO status = null;
		Response response = null;
		try {
			// String propertyNamesString = JSONUtil.toJsonString(propertyNames);
			// String propertyNamesStr = Arrays.toString(propertyNames.toArray(new String[propertyNames.size()]));
			String propertyNamesStr = StringUtil.toString(propertyNames);

			WebTarget target = getRootPath().path("indexitems").path(indexProviderId).path(String.valueOf(indexItemId)).path("properties");
			target = target.queryParam("propertynames", propertyNamesStr);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

}

/// **
// * Check whether an index item exists.
// *
// * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexservice/indexitems/exists?indexproviderid={indexproviderid}&type={type}&name={name}
// *
// * @param indexProviderId
// * @param type
// * @param name
// * @return
// * @throws ClientException
// */
// public boolean indexItemExists(String indexProviderId, String type, String name) throws ClientException {
// Boolean exists = Boolean.FALSE;
// Response response = null;
// try {
// WebTarget target = getRootPath().path("indexservice/indexitems/exists");
// target.queryParam("indexproviderid", indexProviderId);
// target.queryParam("type", type);
// target.queryParam("name", name);
// Builder builder = target.request(MediaType.APPLICATION_JSON);
// response = updateHeaders(builder).get();
// checkResponse(response);
//
// exists = response.readEntity(Boolean.class);
//
// } catch (ClientException e) {
// handleException(e);
// } finally {
// IOUtil.closeQuietly(response, true);
// }
// return exists;
// }

/// **
// * Check whether an index item exists.
// *
// * URL (GET): {scheme}://{host}:{port}/{contextRoot}/indexservice/indexitem/{indexitemid}/exists
// *
// * @param indexItemId
// * @return
// * @throws ClientException
// */
// public boolean indexItemExists(Integer indexItemId) throws ClientException {
// Boolean exists = Boolean.FALSE;
// Response response = null;
// try {
// WebTarget target = getRootPath().path("indexservice/indexitem").path(String.valueOf(indexItemId)).path("exists");
// target.queryParam("indexitemid", indexItemId);
// Builder builder = target.request(MediaType.APPLICATION_JSON);
// response = updateHeaders(builder).get();
// checkResponse(response);
//
// exists = response.readEntity(Boolean.class);
//
// } catch (ClientException e) {
// handleException(e);
// } finally {
// IOUtil.closeQuietly(response, true);
// }
// return exists;
// }