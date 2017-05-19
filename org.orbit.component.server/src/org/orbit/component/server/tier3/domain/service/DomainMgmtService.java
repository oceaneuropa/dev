package org.orbit.component.server.tier3.domain.service;

import java.util.List;

import org.orbit.component.model.tier3.domain.DomainMgmtException;
import org.orbit.component.model.tier3.domain.MachineConfigRTO;

public interface DomainMgmtService {

	List<MachineConfigRTO> getMachines() throws DomainMgmtException;

	MachineConfigRTO getMachine(String machineId) throws DomainMgmtException;

	boolean machineExists(String machineId) throws DomainMgmtException;

	boolean addMachine(MachineConfigRTO addMachineRequest) throws DomainMgmtException;

	boolean updateMachine(MachineConfigRTO updateMachineRequest) throws DomainMgmtException;

	boolean removeMachine(String machineId) throws DomainMgmtException;

}
