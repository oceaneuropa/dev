package org.orbit.component.runtime.tier3.domainmanagement.service;

import java.util.List;

import org.orbit.component.model.tier3.domain.MachineConfigRTO;
import org.orbit.component.model.tier3.domain.NodeConfigRTO;
import org.orbit.component.model.tier3.domain.TransferAgentConfigRTO;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.WebServiceAware;
import org.origin.core.resources.IWorkspaceService;

public interface DomainManagementService extends WebServiceAware {

	String getName();

	// ------------------------------------------------------
	// Machine management
	// ------------------------------------------------------
	List<MachineConfigRTO> getMachineConfigs() throws ServerException;

	MachineConfigRTO getMachineConfig(String machineId) throws ServerException;

	boolean machineConfigExists(String machineId) throws ServerException;

	boolean addMachineConfig(MachineConfigRTO addMachineRequest) throws ServerException;

	boolean updateMachineConfig(MachineConfigRTO updateMachineRequest, List<String> fieldsToUpdate) throws ServerException;

	boolean deleteMachineConfig(String machineId) throws ServerException;

	// ------------------------------------------------------
	// TransferAgent management
	// ------------------------------------------------------
	List<TransferAgentConfigRTO> getTransferAgentConfigs(String machineId) throws ServerException;

	TransferAgentConfigRTO getTransferAgentConfig(String machineId, String transferAgentId) throws ServerException;

	boolean transferAgentConfigExists(String machineId, String transferAgentId) throws ServerException;

	boolean addTransferAgentConfig(String machineId, TransferAgentConfigRTO addTransferAgentRequest) throws ServerException;

	boolean updateTransferAgentConfig(String machineId, TransferAgentConfigRTO updateTransferAgentRequest, List<String> fieldsToUpdate) throws ServerException;

	boolean deleteTransferAgentConfig(String machineId, String transferAgentId) throws ServerException;

	// ------------------------------------------------------
	// Node management
	// ------------------------------------------------------
	List<NodeConfigRTO> getNodeConfigs(String machineId, String transferAgentId) throws ServerException;

	NodeConfigRTO getNodeConfig(String machineId, String transferAgentId, String nodeId) throws ServerException;

	boolean nodeConfigExists(String machineId, String transferAgentId, String nodeId) throws ServerException;

	boolean addNodeConfig(String machineId, String transferAgentId, NodeConfigRTO addNodeRequest) throws ServerException;

	boolean updateNodeConfig(String machineId, String transferAgentId, NodeConfigRTO updateNodeRequest, List<String> fieldsToUpdate) throws ServerException;

	boolean deleteNodeConfig(String machineId, String transferAgentId, String nodeId) throws ServerException;

	// ------------------------------------------------------
	// Workspaces management
	// ------------------------------------------------------
	IWorkspaceService getWorkspaceService();

}
