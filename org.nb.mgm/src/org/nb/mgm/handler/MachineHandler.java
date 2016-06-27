package org.nb.mgm.handler;

import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_EXIST;
import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_ILLEGAL_PARAMETER;
import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_ILLEGAL_STATES;
import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_NOT_FOUND;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.nb.mgm.exception.MgmException;
import org.nb.mgm.model.query.MachineQuery;
import org.nb.mgm.model.runtime.ClusterRoot;
import org.nb.mgm.model.runtime.Machine;
import org.nb.mgm.service.ManagementService;
import org.origin.common.util.SearchPattern;
import org.origin.common.util.Util;

/**
 * Data handler for Machine.
 * 
 */
public class MachineHandler {

	protected ManagementService mgmService;

	/**
	 * 
	 * @param mgmService
	 */
	public MachineHandler(ManagementService mgmService) {
		this.mgmService = mgmService;
	}

	protected ClusterRoot getRoot() {
		return this.mgmService.getRoot();
	}

	/**
	 * Get all Machines.
	 * 
	 * @return
	 */
	public List<Machine> getMachines() throws MgmException {
		return getMachines(null);
	}

	/**
	 * Get Machines by query.
	 * 
	 * @param query
	 * @return
	 */
	public List<Machine> getMachines(MachineQuery query) throws MgmException {
		List<Machine> matchedMachines = new ArrayList<Machine>();

		ClusterRoot root = getRoot();
		if (query == null) {
			// Query is not available - all Machines are returned
			matchedMachines.addAll(root.getMachines());

		} else {
			// Query is available - return Machines which match the search patterns
			SearchPattern namePattern = SearchPattern.createPattern(query.getName());
			SearchPattern ipAddressPattern = SearchPattern.createPattern(query.getIpAddress());
			SearchPattern filterPattern = SearchPattern.createPattern(query.getFilter());
			// String status = query.getStatus();

			for (Iterator<Machine> machineItor = root.getMachines().iterator(); machineItor.hasNext();) {
				Machine currMachine = machineItor.next();
				boolean matchName = namePattern != null ? namePattern.matches(currMachine.getName()) : true;
				boolean matchIpAddress = ipAddressPattern != null ? ipAddressPattern.matches(currMachine.getIpAddress()) : true;
				boolean matchFilter = false;
				if (filterPattern != null) {
					if (filterPattern.matches(currMachine.getName()) || filterPattern.matches(currMachine.getIpAddress())) {
						matchFilter = true;
					}
				} else {
					matchFilter = true;
				}
				if (matchName && matchIpAddress && matchFilter) {
					matchedMachines.add(currMachine);
				}
			}
		}

		return matchedMachines;
	}

	/**
	 * Get Machine information by Id.
	 * 
	 * @param machineId
	 * @return
	 * @throws MgmException
	 */
	public Machine getMachine(String machineId) throws MgmException {
		Machine resultMachine = null;

		// Throw exception - empty Id
		if (machineId == null || machineId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Machine Id cannot be empty.", null);
		}

		// Find Machine by Id
		ClusterRoot root = getRoot();
		for (Iterator<Machine> machineItor = root.getMachines().iterator(); machineItor.hasNext();) {
			Machine currMachine = machineItor.next();
			if (machineId.equals(currMachine.getId())) {
				resultMachine = currMachine;
				break;
			}
		}

		return resultMachine;
	}

	/**
	 * Add a Machine to the cluster.
	 * 
	 * @param machine
	 * @throws MgmException
	 */
	public void addMachine(Machine machine) throws MgmException {
		// Throw exception - empty Machine
		if (machine == null) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Machine cannot be empty.", null);
		}

		// Generate unique Machine Id
		if (machine.getId() == null || machine.getId().isEmpty()) {
			machine.setId(UUID.randomUUID().toString());
		}

		// Throw exception - empty Machine name
		if (machine.getName() == null || machine.getName().isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Machine name cannot be empty.", null);
		}

		// Throw exception - empty Machine IP address
		if (machine.getIpAddress() == null || machine.getIpAddress().isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Machine IP address cannot be empty.", null);
		}

		// Throw exception - Machine with same Id or IP address exists
		ClusterRoot root = getRoot();
		for (Iterator<Machine> machineItor = root.getMachines().iterator(); machineItor.hasNext();) {
			Machine currMachine = machineItor.next();
			if (machine.getId().equals(currMachine.getId())) {
				throw new MgmException(ERROR_CODE_ENTITY_EXIST, "Machine with same Id already exists.", null);
			}
			// if (machine.getName().equals(currMachine.getName())) {
			// throw new MgmException(ERROR_CODE_ENTITY_EXIST, "Machine with same name already exists.", null);
			// }
			// if (machine.getIpAddress().equals(currMachine.getIpAddress())) {
			// throw new MgmException(ERROR_CODE_ENTITY_EXIST, "Machine with same IP address already exists.", null);
			// }
		}

		// Generate unique machine name
		String name = machine.getName();
		String uniqueName = name;
		int index = 1;
		while (true) {
			boolean nameExist = false;
			for (Iterator<Machine> machineItor = root.getMachines().iterator(); machineItor.hasNext();) {
				Machine currMachine = machineItor.next();
				if (uniqueName.equals(currMachine.getName())) {
					nameExist = true;
					break;
				}
			}
			if (nameExist) {
				uniqueName = name + "(" + index + ")";
				index++;
			} else {
				break;
			}
		}
		if (!uniqueName.equals(name)) {
			machine.setName(uniqueName);
		}

		root.addMachine(machine);
	}

	/**
	 * Update Machine information.
	 * 
	 * @param machine
	 * @throws MgmException
	 */
	public void updateMachine(Machine machine) throws MgmException {
		// Throw exception - empty Machine
		if (machine == null) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Machine cannot be empty.", null);
		}

		// Find Machine by Id
		Machine machineToUpdate = getMachine(machine.getId());

		// Throw exception - Machine not found
		if (machineToUpdate == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "Machine cannot be found.", null);
		}

		// No need to update when they are the same object.
		if (machineToUpdate == machine) {
			return;
		}

		// Machine name is changed - Update Machine name
		if (Util.compare(machineToUpdate.getName(), machine.getName()) != 0) {
			machineToUpdate.setName(machine.getName());
		}
		// Machine description is changed - Update Machine description
		if (Util.compare(machineToUpdate.getDescription(), machine.getDescription()) != 0) {
			machineToUpdate.setDescription(machine.getDescription());
		}
		// Machine IP address is changed - Update Machine IP address
		if (Util.compare(machineToUpdate.getIpAddress(), machine.getIpAddress()) != 0) {
			machineToUpdate.setIpAddress(machine.getIpAddress());
		}
	}

	/**
	 * Delete a Machine from the cluster.
	 * 
	 * @param machineId
	 * @throws MgmException
	 */
	public void deleteMachine(String machineId) throws MgmException {
		// Throw exception - empty Id
		if (machineId == null || machineId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Machine Id cannot be empty.", null);
		}

		// Find Machine by Id
		Machine machineToDelete = getMachine(machineId);

		// Throw exception - Machine not found
		if (machineToDelete == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "Machine cannot be found.", null);
		}

		// Throw exception - Machine contains Homes
		if (!machineToDelete.getHomes().isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_STATES, "Machine cannot be deleted. Please delete Homes in the Machine first.", null);
		}

		// Delete Machine from cluster root.
		ClusterRoot root = getRoot();
		root.deleteMachine(machineToDelete);
	}

	/**
	 * Get Machine properties.
	 * 
	 * @param machineId
	 * @return
	 * @throws MgmException
	 */
	public Map<String, Object> getProperties(String machineId) throws MgmException {
		Map<String, Object> properties = null;
		Machine machine = getMachine(machineId);
		if (machine != null) {
			properties = machine.getProperties();
		}
		return properties;
	}

	/**
	 * Set Machine properties.
	 * 
	 * @param machineId
	 * @param properties
	 * @throws MgmException
	 */
	public void setProperties(String machineId, Map<String, Object> properties) throws MgmException {
		Machine machine = getMachine(machineId);
		if (machine != null) {
			machine.setProperties(properties);
		}
	}

	/**
	 * Remove Machine properties.
	 * 
	 * @param machineId
	 * @param propNames
	 * @throws MgmException
	 */
	public void removeProperties(String machineId, List<String> propNames) throws MgmException {
		Machine machine = getMachine(machineId);
		if (machine != null) {
			machine.removeProperties(propNames);
		}
	}

}
