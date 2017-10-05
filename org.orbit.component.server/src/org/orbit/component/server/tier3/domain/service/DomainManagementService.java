package org.orbit.component.server.tier3.domain.service;

import java.util.List;

import org.orbit.component.model.tier3.domain.DomainMgmtException;
import org.orbit.component.model.tier3.domain.MachineConfigRTO;
import org.orbit.component.model.tier3.domain.NodeConfigRTO;
import org.orbit.component.model.tier3.domain.TransferAgentConfigRTO;
import org.origin.core.resources.IWorkspaceService;

public interface DomainManagementService {

	String getNamespace();

	String getName();

	String getHostURL();

	String getContextRoot();

	// ------------------------------------------------------
	// Machine management
	// ------------------------------------------------------
	List<MachineConfigRTO> getMachineConfigs() throws DomainMgmtException;

	MachineConfigRTO getMachineConfig(String machineId) throws DomainMgmtException;

	boolean machineConfigExists(String machineId) throws DomainMgmtException;

	boolean addMachineConfig(MachineConfigRTO addMachineRequest) throws DomainMgmtException;

	boolean updateMachineConfig(MachineConfigRTO updateMachineRequest, List<String> fieldsToUpdate) throws DomainMgmtException;

	boolean deleteMachineConfig(String machineId) throws DomainMgmtException;

	// ------------------------------------------------------
	// TransferAgent management
	// ------------------------------------------------------
	List<TransferAgentConfigRTO> getTransferAgentConfigs(String machineId) throws DomainMgmtException;

	TransferAgentConfigRTO getTransferAgentConfig(String machineId, String transferAgentId) throws DomainMgmtException;

	boolean transferAgentConfigExists(String machineId, String transferAgentId) throws DomainMgmtException;

	boolean addTransferAgentConfig(String machineId, TransferAgentConfigRTO addTransferAgentRequest) throws DomainMgmtException;

	boolean updateTransferAgentConfig(String machineId, TransferAgentConfigRTO updateTransferAgentRequest, List<String> fieldsToUpdate) throws DomainMgmtException;

	boolean deleteTransferAgentConfig(String machineId, String transferAgentId) throws DomainMgmtException;

	// ------------------------------------------------------
	// Node management
	// ------------------------------------------------------
	List<NodeConfigRTO> getNodeConfigs(String machineId, String transferAgentId) throws DomainMgmtException;

	NodeConfigRTO getNodeConfig(String machineId, String transferAgentId, String nodeId) throws DomainMgmtException;

	boolean nodeConfigExists(String machineId, String transferAgentId, String nodeId) throws DomainMgmtException;

	boolean addNodeConfig(String machineId, String transferAgentId, NodeConfigRTO addNodeRequest) throws DomainMgmtException;

	boolean updateNodeConfig(String machineId, String transferAgentId, NodeConfigRTO updateNodeRequest, List<String> fieldsToUpdate) throws DomainMgmtException;

	boolean deleteNodeConfig(String machineId, String transferAgentId, String nodeId) throws DomainMgmtException;

	// ------------------------------------------------------
	// Workspaces management
	// ------------------------------------------------------
	IWorkspaceService getWorkspaceService();

}
