package org.nb.home.handler;

import java.io.File;

public class HomeDirHandler {

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
	public File getSectorFolder(File homeDir, String sectorName) {
		return new File(homeDir, SECTORS_DIR_NAME + File.pathSeparator + sectorName);
	}

	/**
	 * Create sector dir.
	 * 
	 * @param homeDir
	 * @param sectorName
	 */
	public File createSectorFolder(File homeDir, String sectorName) {
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
	public void initSectorFolder(File sectorDir) {
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
	public File getArchivesFolder(File homeDir, String sectorName) {
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
	public File getSpacesFolder(File homeDir, String sectorName) {
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
	public File getNodesFolder(File homeDir, String sectorName) {
		File sectorDir = getSectorFolder(homeDir, sectorName);
		return new File(sectorDir, NODES_DIR_NAME);
	}

}
