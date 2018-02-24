package org.orbit.component.connector.tier3.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.model.tier3.domain.dto.MachineConfig;
import org.orbit.component.model.tier3.domain.dto.MachineConfigDTO;
import org.orbit.component.model.tier3.domain.dto.ModelConverter;
import org.orbit.component.model.tier3.domain.dto.NodeConfig;
import org.orbit.component.model.tier3.domain.dto.NodeConfigDTO;
import org.orbit.component.model.tier3.domain.dto.TransferAgentConfig;
import org.orbit.component.model.tier3.domain.dto.TransferAgentConfigDTO;
import org.orbit.component.model.tier3.domain.request.AddMachineConfigRequest;
import org.orbit.component.model.tier3.domain.request.AddNodeConfigRequest;
import org.orbit.component.model.tier3.domain.request.AddTransferAgentConfigRequest;
import org.orbit.component.model.tier3.domain.request.UpdateMachineConfigRequest;
import org.orbit.component.model.tier3.domain.request.UpdateNodeConfigRequest;
import org.orbit.component.model.tier3.domain.request.UpdateTransferAgentConfigRequest;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.model.StatusDTO;

public class DomainServiceClientImpl extends ServiceClientImpl<DomainManagementClient, DomainServiceWSClient> implements DomainManagementClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public DomainServiceClientImpl(ServiceConnector<DomainManagementClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected DomainServiceWSClient createWSClient(Map<String, Object> properties) {
		String realm = (String) properties.get(OrbitConstants.REALM);
		String username = (String) properties.get(OrbitConstants.USERNAME);
		String fullUrl = (String) properties.get(OrbitConstants.URL);

		ClientConfiguration config = ClientConfiguration.create(realm, username, fullUrl);
		return new DomainServiceWSClient(config);
	}

	@Override
	public String getURL() {
		String fullUrl = (String) this.properties.get(OrbitConstants.URL);
		return fullUrl;
	}

	// ---------------------------------------------------------
	// Machine management
	// ---------------------------------------------------------
	/**
	 * 
	 * @param machineId
	 * @throws ClientException
	 */
	protected void checkMachineId(String machineId) throws ClientException {
		if (machineId == null || machineId.isEmpty()) {
			throw new ClientException(400, "machineId is empty.");
		}
	}

	@Override
	public MachineConfig[] getMachineConfigs() throws ClientException {
		List<MachineConfig> machines = new ArrayList<MachineConfig>();
		try {
			List<MachineConfigDTO> machineDTOs = this.client.getMachines();
			for (MachineConfigDTO machineDTO : machineDTOs) {
				machines.add(ModelConverter.INSTANCE.toMachineConfig(machineDTO));
			}
		} catch (ClientException e) {
			throw e;
		}
		return machines.toArray(new MachineConfig[machines.size()]);
	}

	@Override
	public MachineConfig getMachineConfig(String machineId) throws ClientException {
		checkMachineId(machineId);

		MachineConfig machine = null;
		try {
			MachineConfigDTO machineDTO = this.client.getMachine(machineId);
			if (machineDTO != null) {
				machine = ModelConverter.INSTANCE.toMachineConfig(machineDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return machine;
	}

	@Override
	public boolean addMachineConfig(AddMachineConfigRequest addMachineRequest) throws ClientException {
		String machineId = addMachineRequest.getMachineId();
		checkMachineId(machineId);

		try {
			MachineConfigDTO addMachineRequestDTO = ModelConverter.INSTANCE.toDTO(addMachineRequest);
			StatusDTO status = this.client.addMachine(addMachineRequestDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean updateMachineConfig(UpdateMachineConfigRequest updateMachineRequest) throws ClientException {
		String machineId = updateMachineRequest.getMachineId();
		checkMachineId(machineId);

		try {
			MachineConfigDTO updateMachineRequestDTO = ModelConverter.INSTANCE.toDTO(updateMachineRequest);
			StatusDTO status = this.client.updateMachine(updateMachineRequestDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean removeMachineConfig(String machineId) throws ClientException {
		checkMachineId(machineId);

		try {
			StatusDTO status = this.client.removeMachine(machineId);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	// ---------------------------------------------------------
	// TransferAgent management
	// ---------------------------------------------------------
	/**
	 * 
	 * @param transferAgentId
	 * @throws ClientException
	 */
	protected void checkTransferAgentId(String transferAgentId) throws ClientException {
		if (transferAgentId == null || transferAgentId.isEmpty()) {
			throw new ClientException(400, "transferAgentId is empty.");
		}
	}

	@Override
	public TransferAgentConfig[] getTransferAgentConfigs(String machineId) throws ClientException {
		checkMachineId(machineId);

		List<TransferAgentConfig> transferAgents = new ArrayList<TransferAgentConfig>();
		try {
			List<TransferAgentConfigDTO> transferAgentDTOs = this.client.getTransferAgents(machineId);
			for (TransferAgentConfigDTO transferAgentDTO : transferAgentDTOs) {
				transferAgents.add(ModelConverter.INSTANCE.toTransferAgentConfig(machineId, transferAgentDTO));
			}
		} catch (ClientException e) {
			throw e;
		}
		return transferAgents.toArray(new TransferAgentConfig[transferAgents.size()]);
	}

	@Override
	public TransferAgentConfig getTransferAgentConfig(String machineId, String transferAgentId) throws ClientException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);

		TransferAgentConfig transferAgent = null;
		try {
			TransferAgentConfigDTO transferAgentDTO = this.client.getTransferAgent(machineId, transferAgentId);
			if (transferAgentDTO != null) {
				transferAgent = ModelConverter.INSTANCE.toTransferAgentConfig(machineId, transferAgentDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return transferAgent;
	}

	@Override
	public boolean addTransferAgentConfig(String machineId, AddTransferAgentConfigRequest addTransferAgentRequest) throws ClientException {
		checkMachineId(machineId);
		String transferAgentId = addTransferAgentRequest.getTransferAgentId();
		checkTransferAgentId(transferAgentId);

		try {
			TransferAgentConfigDTO addTransferAgentRequestDTO = ModelConverter.INSTANCE.toDTO(addTransferAgentRequest);
			StatusDTO status = this.client.addTransferAgent(machineId, addTransferAgentRequestDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean updateTransferAgentConfig(String machineId, UpdateTransferAgentConfigRequest updateTransferAgentRequest) throws ClientException {
		checkMachineId(machineId);
		String transferAgentId = updateTransferAgentRequest.getTransferAgentId();
		checkTransferAgentId(transferAgentId);

		try {
			TransferAgentConfigDTO updateTransferAgentRequestDTO = ModelConverter.INSTANCE.toDTO(updateTransferAgentRequest);
			StatusDTO status = this.client.updateTransferAgent(machineId, updateTransferAgentRequestDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean removeTransferAgentConfig(String machineId, String transferAgentId) throws ClientException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);

		try {
			StatusDTO status = this.client.removeTransferAgent(machineId, transferAgentId);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	// ---------------------------------------------------------
	// Node management
	// ---------------------------------------------------------
	/**
	 * 
	 * @param nodeId
	 * @throws ClientException
	 */
	protected void checkNodeId(String nodeId) throws ClientException {
		if (nodeId == null || nodeId.isEmpty()) {
			throw new ClientException(400, "nodeId is empty.");
		}
	}

	@Override
	public NodeConfig[] getNodeConfigs(String machineId, String transferAgentId) throws ClientException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);

		List<NodeConfig> nodeConfigs = new ArrayList<NodeConfig>();
		try {
			List<NodeConfigDTO> nodeConfigDTOs = this.client.getNodes(machineId, transferAgentId);
			for (NodeConfigDTO nodeConfigDTO : nodeConfigDTOs) {
				nodeConfigs.add(ModelConverter.INSTANCE.toNodeConfig(machineId, transferAgentId, nodeConfigDTO));
			}
		} catch (ClientException e) {
			throw e;
		}
		return nodeConfigs.toArray(new NodeConfig[nodeConfigs.size()]);
	}

	@Override
	public NodeConfig getNodeConfig(String machineId, String transferAgentId, String nodeId) throws ClientException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);
		checkNodeId(nodeId);

		NodeConfig nodeConfig = null;
		try {
			NodeConfigDTO nodeConfigDTO = this.client.getNode(machineId, transferAgentId, nodeId);
			if (nodeConfigDTO != null) {
				nodeConfig = ModelConverter.INSTANCE.toNodeConfig(machineId, transferAgentId, nodeConfigDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return nodeConfig;
	}

	@Override
	public boolean addNodeConfig(String machineId, String transferAgentId, AddNodeConfigRequest addNodeRequest) throws ClientException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);
		String nodeId = addNodeRequest.getNodeId();
		checkNodeId(nodeId);

		try {
			NodeConfigDTO addNodeRequestDTO = ModelConverter.INSTANCE.toDTO(addNodeRequest);
			StatusDTO status = this.client.addNode(machineId, transferAgentId, addNodeRequestDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean updateNodeConfig(String machineId, String transferAgentId, UpdateNodeConfigRequest updateNodeRequest) throws ClientException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);
		String nodeId = updateNodeRequest.getNodeId();
		checkNodeId(nodeId);

		try {
			NodeConfigDTO updateNodeRequestDTO = ModelConverter.INSTANCE.toDTO(updateNodeRequest);
			StatusDTO status = this.client.updateNode(machineId, transferAgentId, updateNodeRequestDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean removeNodeConfig(String machineId, String transferAgentId, String nodeId) throws ClientException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);
		checkNodeId(nodeId);

		try {
			StatusDTO status = this.client.removeNode(machineId, transferAgentId, nodeId);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

}

// @Override
// public String getName() {
// String name = (String) this.properties.get(OrbitConstants.DOMAIN_SERVICE_NAME);
// return name;
// }

// ------------------------------------------------------
// Life cycle
// ------------------------------------------------------
// protected Map<String, TransferAgent> transferAgentMap = new LinkedHashMap<String, TransferAgent>();
//
// @Override
// public TransferAgent getTransferAgent(TransferAgentConfig taConfig) throws ClientException {
// String machineId = taConfig.getMachineId();
// String transferAgentId = taConfig.getId();
//
// String key = machineId + "#" + transferAgentId;
//
// TransferAgent transferAgent = this.transferAgentMap.get(key);
// if (transferAgent == null) {
// String name = taConfig.getName();
// String hostURL = taConfig.getHostURL();
// String contextRoot = taConfig.getContextRoot();
//
// Map<String, Object> properties = new HashMap<String, Object>();
// properties.put(OrbitConstants.TRANSFER_AGENT_MACHINE_ID, machineId);
// properties.put(OrbitConstants.TRANSFER_AGENT_TRANSFER_AGENT_ID, transferAgentId);
// properties.put(OrbitConstants.TRANSFER_AGENT_NAME, name);
// properties.put(OrbitConstants.TRANSFER_AGENT_HOST_URL, hostURL);
// properties.put(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT, contextRoot);
//
// transferAgent = new TransferAgentImpl(properties);
// this.transferAgentMap.put(key, transferAgent);
// }
//
// return transferAgent;
// }

//// ---------------------------------------------------------
//// Helper methods
//// ---------------------------------------------------------
/// **
// * Get web service client configuration.
// *
// * @param properties
// * @return
// */
// protected ClientConfiguration getClientConfiguration(Map<String, Object> properties) {
// String realm = (String) properties.get(OrbitConstants.REALM);
// String username = (String) properties.get(OrbitConstants.USERNAME);
// String url = (String) properties.get(OrbitConstants.DOMAIN_SERVICE_HOST_URL);
// String contextRoot = (String) properties.get(OrbitConstants.DOMAIN_SERVICE_CONTEXT_ROOT);
// return ClientConfiguration.create(realm, username, url, contextRoot);
// }
