package org.nb.mgm.client.ws;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.nb.mgm.model.dto.HomeDTO;
import org.origin.common.io.IOUtil;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;

/*
 * Home web service client
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes?name={name}&url={url}&status={status}&filter={filter}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes (Body parameter: HomeDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes (Body parameter: HomeDTO)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}
 */
public class HomeClient extends AbstractClient {

	/**
	 * 
	 * @param config
	 */
	public HomeClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * Get all Homes in a Machine.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes?name={name}&url={url}&status={status}&filter={filter}
	 * 
	 * @param machineId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<HomeDTO> getHomes(String machineId) throws ClientException {
		return getHomes(machineId, null);
	}

	/**
	 * Get all Homes in a Machine by query parameters.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes?name={name}&url={url}&status={status}&filter={filter}
	 * 
	 * @param machineId
	 * @param properties
	 *            supported keys are: "name", "url", "status", "filter".
	 * @return
	 * @throws ClientException
	 */
	public List<HomeDTO> getHomes(String machineId, Properties properties) throws ClientException {
		List<HomeDTO> homes = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(machineId).path("homes");
			if (properties != null) {
				String name = properties.getProperty("name");
				if (name != null) {
					target.queryParam("name", name);
				}
				String url = properties.getProperty("url");
				if (url != null) {
					target.queryParam("url", url);
				}
				String status = properties.getProperty("status");
				if (status != null) {
					target.queryParam("status", url);
				}
				String filter = properties.getProperty("filter");
				if (filter != null) {
					target.queryParam("filter", filter);
				}
			}
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			homes = response.readEntity(new GenericType<List<HomeDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		if (homes == null) {
			homes = Collections.emptyList();
		}
		return homes;
	}

	/**
	 * Get Home by machine Id and home Id.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}
	 * 
	 * @param machineId
	 * @param homeId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public HomeDTO getHome(String machineId, String homeId) throws ClientException {
		HomeDTO home = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(machineId).path("homes").path(homeId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			home = response.readEntity(HomeDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return home;
	}

	/**
	 * Add a Home to a Machine.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes
	 * 
	 * @param machineId
	 * @param home
	 *            Body parameter for the new Home.
	 * 
	 * @return new Home
	 * @throws ClientException
	 */
	public HomeDTO addHome(String machineId, HomeDTO home) throws ClientException {
		HomeDTO newHome = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(machineId).path("homes").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<HomeDTO>(home) {
			}));
			checkResponse(response);

			newHome = response.readEntity(HomeDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return newHome;
	}

	/**
	 * Update Home information.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes
	 * 
	 * @param machineId
	 * @param home
	 *            Body parameter for updating the Home.
	 * 
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateHome(String machineId, HomeDTO home) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(machineId).path("homes").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<HomeDTO>(home) {
			}));
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
	 * Delete a Home from a Machine by home Id.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}
	 * 
	 * @param machineId
	 * @param homeId
	 * 
	 * @return Delete status
	 * @throws ClientException
	 */
	public StatusDTO deleteHome(String machineId, String homeId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(machineId).path("homes").path(homeId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
			response.close();

		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Get Home properties.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}/properties
	 * 
	 * @param machineId
	 * @param homeId
	 * @param useJsonString
	 * @return
	 * @throws ClientException
	 */
	public Map<String, Object> getProperties(String machineId, String homeId, boolean useJsonString) throws ClientException {
		Map<String, Object> properties = new LinkedHashMap<String, Object>();
		Response response = null;
		try {
			WebTarget webTarget = getRootPath().path(machineId).path("homes").path(homeId).path("properties");
			if (useJsonString) {
				webTarget = webTarget.queryParam("useJsonString", useJsonString);
			}
			Builder builder = webTarget.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			if (useJsonString) {
				String propertiesString = response.readEntity(String.class);
				properties = JSONUtil.toProperties(propertiesString);
			} else {
				properties = response.readEntity(new GenericType<Map<String, Object>>() {
				});
			}

		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return properties;
	}

	/**
	 * Set Home properties.
	 * 
	 * @param machineId
	 * @param homeId
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO setProperties(String machineId, String homeId, Map<String, Object> properties) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			String propertiesString = JSONUtil.toJsonString(properties);

			Builder builder = getRootPath().path(machineId).path("homes").path(homeId).path("properties").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(propertiesString));
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
	 * Remove Home properties.
	 * 
	 * @param machineId
	 * @param homeId
	 * @param propertyNames
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO removeProperties(String machineId, String homeId, List<String> propertyNames) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget webTarget = getRootPath().path(machineId).path("homes").path(homeId).path("properties");
			for (String propertyName : propertyNames) {
				webTarget = webTarget.queryParam("propertyName", propertyName);
			}

			Builder builder = webTarget.request(MediaType.APPLICATION_JSON);
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

}
