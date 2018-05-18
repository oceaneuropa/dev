package org.origin.common.launch.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.origin.common.launch.LaunchConfiguration;
import org.origin.common.launch.LaunchConstants;

public class LaunchConfigurationHelper {

	/**
	 * 
	 * @param file
	 * @param properties
	 */
	public static void save(File file, Properties properties) {
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file);
			properties.store(output, "Configuration File"); //$NON-NLS-1$
			output.flush();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static File getBinFolder(LaunchConfiguration config) throws IOException {
		File binFolder = null;
		String location = config.getAttribute(LaunchConstants.WORKING_DIRECTORY, (String) null);
		if (location != null && !location.isEmpty()) {
			binFolder = new File(location, "bin");
		}
		if (binFolder != null && !binFolder.exists()) {
			binFolder.mkdirs();
		}
		return binFolder;
	}

	// public static File getConfigurationArea(LaunchConfiguration config) throws IOException {
	public static File getConfigurationFolder(LaunchConfiguration config) throws IOException {
		File dir = getConfigurationLocation(config);
		if (dir != null && !dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	public static File getConfigurationLocation(LaunchConfiguration config) throws IOException {
		String configName = config.getName();
		configName = configName.replace('#', 'h');
		// File dir = new File(PDECore.getDefault().getStateLocation().toOSString(), configName);
		File dir = null;
		// if (!config.getAttribute(IPDELauncherConstants.CONFIG_USE_DEFAULT_AREA, true)) {
		String userPath = config.getAttribute(LaunchConstants.CONFIG_LOCATION, (String) null);
		if (userPath != null) {
			// userPath = getSubstitutedString(userPath);
			dir = new File(userPath).getAbsoluteFile();
		}
		// }
		return dir;
	}

}
