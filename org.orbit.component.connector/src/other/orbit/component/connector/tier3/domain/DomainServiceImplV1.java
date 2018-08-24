package other.orbit.component.connector.tier3.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.NodeConfig;
import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.connector.ComponentConstants;
import org.orbit.component.connector.tier3.domain.DomainManagementWSClient;
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
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.util.StringUtil;

public class DomainServiceImplV1 implements DomainManagementClient {

	protected Map<String, Object> properties;
	protected DomainManagementWSClient client;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param properties
	 */
	public DomainServiceImplV1(ServiceConnector<DomainManagementClient> connector, Map<String, Object> properties) {
		if (connector != null) {
			adapt(ServiceConnector.class, connector);
		}
		this.properties = checkProperties(properties);
		initClient();
	}

	@Override
	public boolean close() throws ClientException {
		@SuppressWarnings("unchecked")
		ServiceConnector<DomainManagementClient> connector = getAdapter(ServiceConnector.class);
		if (connector != null) {
			return connector.close(this);
		}
		return false;
	}

	// ---------------------------------------------------------
	// Configuration methods
	// ---------------------------------------------------------
	// @Override
	// public String getName() {
	// String name = (String) this.properties.get(OrbitConstants.DOMAIN_SERVICE_NAME);
	// return name;
	// }

	@Override
	public String getURL() {
		String hostURL = (String) this.properties.get(ComponentConstants.DOMAIN_SERVICE_HOST_URL);
		String contextRoot = (String) this.properties.get(ComponentConstants.DOMAIN_SERVICE_CONTEXT_ROOT);
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
		String oldUrl = (String) this.properties.get(ComponentConstants.DOMAIN_SERVICE_HOST_URL);
		String oldContextRoot = (String) this.properties.get(ComponentConstants.DOMAIN_SERVICE_CONTEXT_ROOT);

		properties = checkProperties(properties);
		this.properties.putAll(properties);

		String newUrl = (String) properties.get(ComponentConstants.DOMAIN_SERVICE_HOST_URL);
		String newContextRoot = (String) properties.get(ComponentConstants.DOMAIN_SERVICE_CONTEXT_ROOT);

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
		WSClientConfiguration clientConfig = getClientConfiguration(this.properties);
		this.client = new DomainManagementWSClient(clientConfig);
	}

	// ---------------------------------------------------------
	// Web service methods
	// ---------------------------------------------------------
	// @Override
	// public int ping() throws ClientException {
	// return this.client.ping();
	// }

	@Override
	public boolean ping() {
		return this.client.doPing();
	}

	@Override
	public String echo(String message) throws ClientException {
		return this.client.echo(message);
	}

	// ---------------------------------------------------------
	// Request/Response
	// ---------------------------------------------------------
	@Override
	public Response sendRequest(Request request) throws ClientException {
		return this.client.sendRequest(request);
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
	public PlatformConfig[] getPlatformConfigs(String machineId) throws ClientException {
		checkMachineId(machineId);

		List<PlatformConfig> transferAgents = new ArrayList<PlatformConfig>();
		try {
			List<PlatformConfigDTO> transferAgentDTOs = this.client.getPlatformConfigs(machineId);
			for (PlatformConfigDTO transferAgentDTO : transferAgentDTOs) {
				transferAgents.add(ModelConverter.Domain.toPlatformConfig(machineId, transferAgentDTO));
			}
		} catch (ClientException e) {
			throw e;
		}
		return transferAgents.toArray(new PlatformConfig[transferAgents.size()]);
	}

	@Override
	public PlatformConfig getPlatformConfig(String machineId, String transferAgentId) throws ClientException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);

		PlatformConfig transferAgent = null;
		try {
			PlatformConfigDTO transferAgentDTO = this.client.getPlatformConfig(machineId, transferAgentId);
			if (transferAgentDTO != null) {
				transferAgent = ModelConverter.Domain.toPlatformConfig(machineId, transferAgentDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return transferAgent;
	}

	@Override
	public boolean addPlatformConfig(String machineId, AddPlatformConfigRequest addTransferAgentRequest) throws ClientException {
		checkMachineId(machineId);
		String transferAgentId = addTransferAgentRequest.getPlatformId();
		checkTransferAgentId(transferAgentId);

		try {
			PlatformConfigDTO addTransferAgentRequestDTO = ModelConverter.Domain.toDTO(addTransferAgentRequest);
			StatusDTO status = this.client.addPlatformConfig(machineId, addTransferAgentRequestDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean updatPlatformConfig(String machineId, UpdatePlatformConfigRequest updateTransferAgentRequest) throws ClientException {
		checkMachineId(machineId);
		String transferAgentId = updateTransferAgentRequest.getPlatformId();
		checkTransferAgentId(transferAgentId);

		try {
			PlatformConfigDTO updateTransferAgentRequestDTO = ModelConverter.Domain.toDTO(updateTransferAgentRequest);
			StatusDTO status = this.client.updatePlatformConfig(machineId, updateTransferAgentRequestDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean removePlatformConfig(String machineId, String transferAgentId) throws ClientException {
		checkMachineId(machineId);
		checkTransferAgentId(transferAgentId);

		try {
			StatusDTO status = this.client.removePlatformConfig(machineId, transferAgentId);
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
				nodeConfigs.add(ModelConverter.Domain.toNodeConfig(machineId, transferAgentId, nodeConfigDTO));
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
				nodeConfig = ModelConverter.Domain.toNodeConfig(machineId, transferAgentId, nodeConfigDTO);
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
			NodeConfigDTO addNodeRequestDTO = ModelConverter.Domain.toDTO(addNodeRequest);
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
			NodeConfigDTO updateNodeRequestDTO = ModelConverter.Domain.toDTO(updateNodeRequest);
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

	// ---------------------------------------------------------
	// Helper methods
	// ---------------------------------------------------------
	/**
	 * Get web service client configuration.
	 * 
	 * @param properties
	 * @return
	 */
	protected WSClientConfiguration getClientConfiguration(Map<String, Object> properties) {
		String realm = (String) properties.get(WSClientConstants.REALM);
		String accessToken = (String) properties.get(WSClientConstants.ACCESS_TOKEN);
		String url = (String) properties.get(ComponentConstants.DOMAIN_SERVICE_HOST_URL);
		String contextRoot = (String) properties.get(ComponentConstants.DOMAIN_SERVICE_CONTEXT_ROOT);

		WSClientConfiguration config = WSClientConfiguration.create(realm, accessToken, url, contextRoot);
		return config;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

	@Override
	public boolean isProxy() {
		return false;
	}

	@Override
	public ServiceMetadata getMetadata() throws ClientException {
		return null;
	}

	@Override
	public String getName() throws ClientException {
		return null;
	}

}

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
