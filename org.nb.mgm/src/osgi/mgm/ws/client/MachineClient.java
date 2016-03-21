package osgi.mgm.ws.client;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import osgi.mgm.common.util.ClientConfiguration;
import osgi.mgm.common.util.ClientException;
import osgi.mgm.ws.dto.MachineDTO;
import osgi.mgm.ws.dto.StatusDTO;

/**
 * Machine web service client
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines?filter={filter}
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
public class MachineClient extends AbstractMgmClient {

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
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<MachineDTO> getMachines() throws ClientException {
		List<MachineDTO> machines = null;
		try {
			Builder builder = getRootPath().path("machines").request(MediaType.APPLICATION_JSON);
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
	 * Get Machines by filter.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines?filter={filter}
	 * 
	 * @param filter
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<MachineDTO> getMachines(String filter) throws ClientException {
		List<MachineDTO> machines = null;
		try {
			Builder builder = getRootPath().path("machines").queryParam("filter", filter).request(MediaType.APPLICATION_JSON);
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
