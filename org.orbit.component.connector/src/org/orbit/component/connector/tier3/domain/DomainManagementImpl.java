package org.orbit.component.connector.tier3.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier3.domain.DomainManagement;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.NodeConfig;
import org.orbit.component.api.tier3.domain.TransferAgentConfig;
import org.orbit.component.api.tier3.domain.request.AddMachineConfigRequest;
import org.orbit.component.api.tier3.domain.request.AddNodeConfigRequest;
import org.orbit.component.api.tier3.domain.request.AddTransferAgentConfigRequest;
import org.orbit.component.api.tier3.domain.request.UpdateMachineConfigRequest;
import org.orbit.component.api.tier3.domain.request.UpdateNodeConfigRequest;
import org.orbit.component.api.tier3.domain.request.UpdateTransferAgentConfigRequest;
import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.model.tier3.domain.MachineConfigDTO;
import org.orbit.component.model.tier3.domain.NodeConfigDTO;
import org.orbit.component.model.tier3.domain.TransferAgentConfigDTO;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.util.StringUtil;

public class DomainManagementImpl implements DomainManagement {

	protected Map<String, Object> properties;
	protected DomainMgmtWSClient client;

	/**
	 * 
	 * @param properties
	 */
	public DomainManagementImpl(Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		initClient();
	}

	// ------------------------------------------------------------------------------------------------
	// Configuration methods
	// ------------------------------------------------------------------------------------------------
	@Override
	public String getName() {
		String name = (String) this.properties.get(OrbitConstants.DOMAIN_MANAGEMENT_NAME);
		return name;
	}

	@Override
	public String getURL() {
		String hostURL = (String) this.properties.get(OrbitConstants.DOMAIN_MANAGEMENT_HOST_URL);
		String contextRoot = (String) this.properties.get(OrbitConstants.DOMAIN_MANAGEMENT_CONTEXT_ROOT);
		return hostURL + contextRoot;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	/**
	 * Update properties. Re-initiate web service client if host URL or context root is changed.
	 * 
	 * @param properties
	 */
	@Override
	public void update(Map<String, Object> properties) {
		String oldUrl = (String) this.properties.get(OrbitConstants.DOMAIN_MANAGEMENT_HOST_URL);
		String oldContextRoot = (String) this.properties.get(OrbitConstants.DOMAIN_MANAGEMENT_CONTEXT_ROOT);

		properties = checkProperties(properties);
		this.properties.putAll(properties);

		String newUrl = (String) properties.get(OrbitConstants.DOMAIN_MANAGEMENT_HOST_URL);
		String newContextRoot = (String) properties.get(OrbitConstants.DOMAIN_MANAGEMENT_CONTEXT_ROOT);

		boolean reinitClient = false;
		if (!StringUtil.equals(oldUrl, newUrl) || !StringUtil.equals(oldContextRoot, newContextRoot)) {
			reinitClient = true;
		}
		if (reinitClient) {
			initClient();
		}
	}

	private Map<String, Object> checkProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	protected void initClient() {
		ClientConfiguration clientConfig = getClientConfiguration(this.properties);
		this.client = new DomainMgmtWSClient(clientConfig);
	}

	// ------------------------------------------------------------------------------------------------
	// Web service methods
	// ------------------------------------------------------------------------------------------------
	@Override
	public int ping() throws ClientException {
		return this.client.ping();
	}

	// ------------------------------------------------------
	// Machine management
	// ------------------------------------------------------
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
				machines.add(toMachine(machineDTO));
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
				machine = toMachine(machineDTO);
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
			MachineConfigDTO addMachineRequestDTO = toDTO(addMachineRequest);
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
			MachineConfigDTO updateMachineRequestDTO = toDTO(updateMachineRequest);
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

	// ------------------------------------------------------
	// TransferAgent management
	// ------------------------------------------------------
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
				transferAgents.add(toTransferAgent(machineId, transferAgentDTO));
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
				transferAgent = toTransferAgent(machineId, transferAgentDTO);
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
			TransferAgentConfigDTO addTransferAgentRequestDTO = toDTO(addTransferAgentRequest);
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
			TransferAgentConfigDTO updateTransferAgentRequestDTO = toDTO(updateTransferAgentRequest);
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

	// ------------------------------------------------------
	// Node management
	// ------------------------------------------------------
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
				nodeConfigs.add(toNode(machineId, transferAgentId, nodeConfigDTO));
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
				nodeConfig = toNode(machineId, transferAgentId, nodeConfigDTO);
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
			NodeConfigDTO addNodeRequestDTO = toDTO(addNodeRequest);
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
			NodeConfigDTO updateNodeRequestDTO = toDTO(updateNodeRequest);
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

	// ------------------------------------------------------
	// Life cycle
	// ------------------------------------------------------
	protected Map<String, TransferAgent> transferAgentMap = new LinkedHashMap<String, TransferAgent>();

	@Override
	public TransferAgent getTransferAgent(String machineId, String transferAgentId) throws ClientException {
		String key = machineId + "#" + transferAgentId;
		TransferAgent transferAgent = transferAgentMap.get(key);
		if (transferAgent == null) {
			TransferAgentConfig transferAgentConfig = getTransferAgentConfig(machineId, transferAgentId);
			String hostURL = transferAgentConfig.getHostURL();
			String contextRoot = transferAgentConfig.getContextRoot();

			// Map<String, Object> properties
		}

		return transferAgent;
	}

	// ------------------------------------------------------------------------------------------------
	// Helper methods
	// ------------------------------------------------------------------------------------------------
	/**
	 * Get web service client configuration.
	 * 
	 * @param properties
	 * @return
	 */
	protected ClientConfiguration getClientConfiguration(Map<String, Object> properties) {
		String url = (String) properties.get(OrbitConstants.DOMAIN_MANAGEMENT_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.DOMAIN_MANAGEMENT_CONTEXT_ROOT);
		return ClientConfiguration.get(url, contextRoot, null, null);
	}

	/**
	 * 
	 * @param machineDTO
	 * @return
	 */
	protected MachineConfig toMachine(MachineConfigDTO machineDTO) {
		MachineConfigImpl impl = new MachineConfigImpl();
		impl.setId(machineDTO.getId());
		impl.setName(machineDTO.getName());
		impl.setIpAddress(machineDTO.getIpAddress());
		return impl;
	}

	/**
	 * 
	 * @param machineId
	 * @param transferAgentDTO
	 * @return
	 */
	protected TransferAgentConfig toTransferAgent(String machineId, TransferAgentConfigDTO transferAgentDTO) {
		TransferAgentConfigImpl impl = new TransferAgentConfigImpl();
		impl.setMachineId(machineId);
		impl.setId(transferAgentDTO.getId());
		impl.setName(transferAgentDTO.getName());
		impl.setHome(transferAgentDTO.getHome());
		impl.setHostURL(transferAgentDTO.getHostURL());
		impl.setContextRoot(transferAgentDTO.getContextRoot());
		return impl;
	}

	/**
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param nodeConfigDTO
	 * @return
	 */
	protected NodeConfig toNode(String machineId, String transferAgentId, NodeConfigDTO nodeConfigDTO) {
		NodeConfigImpl impl = new NodeConfigImpl();
		impl.setMachineId(machineId);
		impl.setTransferAgentId(transferAgentId);
		impl.setId(nodeConfigDTO.getId());
		impl.setName(nodeConfigDTO.getName());
		impl.setHome(nodeConfigDTO.getHome());
		impl.setHostURL(nodeConfigDTO.getHostURL());
		impl.setContextRoot(nodeConfigDTO.getContextRoot());
		return impl;
	}

	/**
	 * 
	 * @param addMachineRequest
	 * @return
	 */
	protected MachineConfigDTO toDTO(AddMachineConfigRequest addMachineRequest) {
		MachineConfigDTO addMachineRequestDTO = new MachineConfigDTO();
		addMachineRequestDTO.setId(addMachineRequest.getMachineId());
		addMachineRequestDTO.setName(addMachineRequest.getName());
		addMachineRequestDTO.setIpAddress(addMachineRequest.getIpAddress());
		return addMachineRequestDTO;
	}

	/**
	 * 
	 * @param updateMachineRequest
	 * @return
	 */
	protected MachineConfigDTO toDTO(UpdateMachineConfigRequest updateMachineRequest) {
		MachineConfigDTO updateMachineRequestDTO = new MachineConfigDTO();
		updateMachineRequestDTO.setId(updateMachineRequest.getMachineId());
		updateMachineRequestDTO.setName(updateMachineRequest.getName());
		updateMachineRequestDTO.setIpAddress(updateMachineRequest.getIpAddress());
		return updateMachineRequestDTO;
	}

	/**
	 * 
	 * @param addTransferAgentRequest
	 * @return
	 */
	protected TransferAgentConfigDTO toDTO(AddTransferAgentConfigRequest addTransferAgentRequest) {
		TransferAgentConfigDTO addTransferAgentRequestDTO = new TransferAgentConfigDTO();
		addTransferAgentRequestDTO.setId(addTransferAgentRequest.getTransferAgentId());
		addTransferAgentRequestDTO.setName(addTransferAgentRequest.getName());
		addTransferAgentRequestDTO.setHome(addTransferAgentRequest.getHome());
		addTransferAgentRequestDTO.setHostURL(addTransferAgentRequest.getHostURL());
		addTransferAgentRequestDTO.setContextRoot(addTransferAgentRequest.getContextRoot());
		return addTransferAgentRequestDTO;
	}

	/**
	 * 
	 * @param updateTransferAgentRequest
	 * @return
	 */
	protected TransferAgentConfigDTO toDTO(UpdateTransferAgentConfigRequest updateTransferAgentRequest) {
		TransferAgentConfigDTO updateTransferAgentRequestDTO = new TransferAgentConfigDTO();
		updateTransferAgentRequestDTO.setId(updateTransferAgentRequest.getTransferAgentId());
		updateTransferAgentRequestDTO.setName(updateTransferAgentRequest.getName());
		updateTransferAgentRequestDTO.setHome(updateTransferAgentRequest.getHome());
		updateTransferAgentRequestDTO.setHostURL(updateTransferAgentRequest.getHostURL());
		updateTransferAgentRequestDTO.setContextRoot(updateTransferAgentRequest.getContextRoot());
		return updateTransferAgentRequestDTO;
	}

	/**
	 * 
	 * @param addNodeRequest
	 * @return
	 */
	protected NodeConfigDTO toDTO(AddNodeConfigRequest addNodeRequest) {
		NodeConfigDTO addNodeRequestDTO = new NodeConfigDTO();
		addNodeRequestDTO.setId(addNodeRequest.getNodeId());
		addNodeRequestDTO.setName(addNodeRequest.getName());
		addNodeRequestDTO.setHome(addNodeRequest.getHome());
		addNodeRequestDTO.setHostURL(addNodeRequest.getHostURL());
		addNodeRequestDTO.setContextRoot(addNodeRequest.getContextRoot());
		return addNodeRequestDTO;
	}

	/**
	 * 
	 * @param updateNodeRequest
	 * @return
	 */
	protected NodeConfigDTO toDTO(UpdateNodeConfigRequest updateNodeRequest) {
		NodeConfigDTO updateNodeRequestDTO = new NodeConfigDTO();
		updateNodeRequestDTO.setId(updateNodeRequest.getNodeId());
		updateNodeRequestDTO.setName(updateNodeRequest.getName());
		updateNodeRequestDTO.setHome(updateNodeRequest.getHome());
		updateNodeRequestDTO.setHostURL(updateNodeRequest.getHostURL());
		updateNodeRequestDTO.setContextRoot(updateNodeRequest.getContextRoot());
		return updateNodeRequestDTO;
	}

}
