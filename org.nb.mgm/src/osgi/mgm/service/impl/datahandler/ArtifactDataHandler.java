package osgi.mgm.service.impl.datahandler;

import static osgi.mgm.service.MgmConstants.ERROR_CODE_ARTIFACT_EXIST;
import static osgi.mgm.service.MgmConstants.ERROR_CODE_ARTIFACT_ILLEGAL_PARAMETER;
import static osgi.mgm.service.MgmConstants.ERROR_CODE_ARTIFACT_ILLEGAL_STATES;
import static osgi.mgm.service.MgmConstants.ERROR_CODE_ARTIFACT_NOT_FOUND;
import static osgi.mgm.service.MgmConstants.ERROR_CODE_META_SECTOR_NOT_FOUND;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import osgi.mgm.common.util.SearchPattern;
import osgi.mgm.common.util.Util;
import osgi.mgm.service.MgmException;
import osgi.mgm.service.MgmService;
import osgi.mgm.service.model.Artifact;
import osgi.mgm.service.model.ArtifactQuery;
import osgi.mgm.service.model.ClusterRoot;
import osgi.mgm.service.model.MetaSector;
import osgi.mgm.service.model.MetaSpace;

/**
 * Data handler for Artifact.
 * 
 */
public class ArtifactDataHandler {

	protected MgmService mgmService;

	/**
	 * 
	 * @param mgmService
	 */
	public ArtifactDataHandler(MgmService mgmService) {
		this.mgmService = mgmService;
	}

	protected ClusterRoot getRoot() {
		return this.mgmService.getRoot();
	}

	/**
	 * Get all Artifacts in a MetaSector.
	 * 
	 * @param metaSectorId
	 * @return
	 */
	public List<Artifact> getArtifacts(String metaSectorId) throws MgmException {
		return getArtifacts(metaSectorId, null);
	}

	/**
	 * Get Artifacts in a MetaSector by query.
	 * 
	 * @param metaSectorId
	 * @param query
	 * @return
	 */
	public List<Artifact> getArtifacts(String metaSectorId, ArtifactQuery query) throws MgmException {
		// Container Machine must exist
		MetaSector metaSector = this.mgmService.getMetaSector(metaSectorId);
		if (metaSector == null) {
			throw new MgmException(ERROR_CODE_META_SECTOR_NOT_FOUND, "MetaSector cannot be found.", null);
		}

		// Get all Artifact in the MetaSector
		List<Artifact> allArtifactsInMetaSector = metaSector.getArtifacts();

		List<Artifact> matchedArtifacts = new ArrayList<Artifact>();
		if (query == null) {
			// Query is not available - all Artifacts in the MetaSector are returned
			matchedArtifacts.addAll(allArtifactsInMetaSector);

		} else {
			// Query is available - return Artifacts which match the search patterns
			SearchPattern namePattern = SearchPattern.createPattern(query.getName());
			SearchPattern versionPattern = SearchPattern.createPattern(query.getVersion());
			SearchPattern filePathPattern = SearchPattern.createPattern(query.getFilePath());
			SearchPattern fileNamePattern = SearchPattern.createPattern(query.getFileName());
			SearchPattern filterPattern = SearchPattern.createPattern(query.getFilter());
			// String status = query.getStatus();

			for (Iterator<Artifact> artifactItor = allArtifactsInMetaSector.iterator(); artifactItor.hasNext();) {
				Artifact currArtifact = artifactItor.next();
				boolean matchName = namePattern != null ? namePattern.matches(currArtifact.getName()) : true;
				boolean matchVersion = versionPattern != null ? versionPattern.matches(currArtifact.getVersion()) : true;
				boolean matchFilePath = filePathPattern != null ? filePathPattern.matches(currArtifact.getFilePath()) : true;
				boolean matchFileName = fileNamePattern != null ? fileNamePattern.matches(currArtifact.getFileName()) : true;

				boolean matchFilter = false;
				if (filterPattern != null) {
					if (filterPattern.matches(currArtifact.getName()) //
							|| filterPattern.matches(currArtifact.getVersion()) //
							|| filterPattern.matches(currArtifact.getFilePath()) //
							|| filterPattern.matches(currArtifact.getFileName()) //
					) {
						matchFilter = true;
					}
				} else {
					matchFilter = true;
				}
				if (matchName && matchVersion && matchFilePath && matchFileName && matchFilter) {
					matchedArtifacts.add(currArtifact);
				}
			}
		}

		return matchedArtifacts;
	}

	/**
	 * Get Artifact information by Artifact Id.
	 * 
	 * @param artifactId
	 * @return
	 */
	public Artifact getArtifact(String artifactId) throws MgmException {
		Artifact resultArtifact = null;

		// Throw exception - empty Id
		if (artifactId == null || artifactId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ARTIFACT_ILLEGAL_PARAMETER, "Artifact Id cannot be empty.", null);
		}

		// Iterate though all MetaSectors and then all Artifacts in each MetaSector. Find the Artifact with matching Id.
		for (Iterator<MetaSector> metaSectorItor = this.mgmService.getMetaSectors().iterator(); metaSectorItor.hasNext();) {
			MetaSector currMetaSector = metaSectorItor.next();

			for (Iterator<Artifact> artifactItor = currMetaSector.getArtifacts().iterator(); artifactItor.hasNext();) {
				Artifact currArtifact = artifactItor.next();
				if (artifactId.equals(currArtifact.getId())) {
					resultArtifact = currArtifact;
					break;
				}
			}

			if (resultArtifact != null) {
				break;
			}
		}

		return resultArtifact;
	}

	/**
	 * Add a Artifact to a MetaSector.
	 * 
	 * @param metaSectorId
	 * @param artifact
	 * @throws MgmException
	 */
	public void addArtifact(String metaSectorId, Artifact artifact) throws MgmException {
		// Container MetaSector must exist
		MetaSector metaSector = this.mgmService.getMetaSector(metaSectorId);
		if (metaSector == null) {
			throw new MgmException(ERROR_CODE_META_SECTOR_NOT_FOUND, "MetaSector cannot be found.", null);
		}

		// Generate unique Artifact Id
		if (artifact.getId() == null || artifact.getId().isEmpty()) {
			artifact.setId(UUID.randomUUID().toString());
		}

		// Throw exception - empty Artifact name
		if (artifact.getName() == null || artifact.getName().isEmpty()) {
			throw new MgmException(ERROR_CODE_ARTIFACT_ILLEGAL_PARAMETER, "Artifact name cannot be empty.", null);
		}

		// Throw exception - Artifact with same Id or same name+version or same filePath exists
		String id = artifact.getId();
		String name = artifact.getName();
		String version = artifact.getVersion();
		String filePath = artifact.getFilePath();

		List<Artifact> allArtifactInMetaSector = metaSector.getArtifacts();
		for (Iterator<Artifact> artifactItor = allArtifactInMetaSector.iterator(); artifactItor.hasNext();) {
			Artifact currArtifact = artifactItor.next();

			String currId = currArtifact.getId();
			String currName = currArtifact.getName();
			String currVersion = currArtifact.getVersion();
			String currFilePath = currArtifact.getFilePath();

			// Check Id exits
			if (id != null && id.equals(currId)) {
				throw new MgmException(ERROR_CODE_ARTIFACT_EXIST, "Artifact with same Id already exists.", null);
			}

			// Check name + version exists
			if (name != null && name.equals(currName)) {
				boolean sameVersion = false;
				if (version != null && version.equals(currVersion)) {
					sameVersion = true;
				} else if (version == null && currVersion == null) {
					sameVersion = true;
				}
				if (sameVersion) {
					throw new MgmException(ERROR_CODE_ARTIFACT_EXIST, "Artifact with same name and version already exists.", null);
				}
			}

			// Check filePath exists
			if (filePath != null && filePath.equals(currFilePath)) {
				throw new MgmException(ERROR_CODE_ARTIFACT_EXIST, "Artifact with same file path already exists.", null);
			}
		}

		metaSector.addArtifact(artifact);
	}

	/**
	 * Update Artifact information.
	 * 
	 * @param artifact
	 * @throws MgmException
	 */
	public void updateArtifact(Artifact artifact) throws MgmException {
		// Throw exception - empty Artifact
		if (artifact == null) {
			throw new MgmException(ERROR_CODE_ARTIFACT_ILLEGAL_PARAMETER, "Artifact cannot be empty.", null);
		}

		// Find Artifact by Id
		Artifact artifactToUpdate = getArtifact(artifact.getId());

		// No need to update when they are the same object.
		if (artifactToUpdate == artifact) {
			return;
		}

		// Artifact name is changed - Update Artifact name
		if (Util.compare(artifactToUpdate.getName(), artifact.getName()) != 0) {
			artifactToUpdate.setName(artifact.getName());
		}
		// Artifact description is changed - Update Artifact description
		if (Util.compare(artifactToUpdate.getDescription(), artifact.getDescription()) != 0) {
			artifactToUpdate.setDescription(artifact.getDescription());
		}
		// Artifact version is changed - Update Artifact version
		if (Util.compare(artifactToUpdate.getVersion(), artifact.getVersion()) != 0) {
			artifactToUpdate.setVersion(artifact.getVersion());
		}
		// Artifact filePath is changed - Update Artifact filePath
		if (Util.compare(artifactToUpdate.getFilePath(), artifact.getFilePath()) != 0) {
			artifactToUpdate.setFilePath(artifact.getFilePath());
		}
		// Artifact fileName is changed - Update Artifact fileName
		if (Util.compare(artifactToUpdate.getFileName(), artifact.getFileName()) != 0) {
			artifactToUpdate.setFileName(artifact.getFileName());
		}
		// Artifact fileSize is changed - Update Artifact fileSize
		if (Util.compare(artifactToUpdate.getFileSize(), artifact.getFileSize()) != 0) {
			artifactToUpdate.setFileSize(artifact.getFileSize());
		}
		// Artifact checksum is changed - Update Artifact checksum
		if (Util.compare(artifactToUpdate.getChecksum(), artifact.getChecksum()) != 0) {
			artifactToUpdate.setChecksum(artifact.getChecksum());
		}
	}

	/**
	 * Delete a Artifact from a MetaSector.
	 * 
	 * @param artifactId
	 * @throws MgmException
	 */
	public void deleteArtifact(String artifactId) throws MgmException {
		// Throw exception - empty Id
		if (artifactId == null || artifactId.isEmpty()) {
			throw new MgmException(ERROR_CODE_ARTIFACT_ILLEGAL_PARAMETER, "Artifact Id cannot be empty.", null);
		}

		// Find Artifact by Id
		Artifact artifactToDelete = getArtifact(artifactId);

		// Throw exception - Artifact not found
		if (artifactToDelete == null) {
			throw new MgmException(ERROR_CODE_ARTIFACT_NOT_FOUND, "Artifact cannot be found.", null);
		}

		// Check whether a MetaSpace can be deleted.
		checkDelete(artifactId);

		// Delete Artifact from MetaSector.
		MetaSector metaSector = artifactToDelete.getMetaSector();
		metaSector.deleteArtifact(artifactToDelete);
	}

	/**
	 * Check whether an Artifact can be deleted.
	 * 
	 * @param artifactId
	 * @return
	 * @throws MgmException
	 */
	public void checkDelete(String artifactId) throws MgmException {
		// 1. Throw exception - Cannot delete Artifact, if the Artifact has been deployed to any MetaSpace.
		boolean foundDeployedToMetaSpace = false;
		for (MetaSector currMetaSector : this.mgmService.getMetaSectors()) {
			for (MetaSpace currMetaSpace : this.mgmService.getMetaSpaces(currMetaSector.getId())) {
				for (String deployedArtifactId : currMetaSpace.getDeployedArtifactIds()) {
					if (artifactId.equals(deployedArtifactId)) {
						foundDeployedToMetaSpace = true;
						break;
					}
				}
				if (foundDeployedToMetaSpace) {
					break;
				}
			}
			if (foundDeployedToMetaSpace) {
				break;
			}
		}
		if (foundDeployedToMetaSpace) {
			throw new MgmException(ERROR_CODE_ARTIFACT_ILLEGAL_STATES, "Artifact cannot be deleted. Please undeploy the Artifact from MetaSpace first.", null);
		}
	}

}
