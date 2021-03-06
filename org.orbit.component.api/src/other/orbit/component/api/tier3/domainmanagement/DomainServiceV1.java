package other.orbit.component.api.tier3.domainmanagement;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.NodeConfig;
import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.model.tier3.domain.AddMachineConfigRequest;
import org.orbit.component.model.tier3.domain.AddNodeConfigRequest;
import org.orbit.component.model.tier3.domain.AddPlatformConfigRequest;
import org.orbit.component.model.tier3.domain.UpdateMachineConfigRequest;
import org.orbit.component.model.tier3.domain.UpdateNodeConfigRequest;
import org.orbit.component.model.tier3.domain.UpdatePlatformConfigRequest;
import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.Pingable;
import org.origin.common.rest.model.Request;

public interface DomainServiceV1 extends Pingable, IAdaptable {

	boolean close() throws ClientException;

	String getName();

	String getURL();

	Map<String, Object> getProperties();

	void update(Map<String, Object> properties);

	int ping() throws ClientException;

	// ------------------------------------------------------
	// Request/Response
	// ------------------------------------------------------
	Response sendRequest(Request request) throws ClientException;

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
	PlatformConfig[] getTransferAgentConfigs(String machineId) throws ClientException;

	PlatformConfig getTransferAgentConfig(String machineId, String transferAgentId) throws ClientException;

	boolean addTransferAgentConfig(String machineId, AddPlatformConfigRequest addTransferAgentRequest) throws ClientException;

	boolean updateTransferAgentConfig(String machineId, UpdatePlatformConfigRequest updateTransferAgentRequest) throws ClientException;

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
// public boolean setTransferAgentConfigProperty(String machineId, String transferAgentId, String name, Object value) throws
// ClientException;
// public Object getTransferAgentConfigProperty(String machineId, String transferAgentId, String name) throws ClientException;
// public boolean removeTransferAgentConfigProperty(String machineId, String transferAgentId, String name) throws ClientException;

// ------------------------------------------------------
// Life cycle
// ------------------------------------------------------
// public TransferAgent getTransferAgent(TransferAgentConfig taConfig) throws ClientException;