package org.orbit.component.server.tier3.domain.service;

import java.util.List;

import org.orbit.component.model.tier3.domain.DomainMgmtException;
import org.orbit.component.model.tier3.domain.MachineConfigRTO;
import org.orbit.component.model.tier3.domain.TransferAgentConfigRTO;

public interface DomainMgmtService {

	public String getHostURL();

	public String getContextRoot();

	public String getName();

	// ------------------------------------------------------
	// Machine management
	// ------------------------------------------------------
	List<MachineConfigRTO> getMachineConfigs() throws DomainMgmtException;

	MachineConfigRTO getMachineConfig(String machineId) throws DomainMgmtException;

	boolean machineConfigExists(String machineId) throws DomainMgmtException;

	boolean addMachineConfig(MachineConfigRTO addMachineRequest) throws DomainMgmtException;

	boolean updateMachineConfig(MachineConfigRTO updateMachineRequest) throws DomainMgmtException;

	boolean deleteMachineConfig(String machineId) throws DomainMgmtException;

	// ------------------------------------------------------
	// TransferAgent management
	// ------------------------------------------------------
	List<TransferAgentConfigRTO> getTransferAgentConfigs(String machineId) throws DomainMgmtException;

	TransferAgentConfigRTO getTransferAgentConfig(String machineId, String transferAgentId) throws DomainMgmtException;

	boolean transferAgentConfigExists(String machineId, String transferAgentId) throws DomainMgmtException;

	boolean addTransferAgentConfig(String machineId, TransferAgentConfigRTO addTransferAgentRequest) throws DomainMgmtException;

	boolean updateTransferAgentConfig(String machineId, TransferAgentConfigRTO updateTransferAgentRequest) throws DomainMgmtException;

	boolean deleteTransferAgentConfig(String machineId, String transferAgentId) throws DomainMgmtException;

}
