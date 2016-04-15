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

import org.nb.mgm.client.util.ClientConfiguration;
import org.nb.mgm.client.util.ClientException;

import osgi.mgm.ws.dto.HomeDTO;
import osgi.mgm.ws.dto.StatusDTO;

/**
 * Home web service client
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes?name={name}&url={url}&status={status}&filter={filter}
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}
 * 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes (Body parameter: HomeDTO)
 * 
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes (Body parameter: HomeDTO)
 * 
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}
 * 
 */
public class HomeClient extends AbstractMgmClient {

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
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes?name={name}&url={url}&status={status}&filter={filter}
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
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes?name={name}&url={url}&status={status}&filter={filter}
	 * 
	 * @param machineId
	 * @param properties
	 *            supported keys are: "name", "url", "status", "filter".
	 * @return
	 * @throws ClientException
	 */
	public List<HomeDTO> getHomes(String machineId, Properties properties) throws ClientException {
		List<HomeDTO> homes = null;
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
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			homes = response.readEntity(new GenericType<List<HomeDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		}
		if (homes == null) {
			homes = Collections.emptyList();
		}
		return homes;
	}

	/**
	 * Get Home by machine Id and home Id.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}
	 * 
	 * @param machineId
	 * @param homeId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public HomeDTO getHome(String machineId, String homeId) throws ClientException {
		HomeDTO home = null;
		try {
			Builder builder = getRootPath().path(machineId).path("homes").path(homeId).request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			home = response.readEntity(HomeDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return home;
	}

	/**
	 * Add a Home to a Machine.
	 * 
	 * Request URL (POST): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes
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
		try {
			Builder builder = getRootPath().path(machineId).path("homes").request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(Entity.json(new GenericEntity<HomeDTO>(home) {
			}));
			checkResponse(response);

			newHome = response.readEntity(HomeDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return newHome;
	}

	/**
	 * Update Home information.
	 * 
	 * Request URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes
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
		try {
			Builder builder = getRootPath().path(machineId).path("homes").request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).put(Entity.json(new GenericEntity<HomeDTO>(home) {
			}));
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return status;
	}

	/**
	 * Delete a Home from a Machine by home Id.
	 * 
	 * Request URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{machineId}/homes/{homeId}
	 * 
	 * @param machineId
	 * @param homeId
	 * 
	 * @return Delete status
	 * @throws ClientException
	 */
	public StatusDTO deleteHome(String machineId, String homeId) throws ClientException {
		StatusDTO status = null;
		try {
			Builder builder = getRootPath().path(machineId).path("homes").path(homeId).request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).delete();
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return status;
	}

}
