package org.orbit.component.connector.tier3.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier3.domain.DomainMgmt;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.TransferAgentConfig;
import org.orbit.component.api.tier3.domain.request.AddMachineRequest;
import org.orbit.component.api.tier3.domain.request.AddTransferAgentRequest;
import org.orbit.component.api.tier3.domain.request.UpdateMachineRequest;
import org.orbit.component.api.tier3.domain.request.UpdateTransferAgentRequest;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.model.tier3.domain.MachineConfigDTO;
import org.orbit.component.model.tier3.domain.TransferAgentConfigDTO;
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
				machines.add(toMachine(machineDTO));
			}
		} catch (ClientException e) {
			throw e;
		}
		return machines.toArray(new MachineConfig[machines.size()]);
	}

	@Override
	public MachineConfig addMachine(String machineId) throws ClientException {
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
	public boolean addMachine(AddMachineRequest addMachineRequest) throws ClientException {
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
	public boolean updateMachine(UpdateMachineRequest updateMachineRequest) throws ClientException {
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
	public boolean removeMachine(String machineId) throws ClientException {
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
	/**
	 * 
	 * @param machineId
	 * @throws ClientException
	 */
	protected void checkTransferAgentId(String transferAgentId) throws ClientException {
		if (transferAgentId == null || transferAgentId.isEmpty()) {
			throw new ClientException(400, "transferAgentId is empty.");
		}
	}

	@Override
	public TransferAgentConfig[] getTransferAgents(String machineId) throws ClientException {
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
	public TransferAgentConfig getTransferAgent(String machineId, String transferAgentId) throws ClientException {
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
	public boolean addTransferAgent(String machineId, AddTransferAgentRequest addTransferAgentRequest) throws ClientException {
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
	public boolean updateTransferAgent(String machineId, UpdateTransferAgentRequest updateTransferAgentRequest) throws ClientException {
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
	public boolean removeTransferAgent(String machineId, String transferAgentId) throws ClientException {
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

	@Override
	public Map<String, Object> getTransferAgentProperties(String machineId, String transferAgentId) {
		return null;
	}

	@Override
	public boolean setTransferAgentProperty(String machineId, String transferAgentId, String name, Object value) {
		return false;
	}

	@Override
	public Object getTransferAgentProperty(String machineId, String transferAgentId, String name) {
		return null;
	}

	@Override
	public boolean removeTransferAgentProperty(String machineId, String transferAgentId, String name) {
		return false;
	}

	// ------------------------------------------------------
	// TransferAgent life cycle
	// ------------------------------------------------------
	// @Override
	// public TransferAgent getTransferAgent(String machineId, String transferAgentId) {
	// return null;
	// }

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
		impl.setTAHome(transferAgentDTO.getTAHome());
		impl.setHostURL(transferAgentDTO.getHostURL());
		impl.setContextRoot(transferAgentDTO.getContextRoot());
		return impl;
	}

	/**
	 * 
	 * @param addMachineRequest
	 * @return
	 */
	protected MachineConfigDTO toDTO(AddMachineRequest addMachineRequest) {
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
	protected MachineConfigDTO toDTO(UpdateMachineRequest updateMachineRequest) {
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
	protected TransferAgentConfigDTO toDTO(AddTransferAgentRequest addTransferAgentRequest) {
		TransferAgentConfigDTO addTransferAgentRequestDTO = new TransferAgentConfigDTO();
		addTransferAgentRequestDTO.setId(addTransferAgentRequest.getTransferAgentId());
		addTransferAgentRequestDTO.setName(addTransferAgentRequest.getName());
		addTransferAgentRequestDTO.setTAHome(addTransferAgentRequest.getTAHome());
		addTransferAgentRequestDTO.setHostURL(addTransferAgentRequest.getHostURL());
		addTransferAgentRequestDTO.setContextRoot(addTransferAgentRequest.getContextRoot());
		return addTransferAgentRequestDTO;
	}

	/**
	 * 
	 * @param updateTransferAgentRequest
	 * @return
	 */
	protected TransferAgentConfigDTO toDTO(UpdateTransferAgentRequest updateTransferAgentRequest) {
		TransferAgentConfigDTO updateTransferAgentRequestDTO = new TransferAgentConfigDTO();
		updateTransferAgentRequestDTO.setId(updateTransferAgentRequest.getTransferAgentId());
		updateTransferAgentRequestDTO.setName(updateTransferAgentRequest.getName());
		updateTransferAgentRequestDTO.setTAHome(updateTransferAgentRequest.getTAHome());
		updateTransferAgentRequestDTO.setHostURL(updateTransferAgentRequest.getHostURL());
		updateTransferAgentRequestDTO.setContextRoot(updateTransferAgentRequest.getContextRoot());
		return updateTransferAgentRequestDTO;
	}

}
