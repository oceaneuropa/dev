package org.origin.common.launch.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.origin.common.launch.LaunchConfig;
import org.origin.common.launch.LaunchConstants;
// import org.origin.common.launch.impl.AbstractLauncher.ExecutionArguments;

public class LaunchArgumentsHelper {

	public static File getWorkingDirectory(LaunchConfig configuration) throws IOException {
		String working;
		try {
			working = configuration.getAttribute(LaunchConstants.WORKING_DIRECTORY, new File(".").getCanonicalPath()); //$NON-NLS-1$
		} catch (IOException e) {
			working = "${workspace_loc}/../"; //$NON-NLS-1$
		}
		File dir = new File(getSubstitutedString(working));
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	private static String getSubstitutedString(String text) throws IOException {
		if (text == null) {
			return ""; //$NON-NLS-1$
		}
		// IStringVariableManager mgr = VariablesPlugin.getDefault().getStringVariableManager();
		// return mgr.performStringSubstitution(text);
		return text;
	}

	public static String getUserVMArguments(LaunchConfig configuration) throws IOException {
		String args = configuration.getAttribute(LaunchConstants.ATTR_VM_ARGUMENTS, (String) null);
		if (args == null) {
			// backward compatibility
			args = configuration.getAttribute("vmargs", (String) null); //$NON-NLS-1$
			if (args != null) {
				// ILaunchConfigurationWorkingCopy wc = null;
				// if (configuration.isWorkingCopy()) {
				// wc = (ILaunchConfigurationWorkingCopy) configuration;
				// } else {
				// wc = configuration.getWorkingCopy();
				// }
				LaunchConfig wc = configuration;
				wc.setAttribute("vmargs", (String) null); //$NON-NLS-1$
				wc.setAttribute(LaunchConstants.ATTR_VM_ARGUMENTS, args);
				// wc.doSave();
				wc.save();
			}
		}
		return args == null ? "" : getSubstitutedString(args); //$NON-NLS-1$
	}

	// public static Map<String, Object> getVMSpecificAttributesMap(LaunchConfiguration config) throws IOException {
	// Map<String, Object> map = new HashMap<String, Object>(2);
	// String javaCommand = config.getAttribute(LaunchConstants.ATTR_JAVA_COMMAND, (String) null);
	// map.put(LaunchConstants.ATTR_JAVA_COMMAND, javaCommand);
	// if (TargetPlatform.getOS().equals("macosx")) { //$NON-NLS-1$
	// ModelEntry entry = PluginRegistry.findEntry("org.eclipse.jdt.debug"); //$NON-NLS-1$
	// if (entry != null) {
	// IPluginModelBase[] models = entry.getExternalModels();
	// for (int i = 0; i < models.length; i++) {
	// File file = new File(models[i].getInstallLocation());
	// if (!file.isFile())
	// file = new File(file, "jdi.jar"); //$NON-NLS-1$
	// if (file.exists()) {
	// map.put(IJavaLaunchConfigurationConstants.ATTR_BOOTPATH_PREPEND, new String[] { file.getAbsolutePath() });
	// break;
	// }
	// }
	// }
	// }
	// return map;
	// }

	// public static String[] getUserProgramArgumentArray(LaunchConfiguration configuration) throws IOException {
	// String args = getUserProgramArguments(configuration);
	// return new ExecutionArguments("", args).getProgramArgumentsArray(); //$NON-NLS-1$
	// }

	public static String getUserProgramArguments(LaunchConfig configuration) throws IOException {
		String args = configuration.getAttribute(LaunchConstants.ATTR_PROGRAM_ARGUMENTS, (String) null);
		if (args == null) {
			// backward compatibility
			args = configuration.getAttribute("progargs", (String) null); //$NON-NLS-1$
			if (args != null) {
				// ILaunchConfigurationWorkingCopy wc = null;
				// if (configuration.isWorkingCopy()) {
				// wc = (ILaunchConfigurationWorkingCopy) configuration;
				// } else {
				// wc = configuration.getWorkingCopy();
				// }
				LaunchConfig wc = configuration;
				wc = configuration;
				wc.setAttribute("progargs", (String) null); //$NON-NLS-1$
				wc.setAttribute(LaunchConstants.ATTR_PROGRAM_ARGUMENTS, args);
				wc.save();
			}
		}
		return args == null ? "" : getSubstitutedString(args); //$NON-NLS-1$
	}

	public static String[] constructClasspath(LaunchConfig configuration) throws IOException {
		// double targetVersion = TargetPlatformHelper.getTargetVersion();
		// String jarPath = targetVersion >= 3.3 ? getEquinoxStartupPath(IPDEBuildConstants.BUNDLE_EQUINOX_LAUNCHER) : getStartupJarPath();
		// if (jarPath == null && targetVersion < 3.3)
		// jarPath = getEquinoxStartupPath("org.eclipse.core.launcher"); //$NON-NLS-1$
		// String jarPath = "";
		// if (jarPath == null)
		// return null;

		ArrayList<String> entries = new ArrayList<String>();
		// entries.add(jarPath);

		// String bootstrap = configuration.getAttribute(IPDELauncherConstants.BOOTSTRAP_ENTRIES, ""); //$NON-NLS-1$
		String bootstrap = configuration.getAttribute("", ""); //$NON-NLS-1$
		StringTokenizer tok = new StringTokenizer(getSubstitutedString(bootstrap), ","); //$NON-NLS-1$
		while (tok.hasMoreTokens()) {
			entries.add(tok.nextToken().trim());
		}
		return entries.toArray(new String[entries.size()]);
	}

}
