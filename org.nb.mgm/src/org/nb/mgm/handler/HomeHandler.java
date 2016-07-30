package org.nb.mgm.handler;

import static org.nb.mgm.service.ManagementConstants.ERROR_CODE_ENTITY_EXIST;
import static org.nb.mgm.service.ManagementConstants.ERROR_CODE_ENTITY_ILLEGAL_PARAMETER;
import static org.nb.mgm.service.ManagementConstants.ERROR_CODE_ENTITY_ILLEGAL_STATES;
import static org.nb.mgm.service.ManagementConstants.ERROR_CODE_ENTITY_NOT_FOUND;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.nb.mgm.exception.ManagementException;
import org.nb.mgm.model.query.HomeQuery;
import org.nb.mgm.model.runtime.Home;
import org.nb.mgm.model.runtime.Machine;
import org.nb.mgm.model.runtime.MetaSector;
import org.nb.mgm.model.runtime.MetaSpace;
import org.nb.mgm.service.ManagementService;
import org.origin.common.util.SearchPattern;
import org.origin.common.util.Util;

/**
 * Data handler for Home.
 * 
 */
public class HomeHandler {

	protected ManagementService mgmService;

	/**
	 * 
	 * @param mgmService
	 */
	public HomeHandler(ManagementService mgmService) {
		this.mgmService = mgmService;
	}

	/**
	 * Get Homes.
	 * 
	 * @param machineId
	 * @return
	 */
	public List<Home> getHomes(String machineId) throws ManagementException {
		return getHomes(machineId, null);
	}

	/**
	 * Get Homes.
	 * 
	 * @param machineId
	 * @param query
	 * @return
	 */
	public List<Home> getHomes(String machineId, HomeQuery query) throws ManagementException {
		// Container Machine must exist
		Machine machine = this.mgmService.getMachine(machineId);
		if (machine == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "Machine cannot be found.", null);
		}

		// Get all Homes in the Machine
		List<Home> allHomesInMachine = machine.getHomes();

		List<Home> matchedHomes = new ArrayList<Home>();
		if (query == null) {
			// Query is not available - all Homes in the Machine are returned
			matchedHomes.addAll(allHomesInMachine);

		} else {
			// Query is available - return Homes which match the search patterns
			SearchPattern namePattern = SearchPattern.createPattern(query.getName());
			SearchPattern filterPattern = SearchPattern.createPattern(query.getFilter());
			// String status = query.getStatus();

			for (Iterator<Home> homeItor = allHomesInMachine.iterator(); homeItor.hasNext();) {
				Home currHome = homeItor.next();
				boolean matchName = namePattern != null ? namePattern.matches(currHome.getName()) : true;
				boolean matchFilter = false;
				if (filterPattern != null) {
					if (filterPattern.matches(currHome.getName())) {
						matchFilter = true;
					}
				} else {
					matchFilter = true;
				}
				if (matchName && matchFilter) {
					matchedHomes.add(currHome);
				}
			}
		}

		return matchedHomes;
	}

	/**
	 * Get a Home.
	 * 
	 * @param homeId
	 * @return
	 */
	public Home getHome(String homeId) throws ManagementException {
		Home resultHome = null;

		// Throw exception - empty Id
		if (homeId == null || homeId.isEmpty()) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Home Id cannot be empty.", null);
		}

		// Iterate though all Machines and then all Homes in each Machine. Find the Home with matching Id.
		for (Iterator<Machine> machineItor = this.mgmService.getMachines().iterator(); machineItor.hasNext();) {
			Machine currMachine = machineItor.next();

			for (Iterator<Home> homeItor = currMachine.getHomes().iterator(); homeItor.hasNext();) {
				Home currHome = homeItor.next();
				if (homeId.equals(currHome.getId())) {
					resultHome = currHome;
					break;
				}
			}

			if (resultHome != null) {
				break;
			}
		}

		return resultHome;
	}

	/**
	 * Add a Home to a Machine.
	 * 
	 * @param machineId
	 * @param newHomeRequest
	 * @throws ManagementException
	 */
	public Home addHome(String machineId, Home newHomeRequest) throws ManagementException {
		// Container Machine must exist
		Machine machine = this.mgmService.getMachine(machineId);
		if (machine == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "Machine cannot be found.", null);
		}

		// Generate unique Home Id
		if (newHomeRequest.getId() == null || newHomeRequest.getId().isEmpty()) {
			newHomeRequest.setId(UUID.randomUUID().toString());
		}

		// Throw exception - empty Home name
		if (newHomeRequest.getName() == null || newHomeRequest.getName().isEmpty()) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Home name cannot be empty.", null);
		}

		// Throw exception - Home with same Id or URL exists
		List<Home> allHomesInMachine = machine.getHomes();
		for (Iterator<Home> homeItor = allHomesInMachine.iterator(); homeItor.hasNext();) {
			Home currHome = homeItor.next();

			if (newHomeRequest.getId().equals(currHome.getId())) {
				throw new ManagementException(ERROR_CODE_ENTITY_EXIST, "Home with same Id already exists.", null);
			}
			// if (home.getName().equals(currHome.getName())) {
			// throw new MgmException(ERROR_CODE_ENTITY_EXIST, "Home with same name already exists.", null);
			// }
			// if (home.getUrl().equals(currHome.getUrl())) {
			// throw new MgmException(ERROR_CODE_ENTITY_EXIST, "Home with same URL already exists.", null);
			// }
		}

		// Generate unique home name
		String name = newHomeRequest.getName();
		String uniqueName = name;
		int index = 1;
		while (true) {
			boolean nameExist = false;
			for (Iterator<Home> homeItor = allHomesInMachine.iterator(); homeItor.hasNext();) {
				Home currHome = homeItor.next();
				if (uniqueName.equals(currHome.getName())) {
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
			newHomeRequest.setName(uniqueName);
		}

		machine.addHome(newHomeRequest);

		return newHomeRequest;
	}

	/**
	 * Update Home.
	 * 
	 * @param updateHomeRequest
	 * @throws ManagementException
	 */
	public void updateHome(Home updateHomeRequest) throws ManagementException {
		// Throw exception - empty Home
		if (updateHomeRequest == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Home cannot be empty.", null);
		}

		// Find Home by Id
		Home homeToUpdate = getHome(updateHomeRequest.getId());

		// No need to update when they are the same object.
		if (homeToUpdate == updateHomeRequest) {
			return;
		}

		// Update name
		if (Util.compare(homeToUpdate.getName(), updateHomeRequest.getName()) != 0) {
			homeToUpdate.setName(updateHomeRequest.getName());
		}
		// Update description
		if (Util.compare(homeToUpdate.getDescription(), updateHomeRequest.getDescription()) != 0) {
			homeToUpdate.setDescription(updateHomeRequest.getDescription());
		}
	}

	/**
	 * Delete a Home from a Machine.
	 * 
	 * @param homeId
	 * @throws ManagementException
	 */
	public void deleteHome(String homeId) throws ManagementException {
		// Throw exception - empty Id
		if (homeId == null || homeId.isEmpty()) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Home Id cannot be empty.", null);
		}

		// Find Home by Id
		Home homeToDelete = getHome(homeId);

		// Throw exception - Home not found
		if (homeToDelete == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "Home cannot be found.", null);
		}

		// Throw exception - Cannot delete Home, if it joins MetaSector or MetaSpace.
		boolean foundJoinedMetaSectors = false;
		for (String joinedMetaSectorId : homeToDelete.getJoinedMetaSectorIds()) {
			MetaSector joinedMetaSector = this.mgmService.getMetaSector(joinedMetaSectorId);
			if (joinedMetaSector != null) {
				foundJoinedMetaSectors = true;
				break;
			}
		}
		if (foundJoinedMetaSectors) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_STATES, "Home cannot be deleted. Please remove the Home from the MetaSectors first.", null);
		}

		boolean foundJoinedMetaSpaces = false;
		for (String joinedMetaSpaceId : homeToDelete.getJoinedMetaSpaceIds()) {
			MetaSpace joinedMetaSpace = this.mgmService.getMetaSpace(joinedMetaSpaceId);
			if (joinedMetaSpace != null) {
				foundJoinedMetaSpaces = true;
				break;
			}
		}
		if (foundJoinedMetaSpaces) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_STATES, "Home cannot be deleted. Please remove the Home from the MetaSpaces first.", null);
		}

		// Delete Home from Machine.
		Machine machine = homeToDelete.getMachine();
		if (machine != null) {
			machine.deleteHome(homeToDelete);
		}
	}

	/**
	 * Get Home properties.
	 * 
	 * @param homeId
	 * @return
	 * @throws ManagementException
	 */
	public Map<String, Object> getProperties(String homeId) throws ManagementException {
		Map<String, Object> properties = null;
		Home home = getHome(homeId);
		if (home != null) {
			properties = home.getProperties();
		}
		return properties;
	}

	/**
	 * Set Home properties.
	 * 
	 * @param homeId
	 * @param properties
	 * @throws ManagementException
	 */
	public void setProperties(String homeId, Map<String, Object> properties) throws ManagementException {
		Home home = getHome(homeId);
		if (home != null) {
			home.setProperties(properties);
		}
	}

	/**
	 * Remove Home properties.
	 * 
	 * @param homeId
	 * @param propNames
	 * @throws ManagementException
	 */
	public void removeProperties(String homeId, List<String> propNames) throws ManagementException {
		Home home = getHome(homeId);
		if (home != null) {
			home.removeProperties(propNames);
		}
	}

}
