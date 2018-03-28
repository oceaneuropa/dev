package org.orbit.component.connector.tier3.domainmanagement;

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
import org.orbit.component.model.tier3.domain.NodeConfigDTO;
import org.orbit.component.model.tier3.domain.PlatformConfigDTO;
import org.origin.common.rest.client.AbstractWSClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.util.ResponseUtil;

/*
 * DomainService Machines resource client.
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
 * Platforms
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms (Body parameter: PlatformConfigDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms (Body parameter: PlatformConfigDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}
 * 
 * Nodes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes/{nodeId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes (Body parameter: NodeConfigDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes (Body parameter: NodeConfigDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes/{nodeId}
 * 
 * @see TransferAgentWSClient
 * 
 */
public class DomainManagementWSClient extends AbstractWSClient {

	public static String MACHINES = "machines";
	public static String PLATFORMS = "platforms";
	public static String NODES = "nodes";

	public DomainManagementWSClient(ClientConfiguration config) {
		super(config);
	}

	// ------------------------------------------------------
	// Machine management
	// ------------------------------------------------------
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
			WebTarget target = getRootPath().path(MACHINES);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			machines = response.readEntity(new GenericType<List<MachineConfigDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
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
			WebTarget target = getRootPath().path(MACHINES).path(machineId);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			machine = response.readEntity(MachineConfigDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
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
			WebTarget target = getRootPath().path(MACHINES);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<MachineConfigDTO>(addMachineRequestDTO) {
			}));
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
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
			WebTarget target = getRootPath().path(MACHINES);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<MachineConfigDTO>(updateMachineRequestDTO) {
			}));
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Remove a machine configuration.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}
	 * 
	 * @param machineId
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO removeMachine(String machineId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(MACHINES).path(machineId);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	// ------------------------------------------------------
	// Platform management
	// ------------------------------------------------------
	/**
	 * Get platform configurations.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms
	 * 
	 * @param machineId
	 * @return
	 * @throws ClientException
	 */
	public List<PlatformConfigDTO> getPlatformConfigs(String machineId) throws ClientException {
		List<PlatformConfigDTO> platformConfigDTOs = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(MACHINES).path(machineId).path(PLATFORMS);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			platformConfigDTOs = response.readEntity(new GenericType<List<PlatformConfigDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (platformConfigDTOs == null) {
			platformConfigDTOs = Collections.emptyList();
		}
		return platformConfigDTOs;
	}

	/**
	 * Get a platform configuration.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}
	 * 
	 * @param machineId
	 * @param platformId
	 * @return
	 * @throws ClientException
	 */
	public PlatformConfigDTO getPlatformConfig(String machineId, String platformId) throws ClientException {
		PlatformConfigDTO platformConfigDTO = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(MACHINES).path(machineId).path(PLATFORMS).path(platformId);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			platformConfigDTO = response.readEntity(PlatformConfigDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return platformConfigDTO;
	}

	/**
	 * Add a platform configuration.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms (Body parameter: PlatformConfigDTO)
	 * 
	 * @param machineId
	 * @param addPlatformConfigDTO
	 *            Body parameter for adding a platform.
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO addPlatformConfig(String machineId, PlatformConfigDTO addPlatformConfigDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(MACHINES).path(machineId).path(PLATFORMS);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<PlatformConfigDTO>(addPlatformConfigDTO) {
			}));
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Update a platform configuration.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms (Body parameter: PlatformConfigDTO)
	 * 
	 * @param machineId
	 * @param updatePlatformRequestDTO
	 *            Body parameter for updating a platform config.
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updatePlatformConfig(String machineId, PlatformConfigDTO updatePlatformRequestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(MACHINES).path(machineId).path(PLATFORMS);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<PlatformConfigDTO>(updatePlatformRequestDTO) {
			}));
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Remove a platform configuration.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}
	 * 
	 * @param machineId
	 * @param platformId
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO removePlatformConfig(String machineId, String platformId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(MACHINES).path(machineId).path(PLATFORMS).path(platformId);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	// ------------------------------------------------------
	// Node management
	// ------------------------------------------------------
	/**
	 * Get node configurations.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes
	 * 
	 * @param machineId
	 * @param platformId
	 * @return
	 * @throws ClientException
	 */
	public List<NodeConfigDTO> getNodes(String machineId, String platformId) throws ClientException {
		List<NodeConfigDTO> nodeConfigs = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(MACHINES).path(machineId).path(PLATFORMS).path(platformId).path(NODES);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			nodeConfigs = response.readEntity(new GenericType<List<NodeConfigDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (nodeConfigs == null) {
			nodeConfigs = Collections.emptyList();
		}
		return nodeConfigs;
	}

	/**
	 * Get a node configuration.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes/{nodeId}
	 * 
	 * @param machineId
	 * @param platformId
	 * @param nodeId
	 * @return
	 * @throws ClientException
	 */
	public NodeConfigDTO getNode(String machineId, String platformId, String nodeId) throws ClientException {
		NodeConfigDTO nodeConfigDTO = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(MACHINES).path(machineId).path(PLATFORMS).path(platformId).path(NODES).path(nodeId);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			nodeConfigDTO = response.readEntity(NodeConfigDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return nodeConfigDTO;
	}

	/**
	 * Add a node configuration.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes (Body parameter: NodeConfigDTO)
	 * 
	 * @param machineId
	 * @param platformId
	 * @param addNodeRequestDTO
	 *            Body parameter for adding a node.
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO addNode(String machineId, String platformId, NodeConfigDTO addNodeRequestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(MACHINES).path(machineId).path(PLATFORMS).path(platformId).path(NODES);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<NodeConfigDTO>(addNodeRequestDTO) {
			}));
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Update a node configuration.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes (Body parameter: NodeConfigDTO)
	 * 
	 * @param machineId
	 * @param platformId
	 * @param updateNodeRequestDTO
	 *            Body parameter for updating a node.
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateNode(String machineId, String platformId, NodeConfigDTO updateNodeRequestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(MACHINES).path(machineId).path(PLATFORMS).path(platformId).path(NODES);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<NodeConfigDTO>(updateNodeRequestDTO) {
			}));
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Remove a node configuration.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes/{nodeId}
	 * 
	 * @param machineId
	 * @param platformId
	 * @param nodeId
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO removeNode(String machineId, String platformId, String nodeId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(MACHINES).path(machineId).path(PLATFORMS).path(platformId).path(NODES).path(nodeId);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

}
