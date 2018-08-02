package org.orbit.component.api.tier3.domain;

import org.orbit.component.model.tier3.domain.AddMachineConfigRequest;
import org.orbit.component.model.tier3.domain.AddNodeConfigRequest;
import org.orbit.component.model.tier3.domain.AddPlatformConfigRequest;
import org.orbit.component.model.tier3.domain.UpdateMachineConfigRequest;
import org.orbit.component.model.tier3.domain.UpdateNodeConfigRequest;
import org.orbit.component.model.tier3.domain.UpdatePlatformConfigRequest;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface DomainManagementClient extends ServiceClient {

	// ------------------------------------------------------
	// Machine management
	// ------------------------------------------------------
	MachineConfig[] getMachineConfigs() throws ClientException;

	MachineConfig getMachineConfig(String machineId) throws ClientException;

	boolean addMachineConfig(AddMachineConfigRequest addMachineRequest) throws ClientException;

	boolean updateMachineConfig(UpdateMachineConfigRequest updateMachineRequest) throws ClientException;

	boolean removeMachineConfig(String machineId) throws ClientException;

	// ------------------------------------------------------
	// Platform management
	// ------------------------------------------------------
	PlatformConfig[] getPlatformConfigs(String machineId) throws ClientException;

	PlatformConfig getPlatformConfig(String machineId, String platformId) throws ClientException;

	boolean addPlatformConfig(String machineId, AddPlatformConfigRequest addPlatformRequest) throws ClientException;

	boolean updatPlatformConfig(String machineId, UpdatePlatformConfigRequest updatePlatformRequest) throws ClientException;

	boolean removePlatformConfig(String machineId, String platformId) throws ClientException;

	// ------------------------------------------------------
	// Node management
	// ------------------------------------------------------
	NodeConfig[] getNodeConfigs(String machineId, String platformId) throws ClientException;

	NodeConfig getNodeConfig(String machineId, String platformId, String nodeId) throws ClientException;

	boolean addNodeConfig(String machineId, String platformId, AddNodeConfigRequest addNodeRequest) throws ClientException;

	boolean updateNodeConfig(String machineId, String platformId, UpdateNodeConfigRequest updateNodeRequest) throws ClientException;

	boolean removeNodeConfig(String machineId, String platformId, String nodeId) throws ClientException;

}

// public Map<String, Object> getMachineConfigProperties(String machineId) throws ClientException;
// public boolean setMachineConfigProperty(String machineId, String name, Object value) throws ClientException;
// public Object getMachineConfigProperty(String machineId, String name) throws ClientException;
// public boolean removeMachineConfigProperty(String machineId, String name) throws ClientException;

// public Map<String, Object> getTransferAgentConfigProperties(String machineId, String transferAgentId) throws ClientException;
// public boolean setTransferAgentConfigProperty(String machineId, String transferAgentId, String name, Object value) throws
// ClientException;
// public Object getTransferAgentConfigProperty(String machineId, String transferAgentId, String name) throws ClientException;
// public boolean removeTransferAgentConfigProperty(String machineId, String transferAgentId, String name) throws ClientException;

// ------------------------------------------------------
// Life cycle
// ------------------------------------------------------
// public TransferAgent getTransferAgent(TransferAgentConfig taConfig) throws ClientException;

// String getName();
