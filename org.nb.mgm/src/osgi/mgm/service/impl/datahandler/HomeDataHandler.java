package osgi.mgm.service.impl.datahandler;

import static osgi.mgm.service.MgmConstants.ERROR_CODE_HOME_EXIST;
import static osgi.mgm.service.MgmConstants.ERROR_CODE_HOME_ILLEGAL_PARAMETER;
import static osgi.mgm.service.MgmConstants.ERROR_CODE_HOME_ILLEGAL_STATES;
import static osgi.mgm.service.MgmConstants.ERROR_CODE_HOME_NOT_FOUND;
import static osgi.mgm.service.MgmConstants.ERROR_CODE_MACHINE_NOT_FOUND;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import osgi.mgm.service.MgmException;
import osgi.mgm.service.MgmService;
import osgi.mgm.service.model.ClusterRoot;
import osgi.mgm.service.model.Home;
import osgi.mgm.service.model.HomeQuery;
import osgi.mgm.service.model.Machine;
import osgi.mgm.service.model.MetaSector;
import osgi.mgm.service.model.MetaSpace;
import osgi.mgm.util.SearchPattern;
import osgi.mgm.util.Util;

/**
 * Data handler for Home.
 * 
 */
public class HomeDataHandler {

	protected MgmService mgmService;

	/**
	 * 
	 * @param mgmService
	 */
	public HomeDataHandler(MgmService mgmService) {
		this.mgmService = mgmService;
	}

	protected ClusterRoot getRoot() {
		return this.mgmService.getRoot();
	}

	/**
	 * Get all Homes in a Machine.
	 * 
	 * @param machineId
	 * @return
	 */
	public List<Home> getHomes(String machineId) throws MgmException {
		return getHomes(machineId, null);
	}

	/**
	 * Get Homes in a Machine by query.
	 * 
	 * @param machineId
	 * @param query
	 * @return
	 */
	public List<Home> getHomes(String machineId, HomeQuery query) throws MgmException {
		// Container Machine must exist
		Machine machine = this.mgmService.getMachine(machineId);
		if (machine == null) {
			throw new MgmException(ERROR_CODE_MACHINE_NOT_FOUND, "Machine cannot be found.", null);
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
			SearchPattern urlPattern = SearchPattern.createPattern(query.getUrl());
			SearchPattern filterPattern = SearchPattern.createPattern(query.getFilter());
			// String status = query.getStatus();

			for (Iterator<Home> homeItor = allHomesInMachine.iterator(); homeItor.hasNext();) {
				Home currHome = homeItor.next();
				boolean matchName = namePattern != null ? namePattern.matches(currHome.getName()) : true;
				boolean matchUrl = urlPattern != null ? urlPattern.matches(currHome.getUrl()) : true;
				boolean matchFilter = false;
				if (filterPattern != null) {
					if (filterPattern.matches(currHome.getName()) || filterPattern.matches(currHome.getUrl())) {
						matchFilter = true;
					}
				} else {
					matchFilter = true;
				}
				if (matchName && matchUrl && matchFilter) {
					matchedHomes.add(currHome);
				}
			}
		}

		return matchedHomes;
	}

	/**
	 * Get Home information by Home Id.
	 * 
	 * @param homeId
	 * @return
	 */
	public Home getHome(String homeId) throws MgmException {
		Home resultHome = null;

		// Throw exception - empty Id
		if (homeId == null || homeId.isEmpty()) {
			throw new MgmException(ERROR_CODE_HOME_ILLEGAL_PARAMETER, "Home Id cannot be empty.", null);
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
	 * @param home
	 * @throws MgmException
	 */
	public void addHome(String machineId, Home home) throws MgmException {
		// Container Machine must exist
		Machine machine = this.mgmService.getMachine(machineId);
		if (machine == null) {
			throw new MgmException(ERROR_CODE_MACHINE_NOT_FOUND, "Machine cannot be found.", null);
		}

		// Generate unique Home Id
		if (home.getId() == null || home.getId().isEmpty()) {
			home.setId(UUID.randomUUID().toString());
		}

		// Throw exception - empty Home name
		if (home.getName() == null || home.getName().isEmpty()) {
			throw new MgmException(ERROR_CODE_HOME_ILLEGAL_PARAMETER, "Home name cannot be empty.", null);
		}

		// Throw exception - empty Home URL
		if (home.getUrl() == null || home.getUrl().isEmpty()) {
			throw new MgmException(ERROR_CODE_HOME_ILLEGAL_PARAMETER, "Home URL cannot be empty.", null);
		}

		// Throw exception - Home with same Id or URL exists
		List<Home> allHomesInMachine = machine.getHomes();
		for (Iterator<Home> homeItor = allHomesInMachine.iterator(); homeItor.hasNext();) {
			Home currHome = homeItor.next();

			if (home.getId().equals(currHome.getId())) {
				throw new MgmException(ERROR_CODE_HOME_EXIST, "Home with same Id already exists.", null);
			}
			// if (home.getName().equals(currHome.getName())) {
			// throw new MgmException(ERROR_CODE_HOME_EXIST, "Home with same name already exists.", null);
			// }
			if (home.getUrl().equals(currHome.getUrl())) {
				throw new MgmException(ERROR_CODE_HOME_EXIST, "Home with same URL already exists.", null);
			}
		}

		// Generate unique home name
		String name = home.getName();
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
			home.setName(uniqueName);
		}

		machine.addHome(home);
	}

	/**
	 * Update Home information.
	 * 
	 * @param home
	 * @throws MgmException
	 */
	public void updateHome(Home home) throws MgmException {
		// Throw exception - empty Home
		if (home == null) {
			throw new MgmException(ERROR_CODE_HOME_ILLEGAL_PARAMETER, "Home cannot be empty.", null);
		}

		// Find Home by Id
		Home homeToUpdate = getHome(home.getId());

		// No need to update when they are the same object.
		if (homeToUpdate == home) {
			return;
		}

		// Home name is changed - Update Home name
		if (Util.compare(homeToUpdate.getName(), home.getName()) != 0) {
			homeToUpdate.setName(home.getName());
		}
		// Home description is changed - Update Home description
		if (Util.compare(homeToUpdate.getDescription(), home.getDescription()) != 0) {
			homeToUpdate.setDescription(home.getDescription());
		}
		// Home URL is changed - Update Home URL
		if (Util.compare(homeToUpdate.getUrl(), home.getUrl()) != 0) {
			homeToUpdate.setUrl(home.getUrl());
		}
	}

	/**
	 * Delete a Home from a Machine.
	 * 
	 * @param homeId
	 * @throws MgmException
	 */
	public void deleteHome(String homeId) throws MgmException {
		// Throw exception - empty Id
		if (homeId == null || homeId.isEmpty()) {
			throw new MgmException(ERROR_CODE_HOME_ILLEGAL_PARAMETER, "Home Id cannot be empty.", null);
		}

		// Find Home by Id
		Home homeToDelete = getHome(homeId);

		// Throw exception - Machine not found
		if (homeToDelete == null) {
			throw new MgmException(ERROR_CODE_HOME_NOT_FOUND, "Home cannot be found.", null);
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
			throw new MgmException(ERROR_CODE_HOME_ILLEGAL_STATES, "Home cannot be deleted. Please remove the Home from the MetaSectors first.", null);
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
			throw new MgmException(ERROR_CODE_HOME_ILLEGAL_STATES, "Home cannot be deleted. Please remove the Home from the MetaSpaces first.", null);
		}

		// Delete Home from Machine.
		Machine machine = homeToDelete.getMachine();
		if (machine != null) {
			machine.deleteHome(homeToDelete);
		}
	}

}
