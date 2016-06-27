package org.nb.mgm.handler;

import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_EXIST;
import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_ILLEGAL_PARAMETER;
import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_ILLEGAL_STATES;
import static org.nb.mgm.service.MgmConstants.ERROR_CODE_ENTITY_NOT_FOUND;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.nb.mgm.exception.MgmException;
import org.nb.mgm.model.query.MetaSectorQuery;
import org.nb.mgm.model.runtime.ClusterRoot;
import org.nb.mgm.model.runtime.Home;
import org.nb.mgm.model.runtime.Machine;
import org.nb.mgm.model.runtime.MetaSector;
import org.nb.mgm.service.ManagementService;
import org.origin.common.util.SearchPattern;
import org.origin.common.util.Util;

/**
 * Data handler for MetaSector.
 *
 */
public class MetaSectorHandler {

	protected ManagementService mgmService;

	/**
	 * 
	 * @param mgmService
	 */
	public MetaSectorHandler(ManagementService mgmService) {
		this.mgmService = mgmService;
	}

	protected ClusterRoot getRoot() {
		return this.mgmService.getRoot();
	}

	/**
	 * Get all MetaSectors.
	 * 
	 * @return
	 */
	public List<MetaSector> getMetaSectors() throws MgmException {
		return getMetaSectors(null);
	}

	/**
	 * Get MetaSectors by query.
	 * 
	 * @param query
	 * @return
	 */
	public List<MetaSector> getMetaSectors(MetaSectorQuery query) throws MgmException {
		List<MetaSector> matchedMetaSectors = new ArrayList<MetaSector>();

		ClusterRoot root = getRoot();
		if (query == null) {
			// Query is not available - all MetaSectors are returned
			matchedMetaSectors.addAll(root.getMetaSectors());

		} else {
			// Query is available - return MetaSectors which match the search patterns
			SearchPattern namePattern = SearchPattern.createPattern(query.getName());
			SearchPattern filterPattern = SearchPattern.createPattern(query.getFilter());
			// String status = query.getStatus();

			for (Iterator<MetaSector> metaSectorItor = root.getMetaSectors().iterator(); metaSectorItor.hasNext();) {
				MetaSector currMetaSector = metaSectorItor.next();
				boolean matchName = namePattern != null ? namePattern.matches(currMetaSector.getName()) : true;
				boolean matchFilter = false;
				if (filterPattern != null) {
					if (filterPattern.matches(currMetaSector.getName())) {
						matchFilter = true;
					}
				} else {
					matchFilter = true;
				}
				if (matchName && matchFilter) {
					matchedMetaSectors.add(currMetaSector);
				}
			}
		}

		return matchedMetaSectors;
	}

	/**
	 * Get MetaSector information by Id.
	 * 
	 * @param metaSectorId
	 * @return
	 * @throws MgmException
	 */
	public MetaSector getMetaSector(String metaSectorId) throws MgmException {
		MetaSector resultMetaSector = null;

		// Throw exception - empty Id
		if (metaSectorId == null || metaSectorId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Machine Id cannot be empty.", null);
		}

		// Find MetaSector by Id
		ClusterRoot root = getRoot();
		for (Iterator<MetaSector> metaSectorItor = root.getMetaSectors().iterator(); metaSectorItor.hasNext();) {
			MetaSector currMetaSector = metaSectorItor.next();
			if (metaSectorId.equals(currMetaSector.getId())) {
				resultMetaSector = currMetaSector;
				break;
			}
		}

		return resultMetaSector;
	}

	/**
	 * Add a MetaSector to the cluster.
	 * 
	 * @param metaSector
	 * @throws MgmException
	 */
	public void addMetaSector(MetaSector metaSector) throws MgmException {
		// Throw exception - empty MetaSector
		if (metaSector == null) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "MetaSector cannot be empty.", null);
		}

		// Generate unique MetaSector Id
		if (metaSector.getId() == null || metaSector.getId().isEmpty()) {
			metaSector.setId(UUID.randomUUID().toString());
		}

		// Throw exception - empty MetaSector name
		if (metaSector.getName() == null || metaSector.getName().isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "MetaSector name cannot be empty.", null);
		}

		// Throw exception - MetaSector with same Id exists
		ClusterRoot root = getRoot();
		for (Iterator<MetaSector> metaSectorItor = root.getMetaSectors().iterator(); metaSectorItor.hasNext();) {
			MetaSector currMetaSector = metaSectorItor.next();
			if (metaSector.getId().equals(currMetaSector.getId())) {
				throw new MgmException(ERROR_CODE_ENTITY_EXIST, "MetaSector with same Id already exists.", null);
			}
			// if (metaSector.getName().equals(currMetaSector.getName())) {
			// throw new MgmException(ERROR_CODE_META_SECTOR_EXIST, "MetaSector with same name already exists.", null);
			// }
		}

		// Generate unique metaSector name
		String name = metaSector.getName();
		String uniqueName = name;
		int index = 1;
		while (true) {
			boolean nameExist = false;
			for (Iterator<MetaSector> metaSectorItor = root.getMetaSectors().iterator(); metaSectorItor.hasNext();) {
				MetaSector currMetaSector = metaSectorItor.next();
				if (uniqueName.equals(currMetaSector.getName())) {
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
			metaSector.setName(uniqueName);
		}

		root.addMetaSector(metaSector);
	}

	/**
	 * Update MetaSector information.
	 * 
	 * @param metaSector
	 * @throws MgmException
	 */
	public void updateMetaSector(MetaSector metaSector) throws MgmException {
		// Throw exception - empty Machine
		if (metaSector == null) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "MetaSector cannot be empty.", null);
		}

		// Find MetaSector by Id
		MetaSector metaSectorToUpdate = getMetaSector(metaSector.getId());

		// Throw exception - Machine not found
		if (metaSectorToUpdate == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "MetaSector cannot be found.", null);
		}

		// No need to update when they are the same object.
		if (metaSectorToUpdate == metaSector) {
			return;
		}

		// MetaSector name is changed - Update MetaSector name
		if (Util.compare(metaSectorToUpdate.getName(), metaSector.getName()) != 0) {
			metaSectorToUpdate.setName(metaSector.getName());
		}

		// MetaSector description is changed - Update MetaSector description
		if (Util.compare(metaSectorToUpdate.getDescription(), metaSector.getDescription()) != 0) {
			metaSectorToUpdate.setDescription(metaSector.getDescription());
		}
	}

	/**
	 * Delete a MetaSector from the cluster.
	 * 
	 * @param metaSectorId
	 * @throws MgmException
	 */
	public void deleteMetaSector(String metaSectorId) throws MgmException {
		// Throw exception - empty Id
		if (metaSectorId == null || metaSectorId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "MetaSector Id cannot be empty.", null);
		}

		// Find MetaSector by Id
		MetaSector metaSectorToDelete = getMetaSector(metaSectorId);

		// Throw exception - MetaSector not found
		if (metaSectorToDelete == null) {
			throw new MgmException(ERROR_CODE_ENTITY_NOT_FOUND, "MetaSector cannot be found.", null);
		}

		// Throw exception - Cannot delete the MetaSector, if it contains MetaSpaces
		if (!metaSectorToDelete.getMetaSpaces().isEmpty()) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_STATES, "MetaSector cannot be deleted. Please delete MetaSpaces in the MetaSector first.", null);
		}

		// Throw exception - Cannot delete the MetaSector, if there are Homes which join the MetaSector.
		boolean foundHomeJoinedThisMetaSector = false;
		for (Machine currMachine : this.mgmService.getMachines()) {
			for (Home currHome : this.mgmService.getHomes(currMachine.getId())) {
				for (String joinedMetaSectorId : currHome.getJoinedMetaSectorIds()) {
					if (metaSectorId.equals(joinedMetaSectorId)) {
						foundHomeJoinedThisMetaSector = true;
						break;
					}
				}
				if (foundHomeJoinedThisMetaSector) {
					break;
				}
			}
			if (foundHomeJoinedThisMetaSector) {
				break;
			}
		}
		if (foundHomeJoinedThisMetaSector) {
			throw new MgmException(ERROR_CODE_ENTITY_ILLEGAL_STATES, "MetaSector cannot be deleted. Please remove Homes which joined this MetaSector first.", null);
		}

		// Delete MetaSector from cluster root.
		ClusterRoot root = getRoot();
		root.deleteMetaSector(metaSectorToDelete);
	}

}
