package org.nb.mgm.handler;

import static org.nb.mgm.model.ManagementConstants.ERROR_CODE_ENTITY_EXIST;
import static org.nb.mgm.model.ManagementConstants.ERROR_CODE_ENTITY_ILLEGAL_PARAMETER;
import static org.nb.mgm.model.ManagementConstants.ERROR_CODE_ENTITY_NOT_FOUND;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.nb.mgm.model.exception.ManagementException;
import org.nb.mgm.model.runtime.Project;
import org.nb.mgm.model.runtime.Software;
import org.nb.mgm.service.ManagementService;
import org.origin.common.io.FileUtil;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.util.Util;

public class ProjectSoftwareHandler {

	protected ManagementService mgmService;

	/**
	 * 
	 * @param mgmService
	 */
	public ProjectSoftwareHandler(ManagementService mgmService) {
		this.mgmService = mgmService;
	}

	/**
	 * Get Software list in a Project.
	 * 
	 * @param projectId
	 * @return
	 * @throws ManagementException
	 */
	public List<Software> getProjectSoftware(String projectId) throws ManagementException {
		// Throw exception - empty Id
		checkProjectId(projectId);

		// Container Project must exist
		Project project = this.mgmService.getProject(projectId);
		checkContainerProject(project);

		List<Software> softwareList = project.getSoftware();

		List<Software> resultSoftwareList = new ArrayList<Software>();
		resultSoftwareList.addAll(softwareList);

		return resultSoftwareList;
	}

	/**
	 * Get Software.
	 * 
	 * @param projectId
	 * @param softwareId
	 * @return
	 * @throws ManagementException
	 */
	public Software getProjectSoftware(String projectId, String softwareId) throws ManagementException {
		// Throw exception - empty Id
		checkProjectId(projectId);
		checkSoftwareId(softwareId);

		// Container Project must exist
		Project project = this.mgmService.getProject(projectId);
		checkContainerProject(project);

		Software resultSoftware = null;

		// Iterate all Software in a Project. Find the Software with matching Id.
		for (Iterator<Software> softwareItor = project.getSoftware().iterator(); softwareItor.hasNext();) {
			Software currSoftware = softwareItor.next();
			if (softwareId.equals(currSoftware.getId())) {
				resultSoftware = currSoftware;
				break;
			}
		}

		return resultSoftware;
	}

	/**
	 * Get Software content.
	 * 
	 * @param projectId
	 * @param softwareId
	 * @return
	 * @throws ManagementException
	 */
	public InputStream getProjectSoftwareContent(String projectId, String softwareId) throws ManagementException {
		InputStream is = null;
		Software software = getProjectSoftware(projectId, softwareId);
		if (software != null) {
			String localPath = software.getLocalPath();
			if (localPath != null) {
				File softwareFile = new File(localPath);
				try {
					if (softwareFile.exists() && softwareFile.isFile()) {
						is = new FileInputStream(softwareFile);
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new ManagementException(StatusDTO.RESP_500, "Cannot create InputStream from " + softwareFile.getAbsolutePath() + ".", e);
				}
			}
		}
		return is;
	}

	/**
	 * Set Software content.
	 * 
	 * @param projectId
	 * @param softwareId
	 * @param fileName
	 * @param length
	 * @param lastModified
	 * @param input
	 * @return
	 * @throws ManagementException
	 */
	public boolean setProjectSoftwareContent(String projectId, String softwareId, String fileName, long length, Date lastModified, InputStream input) throws ManagementException {
		Software software = getProjectSoftware(projectId, softwareId);
		if (software != null) {
			if (fileName == null) {
				// Create default file name
				String name = software.getName();
				String version = software.getVersion();
				String fileExtension = null;
				String localPath = software.getLocalPath();
				if (localPath != null) {
					fileExtension = FilenameUtils.getExtension(localPath);
				}
				fileName = name + "_" + version + "." + fileExtension;
			}

			File projectSoftwareFileDir = ManagementSetupUtil.getProjectSoftwareFileDir(projectId, softwareId);
			try {
				FileUtils.cleanDirectory(projectSoftwareFileDir);
			} catch (IOException e) {
				e.printStackTrace();
			}

			File softwareFile = new File(projectSoftwareFileDir, fileName);
			try {
				FileUtil.copyInputStreamToFile(input, softwareFile);

				if (softwareFile.exists()) {
					// Update File
					softwareFile.setLastModified(lastModified.getTime());

					// Update Software
					// long length = softwareFile.length();
					// Date lastModified = new Date(softwareFile.lastModified());
					software.setLength(length);
					software.setLastModified(lastModified);
					software.setLocalPath(softwareFile.getAbsolutePath());
					software.setFileName(fileName);

					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new ManagementException(StatusDTO.RESP_500, "Cannot copy InputStream to " + softwareFile.getAbsolutePath() + ".", e);
			}
		}
		return false;
	}

	/**
	 * Add Software to a Project.
	 * 
	 * @param projectId
	 * @param newSoftwareRequest
	 * @throws ManagementException
	 */
	public Software addProjectSoftware(String projectId, Software newSoftwareRequest) throws ManagementException {
		// Throw exception - empty Id
		checkProjectId(projectId);

		// Throw exception - empty Software
		checkSoftware(newSoftwareRequest);

		// Container Project must exist
		Project project = this.mgmService.getProject(projectId);
		checkContainerProject(project);

		// Generate unique ProjectHome Id
		if (newSoftwareRequest.getId() == null || newSoftwareRequest.getId().isEmpty()) {
			newSoftwareRequest.setId(UUID.randomUUID().toString());
		}

		// Throw exception - empty Software name
		if (newSoftwareRequest.getName() == null || newSoftwareRequest.getName().isEmpty()) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Software name cannot be empty.", null);
		}

		if (newSoftwareRequest.getVersion() == null || newSoftwareRequest.getVersion().isEmpty()) {
			newSoftwareRequest.setVersion("1.0.0");
		}

		// Throw exception - Software with same Id or name exists
		for (Iterator<Software> softwareItor = project.getSoftware().iterator(); softwareItor.hasNext();) {
			Software currSoftware = softwareItor.next();

			if (newSoftwareRequest.getId().equals(currSoftware.getId())) {
				throw new ManagementException(ERROR_CODE_ENTITY_EXIST, "Software with same Id already exists.", null);
			}

			if (newSoftwareRequest.getName().equals(currSoftware.getName()) && newSoftwareRequest.getVersion().equals(currSoftware.getVersion())) {
				throw new ManagementException(ERROR_CODE_ENTITY_EXIST, "Software with same name and same version already exists.", null);
			}
		}

		project.addSoftware(newSoftwareRequest);

		return newSoftwareRequest;
	}

	/**
	 * Update Software.
	 * 
	 * @param projectId
	 * @param updateSoftwareRequest
	 * @throws ManagementException
	 */
	public void updateProjectSoftware(String projectId, Software updateSoftwareRequest) throws ManagementException {
		// Throw exception - empty Id
		checkProjectId(projectId);

		// Throw exception - empty Software
		checkSoftware(updateSoftwareRequest);

		// Find Software
		Software softwareToUpdate = getProjectSoftware(projectId, updateSoftwareRequest.getId());

		// Throw exception - Software not found
		checkSoftwareNotFound(softwareToUpdate);

		// No need to update when they are the same object.
		if (softwareToUpdate == updateSoftwareRequest) {
			return;
		}

		// Update type
		if (Util.compare(softwareToUpdate.getType(), updateSoftwareRequest.getType()) != 0) {
			softwareToUpdate.setType(updateSoftwareRequest.getType());
		}
		// Update name
		if (Util.compare(softwareToUpdate.getName(), updateSoftwareRequest.getName()) != 0) {
			softwareToUpdate.setName(updateSoftwareRequest.getName());
		}
		// Update version
		if (Util.compare(softwareToUpdate.getVersion(), updateSoftwareRequest.getVersion()) != 0) {
			softwareToUpdate.setVersion(updateSoftwareRequest.getVersion());
		}
		// Update description
		if (Util.compare(softwareToUpdate.getDescription(), updateSoftwareRequest.getDescription()) != 0) {
			softwareToUpdate.setDescription(updateSoftwareRequest.getDescription());
		}
		// Update length
		if (softwareToUpdate.getLength() != updateSoftwareRequest.getLength()) {
			softwareToUpdate.setLength(updateSoftwareRequest.getLength());
		}
		// Update lastModified
		if (Util.compare(softwareToUpdate.getLastModified(), updateSoftwareRequest.getLastModified()) != 0) {
			softwareToUpdate.setLastModified(updateSoftwareRequest.getLastModified());
		}
		// Update md5
		if (Util.compare(softwareToUpdate.getMd5(), updateSoftwareRequest.getMd5()) != 0) {
			softwareToUpdate.setMd5(updateSoftwareRequest.getMd5());
		}
	}

	/**
	 * Delete Software from a Project.
	 * 
	 * @param projectId
	 * @param softwareId
	 * @return
	 * @throws ManagementException
	 */
	public boolean deleteProjectSoftware(String projectId, String softwareId) throws ManagementException {
		// Throw exception - empty Id
		checkProjectId(projectId);
		checkSoftwareId(softwareId);

		// Find Project
		Project project = this.mgmService.getProject(projectId);
		checkContainerProject(project);

		// Find Software
		Software softwareToDelete = getProjectSoftware(projectId, softwareId);
		checkSoftwareNotFound(softwareToDelete);

		// Delete Software from Project.
		return project.deleteSoftware(softwareToDelete);
	}

	protected void checkProjectId(String projectId) throws ManagementException {
		if (projectId == null || projectId.isEmpty()) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "projectId cannot be empty.", null);
		}
	}

	protected void checkSoftwareId(String softwareId) throws ManagementException {
		if (softwareId == null || softwareId.isEmpty()) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "softwareId cannot be empty.", null);
		}
	}

	protected void checkSoftware(Software software) throws ManagementException {
		if (software == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Software cannot be empty.", null);
		}
	}

	protected void checkSoftwareNotFound(Software software) throws ManagementException {
		if (software == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_ILLEGAL_PARAMETER, "Software cannot be found.", null);
		}
	}

	protected void checkContainerProject(Project project) throws ManagementException {
		if (project == null) {
			throw new ManagementException(ERROR_CODE_ENTITY_NOT_FOUND, "Conatiner Project cannot be found.", null);
		}
	}

}
