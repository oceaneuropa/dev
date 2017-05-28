package org.orbit.component.api.tier3.domain;

import java.util.Map;

import org.orbit.component.api.tier3.domain.request.MachineAddRequest;
import org.orbit.component.api.tier3.domain.request.MachineUpdateRequest;
import org.orbit.component.api.tier3.domain.request.NodeAddRequest;
import org.orbit.component.api.tier3.domain.request.NodeUpdateRequest;
import org.orbit.component.api.tier3.domain.request.TransferAgentAddRequest;
import org.orbit.component.api.tier3.domain.request.TransferAgentUpdateRequest;
import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.origin.common.rest.client.ClientException;

public interface DomainMgmt {

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
	public boolean ping();

	// ------------------------------------------------------
	// Machine management
	// ------------------------------------------------------
	public MachineConfig[] getMachineConfigs() throws ClientException;

	public MachineConfig getMachineConfig(String machineId) throws ClientException;

	public boolean addMachineConfig(MachineAddRequest addMachineRequest) throws ClientException;

	public boolean updateMachineConfig(MachineUpdateRequest updateMachineRequest) throws ClientException;

	public boolean removeMachineConfig(String machineId) throws ClientException;

	// ------------------------------------------------------
	// TransferAgent management
	// ------------------------------------------------------
	public TransferAgentConfig[] getTransferAgentConfigs(String machineId) throws ClientException;

	public TransferAgentConfig getTransferAgentConfig(String machineId, String transferAgentId) throws ClientException;

	public boolean addTransferAgentConfig(String machineId, TransferAgentAddRequest addTransferAgentRequest) throws ClientException;

	public boolean updateTransferAgentConfig(String machineId, TransferAgentUpdateRequest updateTransferAgentRequest) throws ClientException;

	public boolean removeTransferAgentConfig(String machineId, String transferAgentId) throws ClientException;

	// ------------------------------------------------------
	// Node management
	// ------------------------------------------------------
	public NodeConfig[] getNodeConfigs(String machineId, String transferAgentId) throws ClientException;

	public NodeConfig getNodeConfig(String machineId, String transferAgentId, String nodeId) throws ClientException;

	public boolean addNodeConfig(String machineId, String transferAgentId, NodeAddRequest addNodeRequest) throws ClientException;

	public boolean updateNodeConfig(String machineId, String transferAgentId, NodeUpdateRequest updateNodeRequest) throws ClientException;

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
