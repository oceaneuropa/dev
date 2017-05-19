package org.orbit.component.connector.tier3.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.account.request.CreateUserAccountRequest;
import org.orbit.component.api.tier1.account.request.UpdateUserAccountRequest;
import org.orbit.component.api.tier3.domain.DomainMgmt;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.TransferAgentConfig;
import org.orbit.component.api.tier3.domain.request.AddMachineRequest;
import org.orbit.component.api.tier3.domain.request.AddTransferAgentRequest;
import org.orbit.component.api.tier3.domain.request.UpdateMachineRequest;
import org.orbit.component.api.tier3.domain.request.UpdateTransferAgentRequest;
import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.model.tier1.account.UserAccountDTO;
import org.orbit.component.model.tier3.domain.MachineConfigDTO;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.util.StringUtil;

public class DomainMgmtImpl implements DomainMgmt {

	protected Map<String, Object> properties;
	protected DomainMgmtWSClient client;

	/**
	 * 
	 * @param properties
	 */
	public DomainMgmtImpl(Map<String, Object> properties) {
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

	private Map<String, Object> checkProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
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

	protected void initClient() {
		ClientConfiguration clientConfig = getClientConfiguration(this.properties);
		this.client = new DomainMgmtWSClient(clientConfig);
	}

	// ------------------------------------------------------------------------------------------------
	// Web service methods
	// ------------------------------------------------------------------------------------------------
	@Override
	public boolean ping() {
		return this.client.doPing();
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
	public MachineConfig[] getMachines() throws ClientException {
		List<MachineConfig> machines = new ArrayList<MachineConfig>();
		try {
			List<MachineConfigDTO> machineDTOs = this.client.getMachines();
			for (MachineConfigDTO machineDTO : machineDTOs) {
				machines.add(toMachineImpl(machineDTO));
			}
		} catch (ClientException e) {
			throw e;
		}
		return machines.toArray(new MachineConfig[machines.size()]);
	}

	@Override
	public boolean addMachine(AddMachineRequest addMachineRequest) throws ClientException {
		String machineId = addMachineRequest.getMachineId();
		checkMachineId(machineId);

		try {
			UserAccountDTO createUserAccountRequestDTO = toDTO(createUserAccountRequest);
			StatusDTO status = this.client.registerUserAccount(createUserAccountRequestDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean updateMachine(UpdateMachineRequest request) throws ClientException {
		return false;
	}

	@Override
	public boolean removeMachine(String machineId) throws ClientException {
		return false;
	}

	@Override
	public Map<String, Object> getMachineProperties(String machineId) {
		return null;
	}

	@Override
	public boolean setMachineProperty(String machineId, String name, Object value) {
		return false;
	}

	@Override
	public Object getMachineProperty(String machineId, String name) {
		return null;
	}

	@Override
	public boolean removeMachineProperty(String machineId, String name) {
		return false;
	}

	// ------------------------------------------------------
	// TransferAgent management
	// ------------------------------------------------------
	@Override
	public TransferAgentConfig[] getTransferAgents(String machineId) throws ClientException {
		return null;
	}

	@Override
	public boolean addTransferAgent(String machineId, AddTransferAgentRequest request) throws ClientException {
		return false;
	}

	@Override
	public boolean updateTransferAgent(String machineId, UpdateTransferAgentRequest request) throws ClientException {
		return false;
	}

	@Override
	public boolean removeTransferAgent(String machineId, String agentId) throws ClientException {
		return false;
	}

	@Override
	public Map<String, Object> getTransferAgentProperties(String machineId, String agentId) {
		return null;
	}

	@Override
	public boolean setTransferAgentProperty(String machineId, String agentId, String name, Object value) {
		return false;
	}

	@Override
	public Object getTransferAgentProperty(String machineId, String agentId, String name) {
		return null;
	}

	@Override
	public boolean removeTransferAgentProperty(String machineId, String agentId, String name) {
		return false;
	}

	// ------------------------------------------------------
	// TransferAgent life cycle
	// ------------------------------------------------------
	@Override
	public TransferAgent getTransferAgent(String machineId, String agentId) {
		return null;
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
	protected MachineConfig toMachineImpl(MachineConfigDTO machineDTO) {
		MachineConfigImpl impl = new MachineConfigImpl();
		impl.setId(machineDTO.getId());
		impl.setName(machineDTO.getName());
		impl.setIpAddress(machineDTO.getIpAddress());
		return null;
	}

	/**
	 * Convert AddMachineRequest to DTO.
	 * 
	 * @param addMachineRequest
	 * @return
	 */
	protected MachineConfigDTO toDTO(AddMachineRequest addMachineRequest) {
		MachineConfigDTO addMachineRequestDTO = new MachineConfigDTO();
		addMachineRequestDTO.setUserId(addMachineRequest.getId());
		addMachineRequestDTO.setPassword(addMachineRequest.getName());
		addMachineRequestDTO.setEmail(createUserAccountRequest.getEmail());
		addMachineRequestDTO.setFirstName(createUserAccountRequest.getFirstName());
		addMachineRequestDTO.setLastName(createUserAccountRequest.getLastName());
		addMachineRequestDTO.setPhone(createUserAccountRequest.getPhone());
		return addMachineRequestDTO;
	}

	
	/**
	 * Convert UpdateMachineRequest to DTO.
	 * 
	 * @param updateMachineRequest
	 * @return
	 */
	protected MachineConfigDTO toDTO(UpdateMachineRequest updateMachineRequest) {
		MachineConfigDTO updateMachineRequestDTO = new MachineConfigDTO();
		updateMachineRequestDTO.setId(updateMachineRequest.getMachineId());
		updateMachineRequestDTO.setName(updateMachineRequest.getName());
		updateMachineRequestDTO.setEmail(updateUserAccountRequest.getEmail());
		updateMachineRequestDTO.setFirstName(updateUserAccountRequest.getFirstName());
		updateMachineRequestDTO.setLastName(updateUserAccountRequest.getLastName());
		updateMachineRequestDTO.setPhone(updateUserAccountRequest.getPhone());
		return updateMachineRequestDTO;
	}

}
