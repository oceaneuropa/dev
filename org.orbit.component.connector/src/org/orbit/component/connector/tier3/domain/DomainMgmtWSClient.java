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
import org.orbit.component.model.tier3.domain.NodeConfigDTO;
import org.orbit.component.model.tier3.domain.TransferAgentConfigDTO;
import org.origin.common.rest.agent.CommonWSClient;
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
 * TransferAgents
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents (Body parameter: TransferAgentConfigDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents (Body parameter: TransferAgentConfigDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}
 * 
 * Nodes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes/{nodeId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes (Body parameter: NodeConfigDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes (Body parameter: NodeConfigDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes/{nodeId}
 * 
 * @see TransferAgentWSClient
 * 
 */
public class DomainMgmtWSClient extends CommonWSClient {

	public static String PATH_MACHINES = "machines";
	public static String PATH_TRANSFER_AGENTS = "transferagents";
	public static String PATH_NODES = "nodes";

	/**
	 * 
	 * @param config
	 */
	public DomainMgmtWSClient(ClientConfiguration config) {
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

	// ------------------------------------------------------
	// TransferAgent management
	// ------------------------------------------------------
	/**
	 * Get transfer agent configurations.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents
	 * 
	 * @param machineId
	 * @return
	 * @throws ClientException
	 */
	public List<TransferAgentConfigDTO> getTransferAgents(String machineId) throws ClientException {
		List<TransferAgentConfigDTO> transferAgents = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(PATH_MACHINES).path(machineId).path(PATH_TRANSFER_AGENTS);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			transferAgents = response.readEntity(new GenericType<List<TransferAgentConfigDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		if (transferAgents == null) {
			transferAgents = Collections.emptyList();
		}
		return transferAgents;
	}

	/**
	 * Get a transfer agent configuration.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @return
	 * @throws ClientException
	 */
	public TransferAgentConfigDTO getTransferAgent(String machineId, String transferAgentId) throws ClientException {
		TransferAgentConfigDTO transferAgent = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_MACHINES).path(machineId).path(PATH_TRANSFER_AGENTS).path(transferAgentId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			transferAgent = response.readEntity(TransferAgentConfigDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		return transferAgent;
	}

	/**
	 * Add a transfer agent configuration.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents (Body parameter: TransferAgentConfigDTO)
	 * 
	 * @param machineId
	 * @param addTransferAgentRequestDTO
	 *            Body parameter for adding a transfer agent.
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO addTransferAgent(String machineId, TransferAgentConfigDTO addTransferAgentRequestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_MACHINES).path(machineId).path(PATH_TRANSFER_AGENTS).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<TransferAgentConfigDTO>(addTransferAgentRequestDTO) {
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
	 * Update a transfer agent configuration.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents (Body parameter: TransferAgentConfigDTO)
	 * 
	 * @param machineId
	 * @param updateTransferAgentRequestDTO
	 *            Body parameter for updating a transfer agent.
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateTransferAgent(String machineId, TransferAgentConfigDTO updateTransferAgentRequestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_MACHINES).path(machineId).path(PATH_TRANSFER_AGENTS).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<TransferAgentConfigDTO>(updateTransferAgentRequestDTO) {
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
	 * Remove a transfer agent configuration.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO removeTransferAgent(String machineId, String transferAgentId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_MACHINES).path(machineId).path(PATH_TRANSFER_AGENTS).path(transferAgentId).request(MediaType.APPLICATION_JSON);
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

	// ------------------------------------------------------
	// Node management
	// ------------------------------------------------------
	/**
	 * Get node configurations.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @return
	 * @throws ClientException
	 */
	public List<NodeConfigDTO> getNodes(String machineId, String transferAgentId) throws ClientException {
		List<NodeConfigDTO> nodeConfigs = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(PATH_MACHINES).path(machineId).path(PATH_TRANSFER_AGENTS).path(transferAgentId).path(PATH_NODES);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			nodeConfigs = response.readEntity(new GenericType<List<NodeConfigDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		if (nodeConfigs == null) {
			nodeConfigs = Collections.emptyList();
		}
		return nodeConfigs;
	}

	/**
	 * Get a node configuration.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes/{nodeId}
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param nodeId
	 * @return
	 * @throws ClientException
	 */
	public NodeConfigDTO getNode(String machineId, String transferAgentId, String nodeId) throws ClientException {
		NodeConfigDTO nodeConfigDTO = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_MACHINES).path(machineId).path(PATH_TRANSFER_AGENTS).path(transferAgentId).path(PATH_NODES).path(nodeId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			nodeConfigDTO = response.readEntity(NodeConfigDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		return nodeConfigDTO;
	}

	/**
	 * Add a node configuration.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes (Body parameter: NodeConfigDTO)
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param addNodeRequestDTO
	 *            Body parameter for adding a node.
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO addNode(String machineId, String transferAgentId, NodeConfigDTO addNodeRequestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_MACHINES).path(machineId).path(PATH_TRANSFER_AGENTS).path(transferAgentId).path(PATH_NODES).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<NodeConfigDTO>(addNodeRequestDTO) {
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
	 * Update a node configuration.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes (Body parameter: NodeConfigDTO)
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param updateNodeRequestDTO
	 *            Body parameter for updating a node.
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateNode(String machineId, String transferAgentId, NodeConfigDTO updateNodeRequestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_MACHINES).path(machineId).path(PATH_TRANSFER_AGENTS).path(transferAgentId).path(PATH_NODES).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<NodeConfigDTO>(updateNodeRequestDTO) {
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
	 * Remove a node configuration.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes/{nodeId}
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param nodeId
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO removeNode(String machineId, String transferAgentId, String nodeId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_MACHINES).path(machineId).path(PATH_TRANSFER_AGENTS).path(transferAgentId).path(PATH_NODES).path(nodeId).request(MediaType.APPLICATION_JSON);
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
