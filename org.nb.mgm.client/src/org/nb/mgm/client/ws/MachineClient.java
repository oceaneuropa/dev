package org.nb.mgm.client.ws;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.nb.mgm.model.dto.MachineDTO;
import org.origin.common.io.IOUtil;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.util.PropertyUtil;

/*
 * Machine web service client
 * 
 * Machine resource
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines?name={name}&ipaddress={ipaddress}&filter={filter} 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/machines (Body parameter: MachineDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines (Body parameter: MachineDTO)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
 * 
 * Machine properties resource
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/properties?useJsonString=false
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{machineId}/properties (Body parameter "properties": string)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{machineId}/properties?propertyName={propertyName1}&propertyName={propertyName2}
 * 
 */
public class MachineClient extends AbstractClient {

	/**
	 * 
	 * @param config
	 */
	public MachineClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * Get all Machines.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines?name={name}&ipaddress={ipaddress}&filter={filter}
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<MachineDTO> getMachines() throws ClientException {
		return getMachines(null);
	}

	/**
	 * Get Machines by query parameter.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines?name={name}&ipaddress={ipaddress}&filter={filter}
	 * 
	 * @param properties
	 *            supported keys are: "name", "ipaddress", "filter".
	 * @return
	 * @throws ClientException
	 */
	public List<MachineDTO> getMachines(Map<String, ?> properties) throws ClientException {
		List<MachineDTO> machines = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("machines");
			if (properties != null) {
				String name = PropertyUtil.getString(properties, "name", null);
				if (name != null) {
					target.queryParam("name", name);
				}
				String ipaddress = PropertyUtil.getString(properties, "ipaddress", null);
				if (ipaddress != null) {
					target.queryParam("ipaddress", ipaddress);
				}
				String filter = PropertyUtil.getString(properties, "filter", null);
				if (filter != null) {
					target.queryParam("filter", filter);
				}
			}
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			machines = response.readEntity(new GenericType<List<MachineDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		if (machines == null) {
			machines = Collections.emptyList();
		}
		return machines;
	}

	/**
	 * Get Machine by machine Id.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
	 * 
	 * @param machineId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public MachineDTO getMachine(String machineId) throws ClientException {
		MachineDTO machine = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("machines").path(machineId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			machine = response.readEntity(MachineDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return machine;
	}

	/**
	 * Add a Machine to the cluster.
	 * 
	 * Request URL (POST): {scheme}://{host}:{port}/{contextRoot}/machines
	 * 
	 * @param machine
	 *            Body parameter for the new Machine.
	 * 
	 * @return new Machine
	 * @throws ClientException
	 */
	public MachineDTO addMachine(MachineDTO machine) throws ClientException {
		MachineDTO newMachine = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("machines").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<MachineDTO>(machine) {
			}));
			checkResponse(response);

			newMachine = response.readEntity(MachineDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return newMachine;
	}

	/**
	 * Update Machine information.
	 * 
	 * Request URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines
	 * 
	 * @param machine
	 *            Body parameter for updating the Machine.
	 * 
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateMachine(MachineDTO machine) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("machines").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<MachineDTO>(machine) {
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
	 * Delete a Machine from the cluster.
	 * 
	 * Request URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
	 * 
	 * @param machineId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO deleteMachine(String machineId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("machines").path(machineId).request(MediaType.APPLICATION_JSON);
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
	 * Get Machine properties.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/{machineId}/properties?useJsonString=false
	 * 
	 * @param machineId
	 * @param useJsonString
	 * @return
	 * @throws ClientException
	 */
	public Map<String, Object> getProperties(String machineId, boolean useJsonString) throws ClientException {
		Map<String, Object> properties = new LinkedHashMap<String, Object>();
		Response response = null;
		try {
			WebTarget webTarget = getRootPath().path(machineId).path("properties");
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
	 * Set Machine properties.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{machineId}/properties (Body parameter "properties": string)
	 * 
	 * @param machineId
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO setProperties(String machineId, Map<String, Object> properties) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			String propertiesString = JSONUtil.toJsonString(properties);

			Builder builder = getRootPath().path(machineId).path("properties").request(MediaType.APPLICATION_JSON);
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
	 * Remove Machine properties.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{machineId}/properties?propertyName={propertyName1}&propertyName={propertyName2}
	 * 
	 * @param machineId
	 * @param propertyNames
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO removeProperties(String machineId, List<String> propertyNames) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget webTarget = getRootPath().path(machineId).path("properties");
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
