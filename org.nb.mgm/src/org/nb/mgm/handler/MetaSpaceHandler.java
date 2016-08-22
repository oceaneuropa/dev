package org.nb.mgm.handler;

import static org.nb.mgm.model.ManagementConstants.ERROR_CODE_ENTITY_EXIST;
import static org.nb.mgm.model.ManagementConstants.ERROR_CODE_ENTITY_ILLEGAL_PARAMETER;
import static org.nb.mgm.model.ManagementConstants.ERROR_CODE_ENTITY_ILLEGAL_STATES;
import static org.nb.mgm.model.ManagementConstants.ERROR_CODE_ENTITY_NOT_FOUND;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.nb.mgm.model.exception.ManagementException;
import org.nb.mgm.model.query.MetaSpaceQuery;
import org.nb.mgm.model.runtime.ClusterRoot;
import org.nb.mgm.model.runtime.Home;
import org.nb.mgm.model.runtime.Machine;
import org.nb.mgm.model.runtime.MetaSector;
import org.nb.mgm.model.runtime.MetaSpace;
import org.nb.mgm.service.ManagementService;
import org.origin.common.util.SearchPattern;
import org.origin.common.util.Util;

/**
 * Data handler for MetaSpace.
 *
 */
public class MetaSpaceHandler {

	protected ManagementService mgmService;

	/**
	 * 
	 * @param mgmService
	 */
	public MetaSpaceHandler(ManagementService mgmService) {
		this.mgmService = mgmService;
	}

	protected ClusterRoot getRoot() {
		return this.mgmService.getRoot();
	}

	/**
	 * Get all MetaSpaces in a MetaSector.
	 * 
	 * @param metaSectorId
	 * @return
	 */
	public List<MetaSpace> getMetaSpaces(String metaSectorId) throws ManagementException {
		return getMetaSpaces(metaSectorId, null);
	}

	/**
	 * Get MetaSpaces in a MetaSector by query.
	 * 
	 * @param metaSectorId
	 * @param query
	 * @return
	 */
	public List<MetaSpace> getMetaSpaces(String metaSectorId, MetaSpaceQuery query) throws ManagementException {
		// Container MetaSector must exist
		MetaSector metaSector = this.mgmService.getMetaSector(metaSectorId);
		if (metaSector == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "MetaSector cannot be found.", null);
		}

		// Get all MetaSpaces in the MetaSector
		List<MetaSpace> allMetaSpacesInMetaSector = metaSector.getMetaSpaces();

		List<MetaSpace> matchedMetaSpaces = new ArrayList<MetaSpace>();
		if (query == null) {
			// Query is not available - all MetaSpaces in the MetaSector are returned
			matchedMetaSpaces.addAll(allMetaSpacesInMetaSector);

		} else {
			// Query is available - return MetaSpaces which match the search patterns
			SearchPattern namePattern = SearchPattern.createPattern(query.getName());
			SearchPattern filterPattern = SearchPattern.createPattern(query.getFilter());
			// String status = query.getStatus();

			for (Iterator<MetaSpace> metaSpaceItor = allMetaSpacesInMetaSector.iterator(); metaSpaceItor.hasNext();) {
				MetaSpace currMetaSpace = metaSpaceItor.next();
				boolean matchName = namePattern != null ? namePattern.matches(currMetaSpace.getName()) : true;
				boolean matchFilter = false;
				if (filterPattern != null) {
					if (filterPattern.matches(currMetaSpace.getName())) {
						matchFilter = true;
					}
				} else {
					matchFilter = true;
				}
				if (matchName && matchFilter) {
					matchedMetaSpaces.add(currMetaSpace);
				}
			}
		}

		return matchedMetaSpaces;
	}

	/**
	 * Get MetaSpace information by Id.
	 * 
	 * @param metaSpaceId
	 * @return
	 */
	public MetaSpace getMetaSpace(String metaSpaceId) throws ManagementException {
		MetaSpace resultMetaSpace = null;

		// Throw exception - empty Id
		if (metaSpaceId == null || metaSpaceId.isEmpty()) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "MetaSpace Id cannot be empty.", null);
		}

		// Iterate though all MetaSectors and then all MetaSpaces in each MetaSector. Find the MetaSpace with matching Id.
		for (Iterator<MetaSector> metaSectorItor = this.mgmService.getMetaSectors().iterator(); metaSectorItor.hasNext();) {
			MetaSector currMetaSector = metaSectorItor.next();

			for (Iterator<MetaSpace> metaSpaceItor = currMetaSector.getMetaSpaces().iterator(); metaSpaceItor.hasNext();) {
				MetaSpace currMetaSpace = metaSpaceItor.next();
				if (metaSpaceId.equals(currMetaSpace.getId())) {
					resultMetaSpace = currMetaSpace;
					break;
				}
			}

			if (resultMetaSpace != null) {
				break;
			}
		}

		return resultMetaSpace;
	}

	/**
	 * Add a MetaSpace to a MetaSector.
	 * 
	 * @param metaSectorId
	 * @param metaSpace
	 * @throws ManagementException
	 */
	public void addMetaSpace(String metaSectorId, MetaSpace metaSpace) throws ManagementException {
		// Container MetaSector must exist
		MetaSector metaSector = this.mgmService.getMetaSector(metaSectorId);
		if (metaSector == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "MetaSector cannot be found.", null);
		}

		// Generate unique MetaSpace Id
		if (metaSpace.getId() == null || metaSpace.getId().isEmpty()) {
			metaSpace.setId(UUID.randomUUID().toString());
		}

		// Throw exception - empty MetaSpace name
		if (metaSpace.getName() == null || metaSpace.getName().isEmpty()) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "MetaSpace name cannot be empty.", null);
		}

		// Throw exception - MetaSpace with same Id exists
		List<MetaSpace> allMetaSpacesInMetaSector = metaSector.getMetaSpaces();
		for (Iterator<MetaSpace> metaSpaceItor = allMetaSpacesInMetaSector.iterator(); metaSpaceItor.hasNext();) {
			MetaSpace currMetaSpace = metaSpaceItor.next();

			if (metaSpace.getId().equals(currMetaSpace.getId())) {
				throw new ManagementException(ERROR_CODE_ENTITY_EXIST, "MetaSpace with same Id already exists.", null);
			}
			// if (metaSpace.getName().equals(currMetaSpace.getName())) {
			// throw new MgmException(ERROR_CODE_META_SPACE_EXIST, "MetaSpace with same name already exists.", null);
			// }
		}

		// Generate unique metaSpace name
		String name = metaSpace.getName();
		String uniqueName = name;
		int index = 1;
		while (true) {
			boolean nameExist = false;
			for (Iterator<MetaSpace> metaSpaceItor = allMetaSpacesInMetaSector.iterator(); metaSpaceItor.hasNext();) {
				MetaSpace currMetaSpace = metaSpaceItor.next();
				if (uniqueName.equals(currMetaSpace.getName())) {
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
			metaSpace.setName(uniqueName);
		}

		metaSector.addMetaSpace(metaSpace);
	}

	/**
	 * Update MetaSpace information.
	 * 
	 * @param metaSpace
	 * @throws ManagementException
	 */
	public void updateMetaSpace(MetaSpace metaSpace) throws ManagementException {
		// Throw exception - empty MetaSpace
		if (metaSpace == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "MetaSpace cannot be empty.", null);
		}

		// Find MetaSpace by Id
		MetaSpace metaSpaceToUpdate = getMetaSpace(metaSpace.getId());

		// No need to update when they are the same object.
		if (metaSpaceToUpdate == metaSpace) {
			return;
		}

		// MetaSpace name is changed - Update MetaSpace name
		if (Util.compare(metaSpaceToUpdate.getName(), metaSpace.getName()) != 0) {
			metaSpaceToUpdate.setName(metaSpace.getName());
		}
		// MetaSpace description is changed - Update MetaSpace description
		if (Util.compare(metaSpaceToUpdate.getDescription(), metaSpace.getDescription()) != 0) {
			metaSpaceToUpdate.setDescription(metaSpace.getDescription());
		}
	}

	/**
	 * Delete a MetaSpace from a MetaSector.
	 * 
	 * @param metaSpaceId
	 * @throws ManagementException
	 */
	public void deleteMetaSpace(String metaSpaceId) throws ManagementException {
		// Throw exception - empty Id
		if (metaSpaceId == null || metaSpaceId.isEmpty()) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "MetaSpace Id cannot be empty.", null);
		}

		// Find MetaSpace by Id
		MetaSpace metaSpaceToDelete = getMetaSpace(metaSpaceId);

		// Throw exception - MetaSpace not found
		if (metaSpaceToDelete == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "MetaSpace cannot be found.", null);
		}

		// Check whether a MetaSpace can be deleted.
		checkDelete(metaSpaceId);

		// Delete MetaSpace from MetaSector.
		MetaSector metaSector = metaSpaceToDelete.getMetaSector();
		metaSector.deleteMetaSpace(metaSpaceToDelete);
	}

	/**
	 * Check whether a MetaSpace can be deleted.
	 * 
	 * @param metaSpaceId
	 * @throws ManagementException
	 */
	public void checkDelete(String metaSpaceId) throws ManagementException {
		// 1. Throw exception - Cannot delete MetaSpace, if there are Homes which join the MetaSpace.
		boolean foundHomeJoinedThisMetaSpace = false;
		for (Machine currMachine : this.mgmService.getMachines()) {
			for (Home currHome : this.mgmService.getHomes(currMachine.getId())) {
				for (String joinedMetaSpaceId : currHome.getJoinedMetaSpaceIds()) {
					if (metaSpaceId.equals(joinedMetaSpaceId)) {
						foundHomeJoinedThisMetaSpace = true;
						break;
					}
				}
				if (foundHomeJoinedThisMetaSpace) {
					break;
				}
			}
			if (foundHomeJoinedThisMetaSpace) {
				break;
			}
		}
		if (foundHomeJoinedThisMetaSpace) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_STATES, "MetaSpace cannot be deleted. Please remove Homes which joined this MetaSpace first.", null);
		}
	}

}
