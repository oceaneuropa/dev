package org.nb.mgm.handler;

import java.io.File;
import java.util.Hashtable;

import org.nb.mgm.Activator;
import org.origin.common.util.PropertyUtil;

public class ManagementSetupUtil {

	/**
	 * 
	 * @return
	 */
	public static File getHomeDir() {
		File homeDir = null;
		Hashtable<Object, Object> props = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(Activator.getContext(), props, "NB_HOME");
		String homePath = PropertyUtil.getString(props, "NB_HOME", null);
		if (homePath != null) {
			homeDir = new File(homePath);
		}
		return homeDir;
	}

	/**
	 * 
	 * @param homeDir
	 * @return
	 */
	public static File getProjectsDir(File homeDir) {
		File projectsDir = new File(homeDir, "projects");
		if (!projectsDir.exists()) {
			projectsDir.mkdirs();
		}
		return projectsDir;
	}

	/**
	 * 
	 * @param projectId
	 * @return
	 */
	public static File getProjectDir(String projectId) {
		File homeDir = getHomeDir();
		File projectsDir = getProjectsDir(homeDir);
		File projectDir = new File(projectsDir, projectId);
		if (!projectDir.exists()) {
			projectDir.mkdirs();
		}
		return projectDir;
	}

	/**
	 * 
	 * @param projectId
	 * @return
	 */
	public static File getProjectSoftwareDir(String projectId) {
		File projectDir = getProjectDir(projectId);
		File projectSoftwareDir = new File(projectDir, "software");
		if (!projectSoftwareDir.exists()) {
			projectSoftwareDir.mkdirs();
		}
		return projectSoftwareDir;
	}

	/**
	 * 
	 * @param projectId
	 * @param softwareId
	 * @return
	 */
	public static File getProjectSoftwareFileDir(String projectId, String softwareId) {
		File projectSoftwareDir = getProjectSoftwareDir(projectId);
		File projectSoftwareFileDir = new File(projectSoftwareDir, softwareId);
		if (!projectSoftwareFileDir.exists()) {
			projectSoftwareFileDir.mkdirs();
		}
		return projectSoftwareFileDir;
	}

}
