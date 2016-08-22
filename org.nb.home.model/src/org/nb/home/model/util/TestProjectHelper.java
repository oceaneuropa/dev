package org.nb.home.model.util;

import java.io.File;

import org.nb.home.model.runtime.config.ProjectConfig;
import org.origin.common.workingcopy.WorkingCopy;
import org.origin.common.workingcopy.WorkingCopyUtil;

public class TestProjectHelper {

	public static TestProjectHelper INSTANCE = new TestProjectHelper();

	public static final String SECTORS_DIR_NAME = "sectors";
	public static final String SPACES_DIR_NAME = "spaces";
	public static final String NODES_DIR_NAME = "nodes";
	public static final String ARCHIVES_DIR_NAME = "archives";

	// ------------------------------------------------------------------------------------------------
	// Sector
	// ------------------------------------------------------------------------------------------------
	/**
	 * Get sector folder by sector name.
	 * 
	 * @param homeDir
	 * @param sectorName
	 * @return
	 */
	public static File getSectorFolder(File homeDir, String sectorName) {
		return new File(homeDir, SECTORS_DIR_NAME + File.pathSeparator + sectorName);
	}

	/**
	 * Create sector dir.
	 * 
	 * @param homeDir
	 * @param sectorName
	 */
	public static File createSectorFolder(File homeDir, String sectorName) {
		// Create {homeDir}/sectors/{sectorName} folder
		File sectorDir = getSectorFolder(homeDir, sectorName);
		if (!sectorDir.exists()) {
			sectorDir.mkdirs();
		}
		initSectorFolder(sectorDir);
		return sectorDir;
	}

	/**
	 * Initialize a sector dir. Create all the sub-dirs in a sector dir.
	 * 
	 * @param sectorDir
	 */
	public static void initSectorFolder(File sectorDir) {
		if (!sectorDir.exists()) {
			sectorDir.mkdirs();
		}

		// Create {homeDir}/sectors/{sectorDir}/archives folder
		File archivesDir = new File(sectorDir, ARCHIVES_DIR_NAME);
		if (!archivesDir.exists()) {
			archivesDir.mkdirs();
		}

		// Create {homeDir}/sectors/{sectorDir}/spaces folder
		File spacesDir = new File(sectorDir, SPACES_DIR_NAME);
		if (!spacesDir.exists()) {
			spacesDir.mkdirs();
		}

		// Create {homeDir}/sectors/{sectorDir}/nodes folder
		File nodesDir = new File(sectorDir, NODES_DIR_NAME);
		if (!nodesDir.exists()) {
			nodesDir.mkdirs();
		}
	}

	// ------------------------------------------------------------------------------------------------
	// archives
	// ------------------------------------------------------------------------------------------------
	/**
	 * Get the archives folder inside a sector folder by sector name.
	 * 
	 * @param homeDir
	 * @param sectorName
	 * @return
	 */
	public static File getArchivesFolder(File homeDir, String sectorName) {
		File sectorDir = getSectorFolder(homeDir, sectorName);
		return new File(sectorDir, ARCHIVES_DIR_NAME);
	}

	// ------------------------------------------------------------------------------------------------
	// spaces
	// ------------------------------------------------------------------------------------------------
	/**
	 * Get the spaces folder inside a sector folder by sector name.
	 * 
	 * @param homeDir
	 * @param sectorName
	 * @return
	 */
	public static File getSpacesFolder(File homeDir, String sectorName) {
		File sectorDir = getSectorFolder(homeDir, sectorName);
		return new File(sectorDir, SPACES_DIR_NAME);
	}

	// ------------------------------------------------------------------------------------------------
	// nodes
	// ------------------------------------------------------------------------------------------------
	/**
	 * Get the nodes folder inside a sector folder by sector name.
	 * 
	 * @param homeDir
	 * @param sectorName
	 * @return
	 */
	public static File getNodesFolder(File homeDir, String sectorName) {
		File sectorDir = getSectorFolder(homeDir, sectorName);
		return new File(sectorDir, NODES_DIR_NAME);
	}

	/**
	 * 
	 * @param projectConfigFile
	 * @return
	 */
	public ProjectConfig getProjectConfig(File projectConfigFile) {
		ProjectConfig projectConfig = null;
		if (projectConfigFile != null && projectConfigFile.exists() && projectConfigFile.isFile()) {
			WorkingCopy<?> wc = WorkingCopyUtil.getWorkingCopy(projectConfigFile);
			if (wc != null) {
				projectConfig = wc.getRootElement(ProjectConfig.class);
			}
		}
		return projectConfig;
	}

	/**
	 * 
	 * @param projectConfigFile
	 * @param projectId
	 * @return
	 */
	public boolean projectExists(File projectConfigFile, String projectId) {
		if (projectId != null && projectConfigFile != null && projectConfigFile.exists() && projectConfigFile.isFile()) {
			WorkingCopy<?> wc = WorkingCopyUtil.getWorkingCopy(projectConfigFile);
			if (wc != null) {
				ProjectConfig projectConfig = wc.getRootElement(ProjectConfig.class);
				if (projectConfig != null && projectId.equals(projectConfig.getProjectId())) {
					return true;
				}
			}
		}
		return false;
	}

}
