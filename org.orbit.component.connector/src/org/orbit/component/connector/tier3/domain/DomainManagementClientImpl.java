package org.orbit.component.connector.tier3.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.NodeConfig;
import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.connector.util.ModelConverter;
import org.orbit.component.model.tier3.domain.AddMachineConfigRequest;
import org.orbit.component.model.tier3.domain.AddNodeConfigRequest;
import org.orbit.component.model.tier3.domain.AddPlatformConfigRequest;
import org.orbit.component.model.tier3.domain.MachineConfigDTO;
import org.orbit.component.model.tier3.domain.NodeConfigDTO;
import org.orbit.component.model.tier3.domain.PlatformConfigDTO;
import org.orbit.component.model.tier3.domain.UpdateMachineConfigRequest;
import org.orbit.component.model.tier3.domain.UpdateNodeConfigRequest;
import org.orbit.component.model.tier3.domain.UpdatePlatformConfigRequest;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.model.StatusDTO;

public class DomainManagementClientImpl extends ServiceClientImpl<DomainManagementClient, DomainManagementWSClient> implements DomainManagementClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public DomainManagementClientImpl(ServiceConnector<DomainManagementClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected DomainManagementWSClient createWSClient(Map<String, Object> properties) {
		String realm = (String) properties.get(OrbitConstants.REALM);
		String username = (String) properties.get(OrbitConstants.USERNAME);
		String fullUrl = (String) properties.get(OrbitConstants.URL);

		ClientConfiguration config = ClientConfiguration.create(realm, username, fullUrl);
		return new DomainManagementWSClient(config);
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
				machines.add(ModelConverter.Domain.toMachineConfig(machineDTO));
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
				machine = ModelConverter.Domain.toMachineConfig(machineDTO);
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
			MachineConfigDTO addMachineRequestDTO = ModelConverter.Domain.toDTO(addMachineRequest);
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
			MachineConfigDTO updateMachineRequestDTO = ModelConverter.Domain.toDTO(updateMachineRequest);
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
	// Platform management
	// ---------------------------------------------------------
	/**
	 * 
	 * @param platformId
	 * @throws ClientException
	 */
	protected void checkPlatformId(String platformId) throws ClientException {
		if (platformId == null || platformId.isEmpty()) {
			throw new ClientException(400, "platformId is empty.");
		}
	}

	@Override
	public PlatformConfig[] getPlatformConfigs(String machineId) throws ClientException {
		checkMachineId(machineId);

		List<PlatformConfig> platformConfigs = new ArrayList<PlatformConfig>();
		try {
			List<PlatformConfigDTO> platformConfigDTOs = this.client.getPlatformConfigs(machineId);
			for (PlatformConfigDTO platformConfigDTO : platformConfigDTOs) {
				platformConfigs.add(ModelConverter.Domain.toPlatformConfig(machineId, platformConfigDTO));
			}
		} catch (ClientException e) {
			throw e;
		}
		return platformConfigs.toArray(new PlatformConfig[platformConfigs.size()]);
	}

	@Override
	public PlatformConfig getPlatformConfig(String machineId, String platformId) throws ClientException {
		checkMachineId(machineId);
		checkPlatformId(platformId);

		PlatformConfig platformConfig = null;
		try {
			PlatformConfigDTO platformConfigDTO = this.client.getPlatformConfig(machineId, platformId);
			if (platformConfigDTO != null) {
				platformConfig = ModelConverter.Domain.toPlatformConfig(machineId, platformConfigDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return platformConfig;
	}

	@Override
	public boolean addPlatformConfig(String machineId, AddPlatformConfigRequest addPlatformConfigRequest) throws ClientException {
		checkMachineId(machineId);
		String platformId = addPlatformConfigRequest.getPlatformId();
		checkPlatformId(platformId);

		try {
			PlatformConfigDTO addPlatformConfigDTO = ModelConverter.Domain.toDTO(addPlatformConfigRequest);
			StatusDTO status = this.client.addPlatformConfig(machineId, addPlatformConfigDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean updatPlatformConfig(String machineId, UpdatePlatformConfigRequest updatePlatformConfigRequest) throws ClientException {
		checkMachineId(machineId);
		String platformId = updatePlatformConfigRequest.getPlatformId();
		checkPlatformId(platformId);

		try {
			PlatformConfigDTO updatePlatformConfigDTO = ModelConverter.Domain.toDTO(updatePlatformConfigRequest);
			StatusDTO status = this.client.updatePlatformConfig(machineId, updatePlatformConfigDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean removePlatformConfig(String machineId, String platformId) throws ClientException {
		checkMachineId(machineId);
		checkPlatformId(platformId);

		try {
			StatusDTO status = this.client.removePlatformConfig(machineId, platformId);
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
	public NodeConfig[] getNodeConfigs(String machineId, String platformId) throws ClientException {
		checkMachineId(machineId);
		checkPlatformId(platformId);

		List<NodeConfig> nodeConfigs = new ArrayList<NodeConfig>();
		try {
			List<NodeConfigDTO> nodeConfigDTOs = this.client.getNodes(machineId, platformId);
			for (NodeConfigDTO nodeConfigDTO : nodeConfigDTOs) {
				nodeConfigs.add(ModelConverter.Domain.toNodeConfig(machineId, platformId, nodeConfigDTO));
			}
		} catch (ClientException e) {
			throw e;
		}
		return nodeConfigs.toArray(new NodeConfig[nodeConfigs.size()]);
	}

	@Override
	public NodeConfig getNodeConfig(String machineId, String platformId, String nodeId) throws ClientException {
		checkMachineId(machineId);
		checkPlatformId(platformId);
		checkNodeId(nodeId);

		NodeConfig nodeConfig = null;
		try {
			NodeConfigDTO nodeConfigDTO = this.client.getNode(machineId, platformId, nodeId);
			if (nodeConfigDTO != null) {
				nodeConfig = ModelConverter.Domain.toNodeConfig(machineId, platformId, nodeConfigDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return nodeConfig;
	}

	@Override
	public boolean addNodeConfig(String machineId, String platformId, AddNodeConfigRequest addNodeRequest) throws ClientException {
		checkMachineId(machineId);
		checkPlatformId(platformId);
		String nodeId = addNodeRequest.getNodeId();
		checkNodeId(nodeId);

		try {
			NodeConfigDTO addNodeRequestDTO = ModelConverter.Domain.toDTO(addNodeRequest);
			StatusDTO status = this.client.addNode(machineId, platformId, addNodeRequestDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean updateNodeConfig(String machineId, String platformId, UpdateNodeConfigRequest updateNodeRequest) throws ClientException {
		checkMachineId(machineId);
		checkPlatformId(platformId);
		String nodeId = updateNodeRequest.getNodeId();
		checkNodeId(nodeId);

		try {
			NodeConfigDTO updateNodeRequestDTO = ModelConverter.Domain.toDTO(updateNodeRequest);
			StatusDTO status = this.client.updateNode(machineId, platformId, updateNodeRequestDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean removeNodeConfig(String machineId, String platformId, String nodeId) throws ClientException {
		checkMachineId(machineId);
		checkPlatformId(platformId);
		checkNodeId(nodeId);

		try {
			StatusDTO status = this.client.removeNode(machineId, platformId, nodeId);
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
// public String getURL() {
// String fullUrl = (String) this.properties.get(OrbitConstants.URL);
// return fullUrl;
// }

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
