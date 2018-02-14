package org.orbit.component.runtime.tier3.domain.service;

import java.util.List;

import org.orbit.component.model.tier3.domain.DomainException;
import org.orbit.component.model.tier3.domain.MachineConfigRTO;
import org.orbit.component.model.tier3.domain.NodeConfigRTO;
import org.orbit.component.model.tier3.domain.TransferAgentConfigRTO;
import org.origin.common.rest.server.WebServiceAware;
import org.origin.core.resources.IWorkspaceService;

public interface DomainService extends WebServiceAware {

	String getName();

	// ------------------------------------------------------
	// Machine management
	// ------------------------------------------------------
	List<MachineConfigRTO> getMachineConfigs() throws DomainException;

	MachineConfigRTO getMachineConfig(String machineId) throws DomainException;

	boolean machineConfigExists(String machineId) throws DomainException;

	boolean addMachineConfig(MachineConfigRTO addMachineRequest) throws DomainException;

	boolean updateMachineConfig(MachineConfigRTO updateMachineRequest, List<String> fieldsToUpdate) throws DomainException;

	boolean deleteMachineConfig(String machineId) throws DomainException;

	// ------------------------------------------------------
	// TransferAgent management
	// ------------------------------------------------------
	List<TransferAgentConfigRTO> getTransferAgentConfigs(String machineId) throws DomainException;

	TransferAgentConfigRTO getTransferAgentConfig(String machineId, String transferAgentId) throws DomainException;

	boolean transferAgentConfigExists(String machineId, String transferAgentId) throws DomainException;

	boolean addTransferAgentConfig(String machineId, TransferAgentConfigRTO addTransferAgentRequest) throws DomainException;

	boolean updateTransferAgentConfig(String machineId, TransferAgentConfigRTO updateTransferAgentRequest, List<String> fieldsToUpdate) throws DomainException;

	boolean deleteTransferAgentConfig(String machineId, String transferAgentId) throws DomainException;

	// ------------------------------------------------------
	// Node management
	// ------------------------------------------------------
	List<NodeConfigRTO> getNodeConfigs(String machineId, String transferAgentId) throws DomainException;

	NodeConfigRTO getNodeConfig(String machineId, String transferAgentId, String nodeId) throws DomainException;

	boolean nodeConfigExists(String machineId, String transferAgentId, String nodeId) throws DomainException;

	boolean addNodeConfig(String machineId, String transferAgentId, NodeConfigRTO addNodeRequest) throws DomainException;

	boolean updateNodeConfig(String machineId, String transferAgentId, NodeConfigRTO updateNodeRequest, List<String> fieldsToUpdate) throws DomainException;

	boolean deleteNodeConfig(String machineId, String transferAgentId, String nodeId) throws DomainException;

	// ------------------------------------------------------
	// Workspaces management
	// ------------------------------------------------------
	IWorkspaceService getWorkspaceService();

}
