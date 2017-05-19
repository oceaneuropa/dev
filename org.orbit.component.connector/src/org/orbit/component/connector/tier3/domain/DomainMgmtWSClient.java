package org.orbit.component.connector.tier3.domain;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.model.tier3.domain.MachineConfigDTO;
import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ClientUtil;
import org.origin.common.rest.model.StatusDTO;

/*
 * DomainManagement Machines resource client.
 * 
 * {contextRoot} example:
 * /orbit/v1/domain
 * 
 * Machines
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines (Body parameter: MachineConfigDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines (Body parameter: MachineConfigDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
 * 
 */
public class DomainMgmtWSClient extends AbstractClient {

	public static String PATH_MACHINES = "machines";

	/**
	 * 
	 * @param config
	 */
	public DomainMgmtWSClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * Get machine configurations.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<MachineConfigDTO> getMachines() throws ClientException {
		List<MachineConfigDTO> machines = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(PATH_MACHINES);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			machines = response.readEntity(new GenericType<List<MachineConfigDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		if (machines == null) {
			machines = Collections.emptyList();
		}
		return machines;
	}

	/**
	 * Get a machine configuration.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
	 * 
	 * @param machineId
	 * @return
	 * @throws ClientException
	 */
	public MachineConfigDTO getMachine(String machineId) throws ClientException {
		MachineConfigDTO machine = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_MACHINES).path(machineId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			machine = response.readEntity(MachineConfigDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		return machine;
	}

	/**
	 * Add a machine configuration.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines (Body parameter: MachineConfigDTO)
	 * 
	 * @param addMachineRequestDTO
	 *            Body parameter for adding a machine.
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO addMachine(MachineConfigDTO addMachineRequestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_MACHINES).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<MachineConfigDTO>(addMachineRequestDTO) {
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
	 * Update a machine configuration.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines (Body parameter: MachineConfigDTO)
	 * 
	 * @param updateMachineRequestDTO
	 *            Body parameter for updating a machine.
	 * 
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateMachine(MachineConfigDTO updateMachineRequestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_MACHINES).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<MachineConfigDTO>(updateMachineRequestDTO) {
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
	 * Remove a machine configuration.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
	 * 
	 * @param machineId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO removeMachine(String machineId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_MACHINES).path(machineId).request(MediaType.APPLICATION_JSON);
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
