package org.orbit.component.server.tier3.domain.service.impl;

import java.util.List;

import org.orbit.component.model.tier3.domain.DomainMgmtException;
import org.orbit.component.model.tier3.domain.MachineConfigRTO;
import org.orbit.component.server.tier3.domain.service.DomainMgmtService;
import org.osgi.framework.BundleContext;

public class DomainMgmtServiceDatabaseImpl implements DomainMgmtService {

	public void start(BundleContext bundleContext) {

	}

	public void stop() {

	}

	@Override
	public List<MachineConfigRTO> getMachines() throws DomainMgmtException {
		return null;
	}

	@Override
	public MachineConfigRTO getMachine(String machineId) throws DomainMgmtException {
		return null;
	}

	@Override
	public boolean machineExists(String machineId) throws DomainMgmtException {
		return false;
	}

	@Override
	public boolean addMachine(MachineConfigRTO addMachineRequest) throws DomainMgmtException {
		return false;
	}

	@Override
	public boolean updateMachine(MachineConfigRTO updateMachineRequest) throws DomainMgmtException {
		return false;
	}

	@Override
	public boolean removeMachine(String machineId) throws DomainMgmtException {
		return false;
	}

}
