package org.nb.mgm.client.ws;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.nb.mgm.model.dto.MetaSpaceDTO;
import org.origin.common.rest.client.AbstractWSClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.util.ResponseUtil;

/*
 * MetaSpace resource client
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces?name={name}&filter={filter}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces/{metaSpaceId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces (Body parameter: MetaSpaceDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces (Body parameter: MetaSpaceDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces/{metaSpaceId}
 * 
 */
public class MetaSpaceWSClient extends AbstractWSClient {

	/**
	 * 
	 * @param config
	 */
	public MetaSpaceWSClient(ClientConfiguration config) {
		super(config);
	}

	@Override
	public int ping() throws ClientException {
		return 1;
	}

	/**
	 * Get MetaSpaces in a MetaSector.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces?name={name}&filter={filter}
	 * 
	 * @param metaSectorId
	 * @return
	 * @throws ClientException
	 */
	public List<MetaSpaceDTO> getMetaSpaces(String metaSectorId) throws ClientException {
		return getMetaSpaces(metaSectorId, null);
	}

	/**
	 * Get MetaSpaces in a MetaSector.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces?name={name}&filter={filter}
	 * 
	 * @param metaSectorId
	 * @param properties
	 *            supported keys are: "name", "filter".
	 * @return
	 * @throws ClientException
	 */
	public List<MetaSpaceDTO> getMetaSpaces(String metaSectorId, Properties properties) throws ClientException {
		List<MetaSpaceDTO> metaSpaces = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(metaSectorId).path("metaspaces");
			if (properties != null) {
				String name = properties.getProperty("name");
				if (name != null) {
					target.queryParam("name", name);
				}
				String filter = properties.getProperty("filter");
				if (filter != null) {
					target.queryParam("filter", filter);
				}
			}
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			metaSpaces = response.readEntity(new GenericType<List<MetaSpaceDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (metaSpaces == null) {
			metaSpaces = Collections.emptyList();
		}
		return metaSpaces;
	}

	/**
	 * Get a MetaSpace from a MetaSector.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces/{metaSpaceId}
	 * 
	 * @param metaSectorId
	 * @param metaSpaceId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public MetaSpaceDTO getMetaSpace(String metaSectorId, String metaSpaceId) throws ClientException {
		MetaSpaceDTO metaSpace = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("metaspaces").path(metaSpaceId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			metaSpace = response.readEntity(MetaSpaceDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return metaSpace;
	}

	/**
	 * Add a MetaSpace to a MetaSector.
	 * 
	 * Request URL (POST): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces
	 * 
	 * @param metaSectorId
	 * @param metaSpace
	 *            Body parameter for the new MetaSpace.
	 * 
	 * @return new MetaSpace
	 * @throws ClientException
	 */
	public MetaSpaceDTO addMetaSpace(String metaSectorId, MetaSpaceDTO metaSpace) throws ClientException {
		MetaSpaceDTO newMetaSpace = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("metaspaces").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<MetaSpaceDTO>(metaSpace) {
			}));
			checkResponse(response);

			newMetaSpace = response.readEntity(MetaSpaceDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return newMetaSpace;
	}

	/**
	 * Update a MetaSpace.
	 * 
	 * Request URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces
	 * 
	 * @param metaSectorId
	 * @param updateMetaSpaceRequest
	 *            Body parameter for updating the MetaSpace.
	 * 
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateMetaSpace(String metaSectorId, MetaSpaceDTO updateMetaSpaceRequest) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("metaspaces").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<MetaSpaceDTO>(updateMetaSpaceRequest) {
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
	 * Delete a MetaSpace from a MetaSector.
	 * 
	 * Request URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces/{metaSpaceId}
	 * 
	 * @param metaSectorId
	 * @param metaSpaceId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO deleteMetaSpace(String metaSectorId, String metaSpaceId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("metaspaces").path(metaSpaceId).request(MediaType.APPLICATION_JSON);
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
