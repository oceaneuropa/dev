package org.orbit.component.api.tier3.domain;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.component.model.tier3.domain.dto.MachineConfig;
import org.orbit.component.model.tier3.domain.dto.NodeConfig;
import org.orbit.component.model.tier3.domain.dto.TransferAgentConfig;
import org.orbit.component.model.tier3.domain.request.AddMachineConfigRequest;
import org.orbit.component.model.tier3.domain.request.AddNodeConfigRequest;
import org.orbit.component.model.tier3.domain.request.AddTransferAgentConfigRequest;
import org.orbit.component.model.tier3.domain.request.UpdateMachineConfigRequest;
import org.orbit.component.model.tier3.domain.request.UpdateNodeConfigRequest;
import org.orbit.component.model.tier3.domain.request.UpdateTransferAgentConfigRequest;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.Pingable;
import org.origin.common.rest.model.Request;

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
	// Request/Response
	// ------------------------------------------------------
	public Response sendRequest(Request request) throws ClientException;

	// DomainManagementResponseConverter getResponseConverter();

	// ------------------------------------------------------
	// Machine management
	// ------------------------------------------------------
	MachineConfig[] getMachineConfigs() throws ClientException;

	MachineConfig getMachineConfig(String machineId) throws ClientException;

	boolean addMachineConfig(AddMachineConfigRequest addMachineRequest) throws ClientException;

	boolean updateMachineConfig(UpdateMachineConfigRequest updateMachineRequest) throws ClientException;

	boolean removeMachineConfig(String machineId) throws ClientException;

	// ------------------------------------------------------
	// TransferAgent management
	// ------------------------------------------------------
	TransferAgentConfig[] getTransferAgentConfigs(String machineId) throws ClientException;

	TransferAgentConfig getTransferAgentConfig(String machineId, String transferAgentId) throws ClientException;

	boolean addTransferAgentConfig(String machineId, AddTransferAgentConfigRequest addTransferAgentRequest) throws ClientException;

	boolean updateTransferAgentConfig(String machineId, UpdateTransferAgentConfigRequest updateTransferAgentRequest) throws ClientException;

	boolean removeTransferAgentConfig(String machineId, String transferAgentId) throws ClientException;

	// ------------------------------------------------------
	// Node management
	// ------------------------------------------------------
	NodeConfig[] getNodeConfigs(String machineId, String transferAgentId) throws ClientException;

	NodeConfig getNodeConfig(String machineId, String transferAgentId, String nodeId) throws ClientException;

	boolean addNodeConfig(String machineId, String transferAgentId, AddNodeConfigRequest addNodeRequest) throws ClientException;

	boolean updateNodeConfig(String machineId, String transferAgentId, UpdateNodeConfigRequest updateNodeRequest) throws ClientException;

	boolean removeNodeConfig(String machineId, String transferAgentId, String nodeId) throws ClientException;

}

// public Map<String, Object> getMachineConfigProperties(String machineId) throws ClientException;
// public boolean setMachineConfigProperty(String machineId, String name, Object value) throws ClientException;
// public Object getMachineConfigProperty(String machineId, String name) throws ClientException;
// public boolean removeMachineConfigProperty(String machineId, String name) throws ClientException;

// public Map<String, Object> getTransferAgentConfigProperties(String machineId, String transferAgentId) throws ClientException;
// public boolean setTransferAgentConfigProperty(String machineId, String transferAgentId, String name, Object value) throws ClientException;
// public Object getTransferAgentConfigProperty(String machineId, String transferAgentId, String name) throws ClientException;
// public boolean removeTransferAgentConfigProperty(String machineId, String transferAgentId, String name) throws ClientException;

// ------------------------------------------------------
// Life cycle
// ------------------------------------------------------
// public TransferAgent getTransferAgent(TransferAgentConfig taConfig) throws ClientException;