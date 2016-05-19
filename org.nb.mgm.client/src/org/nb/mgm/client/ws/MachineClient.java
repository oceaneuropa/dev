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

import org.nb.mgm.model.dto.MachineDTO;
import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;

/**
 * Machine web service client
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines?name={name}&ipaddress={ipaddress}&filter={filter}
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
 * 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/machines (Body parameter: MachineDTO)
 * 
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines (Body parameter: MachineDTO)
 * 
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
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
	public List<MachineDTO> getMachines(Properties properties) throws ClientException {
		List<MachineDTO> machines = null;
		try {
			WebTarget target = getRootPath().path("machines");
			if (properties != null) {
				String name = properties.getProperty("name");
				if (name != null) {
					target.queryParam("name", name);
				}
				String ipaddress = properties.getProperty("ipaddress");
				if (ipaddress != null) {
					target.queryParam("ipaddress", ipaddress);
				}
				String filter = properties.getProperty("filter");
				if (filter != null) {
					target.queryParam("filter", filter);
				}
			}
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			machines = response.readEntity(new GenericType<List<MachineDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
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
		try {
			Builder builder = getRootPath().path("machines").path(machineId).request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			machine = response.readEntity(MachineDTO.class);
		} catch (ClientException e) {
			handleException(e);
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
		try {
			Builder builder = getRootPath().path("machines").request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(Entity.json(new GenericEntity<MachineDTO>(machine) {
			}));
			checkResponse(response);

			newMachine = response.readEntity(MachineDTO.class);
		} catch (ClientException e) {
			handleException(e);
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
		try {
			Builder builder = getRootPath().path("machines").request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).put(Entity.json(new GenericEntity<MachineDTO>(machine) {
			}));
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
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
		try {
			Builder builder = getRootPath().path("machines").path(machineId).request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).delete();
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return status;
	}

}
