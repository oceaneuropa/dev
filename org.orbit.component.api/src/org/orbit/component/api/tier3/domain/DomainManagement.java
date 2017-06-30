package org.orbit.component.api.tier3.domain;

import java.util.Map;

import org.orbit.component.api.tier3.domain.request.AddMachineConfigRequest;
import org.orbit.component.api.tier3.domain.request.AddNodeConfigRequest;
import org.orbit.component.api.tier3.domain.request.AddTransferAgentConfigRequest;
import org.orbit.component.api.tier3.domain.request.UpdateMachineConfigRequest;
import org.orbit.component.api.tier3.domain.request.UpdateNodeConfigRequest;
import org.orbit.component.api.tier3.domain.request.UpdateTransferAgentConfigRequest;
import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Pingable;

public interface DomainManagement extends Pingable {

	/**
	 * Get service name.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Get service URL.
	 * 
	 * @return
	 */
	String getURL();

	/**
	 * Get configuration properties.
	 * 
	 * @return
	 */
	Map<String, Object> getProperties();

	/**
	 * Update configuration properties.
	 * 
	 * @param properties
	 */
	void update(Map<String, Object> properties);

	/**
	 * Ping the service.
	 * 
	 * @return
	 */
	int ping() throws ClientException;

	// ------------------------------------------------------
	// Machine management
	// ------------------------------------------------------
	public MachineConfig[] getMachineConfigs() throws ClientException;

	public MachineConfig getMachineConfig(String machineId) throws ClientException;

	public boolean addMachineConfig(AddMachineConfigRequest addMachineRequest) throws ClientException;

	public boolean updateMachineConfig(UpdateMachineConfigRequest updateMachineRequest) throws ClientException;

	public boolean removeMachineConfig(String machineId) throws ClientException;

	// ------------------------------------------------------
	// TransferAgent management
	// ------------------------------------------------------
	public TransferAgentConfig[] getTransferAgentConfigs(String machineId) throws ClientException;

	public TransferAgentConfig getTransferAgentConfig(String machineId, String transferAgentId) throws ClientException;

	public boolean addTransferAgentConfig(String machineId, AddTransferAgentConfigRequest addTransferAgentRequest) throws ClientException;

	public boolean updateTransferAgentConfig(String machineId, UpdateTransferAgentConfigRequest updateTransferAgentRequest) throws ClientException;

	public boolean removeTransferAgentConfig(String machineId, String transferAgentId) throws ClientException;

	// ------------------------------------------------------
	// Node management
	// ------------------------------------------------------
	public NodeConfig[] getNodeConfigs(String machineId, String transferAgentId) throws ClientException;

	public NodeConfig getNodeConfig(String machineId, String transferAgentId, String nodeId) throws ClientException;

	public boolean addNodeConfig(String machineId, String transferAgentId, AddNodeConfigRequest addNodeRequest) throws ClientException;

	public boolean updateNodeConfig(String machineId, String transferAgentId, UpdateNodeConfigRequest updateNodeRequest) throws ClientException;

	public boolean removeNodeConfig(String machineId, String transferAgentId, String nodeId) throws ClientException;

	// ------------------------------------------------------
	// Life cycle
	// ------------------------------------------------------
	public TransferAgent getTransferAgent(String machineId, String transferAgentId) throws ClientException;

}

// public Map<String, Object> getMachineConfigProperties(String machineId) throws ClientException;
// public boolean setMachineConfigProperty(String machineId, String name, Object value) throws ClientException;
// public Object getMachineConfigProperty(String machineId, String name) throws ClientException;
// public boolean removeMachineConfigProperty(String machineId, String name) throws ClientException;

// public Map<String, Object> getTransferAgentConfigProperties(String machineId, String transferAgentId) throws ClientException;
// public boolean setTransferAgentConfigProperty(String machineId, String transferAgentId, String name, Object value) throws ClientException;
// public Object getTransferAgentConfigProperty(String machineId, String transferAgentId, String name) throws ClientException;
// public boolean removeTransferAgentConfigProperty(String machineId, String transferAgentId, String name) throws ClientException;
