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

import org.nb.mgm.model.dto.MetaSectorDTO;
import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ClientUtil;
import org.origin.common.rest.model.StatusDTO;

/*
 * MetaSector resource client
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors/?name={name}&filter={filter}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors/{metaSectorId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/metasectors/ (Body parameter: MetaSectorDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/metasectors/ (Body parameter: MetaSectorDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/metasectors/{metaSectorId}
 * 
 */
public class MetaSectorWSClient extends AbstractClient {

	/**
	 * 
	 * @param config
	 */
	public MetaSectorWSClient(ClientConfiguration config) {
		super(config);
	}

	@Override
	public int ping() throws ClientException {
		return 1;
	}

	/**
	 * Get MetaSectors.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<MetaSectorDTO> getMetaSectors() throws ClientException {
		return getMetaSectors(null);
	}

	/**
	 * Get MetaSectors.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors/?name={name}&filter={filter}
	 * 
	 * @param properties
	 *            supported keys are: "name", "filter".
	 * @return
	 * @throws ClientException
	 */
	public List<MetaSectorDTO> getMetaSectors(Properties properties) throws ClientException {
		List<MetaSectorDTO> metaSectors = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("metasectors");
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

			metaSectors = response.readEntity(new GenericType<List<MetaSectorDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		if (metaSectors == null) {
			metaSectors = Collections.emptyList();
		}
		return metaSectors;
	}

	/**
	 * Get a MetaSector.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors/{metaSectorId}
	 * 
	 * @param metaSectorId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public MetaSectorDTO getMetaSector(String metaSectorId) throws ClientException {
		MetaSectorDTO metaSector = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("metasectors").path(metaSectorId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			metaSector = response.readEntity(MetaSectorDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		return metaSector;
	}

	/**
	 * Add a MetaSector.
	 * 
	 * Request URL (POST): {scheme}://{host}:{port}/{contextRoot}/metasectors
	 * 
	 * @param metaSector
	 *            Body parameter for the new MetaSector.
	 * 
	 * @return new MetaSector
	 * @throws ClientException
	 */
	public MetaSectorDTO addMetaSector(MetaSectorDTO metaSector) throws ClientException {
		MetaSectorDTO newMetaSector = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("metasectors").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<MetaSectorDTO>(metaSector) {
			}));
			checkResponse(response);

			newMetaSector = response.readEntity(MetaSectorDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		return newMetaSector;
	}

	/**
	 * Update a MetaSector.
	 * 
	 * Request URL (PUT): {scheme}://{host}:{port}/{contextRoot}/metasectors (Body parameter: MetaSectorDTO)
	 * 
	 * @param updateMetaSectorRequest
	 *            Body parameter for updating the MetaSector.
	 * 
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateMetaSector(MetaSectorDTO updateMetaSectorRequest) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("metasectors").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<MetaSectorDTO>(updateMetaSectorRequest) {
			}));
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Delete a MetaSector.
	 * 
	 * Request URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/metasectors/{metaSectorId}
	 * 
	 * @param metaSectorId
	 * 
	 * @return Delete status
	 * @throws ClientException
	 */
	public StatusDTO deleteMetaSector(String metaSectorId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("metasectors").path(metaSectorId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		return status;
	}

}
