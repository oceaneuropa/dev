package org.origin.common.launch.launcher;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.origin.common.launch.LaunchConfig;
import org.origin.common.launch.LaunchConstants;
import org.origin.common.launch.LaunchInstance;
import org.origin.common.launch.Launcher;
import org.origin.common.launch.runner.JavaProgramRunner;
import org.origin.common.launch.runner.ProgramRunner;
import org.origin.common.launch.util.LaunchConfigurationHelper;
import org.origin.common.util.PropertiesPreserveOrder;

/**
 * @see org.eclipse.pde.launching.EquinoxLaunchConfiguration
 * @see org.eclipse.pde.launching.AbstractPDELaunchConfiguration
 * 
 */
public class OSGiLauncher extends ProgramLauncher implements Launcher {

	public static String ID = "org.origin.launch.OSGiLauncher";

	@Override
	protected void preLaunch(LaunchConfig launchConfig, LaunchInstance launchInstance) throws IOException {
		super.preLaunch(launchConfig, launchInstance);

		// Create start.sh file
		// saveScriptFile(configuration);

		// Create config.ini file
		saveConfigIniFile(launchConfig);
	}

	@Override
	protected ProgramRunner getProgramRunner(LaunchConfig launchConfig) throws IOException {
		return new JavaProgramRunner();
	}

	@Override
	public String getProgram(LaunchConfig launchConfig) throws IOException {
		return launchConfig.getAttribute(LaunchConstants.PROGRAM, "java");
	}

	// Sample start.sh file
	// ---------------------------------------------------------------------------------------------------------------------------------------------------
	// CURRENT_DIR=$(dirname $0)
	// PARENT_DIR1="$(dirname ${CURRENT_DIR})"
	// PARENT_DIR2="$(dirname ${PARENT_DIR1})"
	// PARENT_DIR3="$(dirname ${PARENT_DIR2})"
	//
	// # echo ".sh script executed from: '${PWD}'"
	// # echo ".sh script location: '${CURRENT_DIR}'"
	// # echo ".sh script parent dir: '${PARENT_DIR}'"
	//
	// # java -jar /Users/yayang/origin/ta1/plugins/org.eclipse.osgi_3.10.100.v20150529-1857.jar -configuration /Users/yayang/origin/ta1/configuration -console
	// # java -jar ${CURRENT_DIR}/plugins/org.eclipse.osgi_3.10.100.v20150529-1857.jar -configuration ${CURRENT_DIR}/configuration -console
	// java -jar ${PARENT_DIR3}/plugins/org.eclipse.osgi_3.10.100.v20150529-1857.jar -configuration ${PARENT_DIR1}/configuration -console
	// ---------------------------------------------------------------------------------------------------------------------------------------------------
	// private void saveScriptFile(LaunchConfiguration configuration) throws IOException {
	// List<String> lines = new ArrayList<String>();
	//
	// lines.add("CURRENT_DIR=$(dirname $0)");
	// lines.add("PARENT_DIR1=\"$(dirname ${CURRENT_DIR})\"");
	// lines.add("PARENT_DIR2=\"$(dirname ${PARENT_DIR1})\"");
	// lines.add("PARENT_DIR3=\"$(dirname ${PARENT_DIR2})\"");
	// lines.add("java -jar ${PARENT_DIR3}/plugins/org.eclipse.osgi_3.10.100.v20150529-1857.jar -configuration ${PARENT_DIR1}/configuration -consoleLog
	// -console");
	//
	// File dir = getBinDir(configuration);
	// File startFile = new File(dir, "start.sh");
	//
	// FileUtil.writeLines(startFile, lines, false);
	//
	// configuration.setAttribute(LaunchConstants.PROGRAM, startFile.getAbsolutePath());
	// }

	// Sample config.ini file
	// ---------------------------------------------------------------------------------------------------------------------------------------------------
	// #Configuration File
	// #Thu Apr 26 14:10:20 PDT 2018
	// # osgi.bundles.defaultStartLevel=4
	//
	// osgi.bundles=reference\:file\:eclipse.equinox/org.eclipse.equinox.simpleconfigurator_1.1.0.v20131217-1203.jar@1\:start
	//
	// #org.eclipse.equinox.simpleconfigurator.configUrl=file\:org.eclipse.equinox.simpleconfigurator/bundles.info
	// org.eclipse.equinox.simpleconfigurator.configUrl=file\:/Users/yayang/origin/ta1/configuration/org.eclipse.equinox.simpleconfigurator/bundles.info
	//
	// eclipse.ignoreApp=true
	//
	// org.osgi.service.http.port=12001
	//
	// TA_HOME=/Users/yayang/origin/ta1
	// ---------------------------------------------------------------------------------------------------------------------------------------------------
	protected void saveConfigIniFile(LaunchConfig launchConfig) throws IOException {
		Properties properties = new PropertiesPreserveOrder();

		Map<String, String> configPropertiesMap = launchConfig.getAttribute(LaunchConstants.OSGI_CONFIG_PROPERTIES_MAP, new HashMap<String, String>());
		for (Iterator<String> itor = configPropertiesMap.keySet().iterator(); itor.hasNext();) {
			String vmArgName = itor.next();
			String vmArgValue = configPropertiesMap.get(vmArgName);
			properties.put(vmArgName, vmArgValue);
		}

		Map<String, String> configVMArgumentsMap = launchConfig.getAttribute(LaunchConstants.OSGI_CONFIG_VM_ARGUMENTS_MAP, new HashMap<String, String>());
		for (Iterator<String> itor = configVMArgumentsMap.keySet().iterator(); itor.hasNext();) {
			String vmArgName = itor.next();
			String vmArgValue = configVMArgumentsMap.get(vmArgName);
			properties.put(vmArgName, vmArgValue);
		}

		File dir = getConfigurationDirectory(launchConfig);
		File configIniFile = new File(dir, "config.ini");

		LaunchConfigurationHelper.save(configIniFile, properties); // $NON-NLS-1$
	}

}

// // System arguments
// properties.put("eclipse.ignoreApp", "true"); //$NON-NLS-1$ //$NON-NLS-2$
// properties.put("osgi.noShutdown", "true"); //$NON-NLS-1$ //$NON-NLS-2$
//
// // OSGi default bundle start level
// // int start = configuration.getAttribute(LaunchConstants.DEFAULT_START_LEVEL, 4);
// int start = configuration.getAttribute("osgi.bundles.defaultStartLevel", 4);
// properties.put("osgi.bundles.defaultStartLevel", Integer.toString(start)); //$NON-NLS-1$
//
// // The location of the org.eclipse.equinox.simpleconfigurator.jar is relative to the location of the org.eclipse.osgi_3.10.100.v20150529-1857.jar
// properties.put("osgi.bundles", "reference:file:eclipse.equinox/org.eclipse.equinox.simpleconfigurator_1.1.0.v20131217-1203.jar@1:start");
// // The bundles.info file is shared by all nodes and come from fixed location from the home.
// // e.g. {HOME}/configuration/org.eclipse.equinox.simpleconfigurator/bundles.info
// properties.put("org.eclipse.equinox.simpleconfigurator.configUrl",
// "file:/Users/yayang/origin/ta1/configuration/org.eclipse.equinox.simpleconfigurator/bundles_v04.info");
//
// // VM arguments
// // - Need to parse string to get all of VM arguments configured by users
// properties.put("org.osgi.service.http.port", "12001");
// properties.put("platform.home", "/Users/yayang/origin/ta1");
