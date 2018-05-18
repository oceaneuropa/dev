package org.origin.common.launch.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.origin.common.launch.LaunchConfiguration;
import org.origin.common.launch.LaunchConstants;
import org.origin.common.launch.LaunchHandler;
import org.origin.common.launch.Launcher;
import org.origin.common.launch.impl.ProgramRunner.ProgramConfiguration;
import org.origin.common.launch.util.LaunchArgumentsHelper;
import org.origin.common.launch.util.LaunchConfigurationHelper;

public abstract class AbstractLauncher implements Launcher {

	@Override
	public void launch(LaunchConfiguration launchConfig, LaunchHandler launchHandler) throws IOException {
		try {
			preLaunch(launchConfig, launchHandler);

			String program = getProgram(launchConfig);
			String mainClass = getMainClass(launchConfig);
			String[] classpath = getClasspath(launchConfig);
			String[] systemArguments = getSystemArguments(launchConfig);
			String[] vmArguments = getVMArguments(launchConfig);
			String[] programArguments = getProgramArguments(launchConfig);
			String workingDirectory = getWorkingDirectory(launchConfig).getAbsolutePath();

			ProgramConfiguration runnerConfig = new ProgramConfiguration(program, mainClass, classpath);
			runnerConfig.setSystemArguments(systemArguments);
			runnerConfig.setVMArguments(vmArguments);
			runnerConfig.setProgramArguments(programArguments);
			runnerConfig.setWorkingDirectory(workingDirectory);

			ProgramRunner runner = getProgramRunner(launchConfig);
			if (runner == null) {
				throw new IOException("ProgramRunner cannot be null.");
			}
			runner.run(runnerConfig, launchHandler);

		} catch (final IOException e) {
			throw e;
		}
	}

	protected void preLaunch(LaunchConfiguration launchConfig, LaunchHandler launchHandler) throws IOException {
		// String workingDirLocation = configuration.getAttribute(LaunchConstants.WORKING_DIRECTORY, (String) null);
		String workingDirectory = getWorkingDirectory(launchConfig).getAbsolutePath();
		launchHandler.setAttribute(LaunchConstants.WORKING_DIRECTORY, workingDirectory);
		launchHandler.setAttribute(LaunchConstants.CONFIG_LOCATION, getConfigurationDirectory(launchConfig).toString());
	}

	protected abstract ProgramRunner getProgramRunner(LaunchConfiguration configuration) throws IOException;

	protected abstract String getProgram(LaunchConfiguration configuration) throws IOException;

	public String getMainClass(LaunchConfiguration configuration) {
		// return "org.eclipse.equinox.launcher.Main"; //$NON-NLS-1$
		return "org.eclipse.core.launcher.Main"; //$NON-NLS-1$
	}

	public String[] getClasspath(LaunchConfiguration configuration) throws IOException {
		String[] classpath = LaunchArgumentsHelper.constructClasspath(configuration);
		// if (classpath == null) {
		// String message = PDEMessages.WorkbenchLauncherConfigurationDelegate_noStartup;
		// throw new CoreException(LauncherUtils.createErrorStatus(message));
		// }
		return classpath;
	}

	public String[] getSystemArguments(LaunchConfiguration configuration) throws IOException {
		List<String> systemArgumentsList = new ArrayList<String>();

		Map<String, String> systemArgumentsMap = configuration.getAttribute(LaunchConstants.SYSTEM_ARGUMENTS_MAP, new HashMap<String, String>());
		for (Iterator<String> itor = systemArgumentsMap.keySet().iterator(); itor.hasNext();) {
			String vmArgName = itor.next();
			String vmArgValue = systemArgumentsMap.get(vmArgName);
			systemArgumentsList.add(vmArgName);
			systemArgumentsList.add(vmArgValue);
		}

		return systemArgumentsList.toArray(new String[systemArgumentsList.size()]);
	}

	public String[] getVMArguments(LaunchConfiguration configuration) throws IOException {
		List<String> vmArgumentsList = new ArrayList<String>();

		Map<String, String> vmArgumentsMap = configuration.getAttribute(LaunchConstants.VM_ARGUMENTS_MAP, new HashMap<String, String>());
		for (Iterator<String> itor = vmArgumentsMap.keySet().iterator(); itor.hasNext();) {
			String vmArgName = itor.next();
			String vmArgValue = vmArgumentsMap.get(vmArgName);
			vmArgumentsList.add(vmArgName + "=" + vmArgValue);
		}

		return vmArgumentsList.toArray(new String[vmArgumentsList.size()]);
	}

	public String[] getProgramArguments(LaunchConfiguration configuration) throws IOException {
		List<String> programArgs = configuration.getAttribute(LaunchConstants.PROGRAM_ARGUMENTS_LIST, new ArrayList<String>());
		return programArgs.toArray(new String[programArgs.size()]);
	}

	public File getWorkingDirectory(LaunchConfiguration configuration) throws IOException {
		return LaunchArgumentsHelper.getWorkingDirectory(configuration);
	}

	public File getConfigurationDirectory(LaunchConfiguration configuration) throws IOException {
		return LaunchConfigurationHelper.getConfigurationFolder(configuration);
	}

}

// public static class ExecutionArguments {
// private String fVMArgs;
// private String fProgramArgs;
//
// public ExecutionArguments(String vmArgs, String programArgs) {
// if (vmArgs == null || programArgs == null) {
// throw new IllegalArgumentException();
// }
// fVMArgs = vmArgs;
// fProgramArgs = programArgs;
// }
//
// public String getVMArguments() {
// return fVMArgs;
// }
//
// public String getProgramArguments() {
// return fProgramArgs;
// }
//
// public String[] getVMArgumentsArray() {
// return DebugUtil.parseArguments(fVMArgs);
// }
//
// public String[] getProgramArgumentsArray() {
// return DebugUtil.parseArguments(fProgramArgs);
// }
// }

// protected File configDir = null;

// protected void preLaunch(LaunchConfiguration configuration, LaunchHandler launchHandler) throws IOException {
//// boolean autoValidate = configuration.getAttribute(IPDELauncherConstants.AUTOMATIC_VALIDATE, false);
//// if (autoValidate) {
//// validatePluginDependencies(configuration);
//// }
//// validateProjectDependencies(configuration);
//// LauncherUtils.setLastLaunchMode(launch.getLaunchMode());
//// clear(configuration);
//// synchronizeManifests(configuration);
//
// String workingDirLocation = configuration.getAttribute(LaunchConstants.WORKING_DIRECTORY, (String) null);
// launchHandler.setAttribute(LaunchConstants.WORKING_DIRECTORY, workingDirLocation);
// launchHandler.setAttribute(LaunchConstants.CONFIG_LOCATION, getConfigDirectory(configuration).toString());
// }

// /**
// * Returns an array of environment variables to be used when launching the given configuration or <code>null</code> if unspecified.
// *
// * @param configuration
// * launch configuration
// * @return the environment variables to be used when launching or <code>null</code>
// * @throws CoreException
// * if unable to access associated attribute or if unable to resolve a variable in an environment variable's value
// */
// public String[] getEnvironment(LaunchConfiguration configuration) throws IOException {
// // return DebugPlugin.getDefault().getLaunchManager().getEnvironment(configuration);
// return LaunchActivator.getDefault().getLaunchService().getEnvironment(configuration);
// }

// /**
// * Returns the Map of VM-specific attributes specified by the given launch configuration, or <code>null</code> if none.
// *
// * @param configuration
// * launch configuration
// * @return the <code>Map</code> of VM-specific attributes
// * @exception CoreException
// * if unable to retrieve the attribute
// */
// public Map<String, Object> getVMSpecificAttributesMap(LaunchConfiguration configuration) throws IOException {
// return LaunchArgumentsHelper.getVMSpecificAttributesMap(configuration);
// }

// /**
// * Assigns a default source locator to the given launch if a source locator has not yet been assigned to it, and the associated launch configuration does
// * not specify a source locator.
// *
// * @param configuration
// * configuration being launched
// * @exception CoreException
// * if unable to set the source locator
// */
// protected void setDefaultSourceLocator(LaunchConfiguration configuration) throws IOException {
// ILaunchConfigurationWorkingCopy wc = null;
// if (configuration.isWorkingCopy()) {
// wc = (ILaunchConfigurationWorkingCopy) configuration;
// } else {
// wc = configuration.getWorkingCopy();
// }
// String id = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_SOURCE_PATH_PROVIDER, (String) null);
// if (!PDESourcePathProvider.ID.equals(id)) {
// wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_SOURCE_PATH_PROVIDER, PDESourcePathProvider.ID);
// wc.doSave();
// }
// }

// /*
// * /* (non-Javadoc)
// *
// * @see org.eclipse.debug.core.model.LaunchConfigurationDelegate#getBuildOrder(org.eclipse.debug.core.ILaunchConfiguration, java.lang.String)
// */
// @Override
// protected IProject[] getBuildOrder(ILaunchConfiguration configuration, String mode) throws CoreException {
// return computeBuildOrder(LaunchPluginValidator.getAffectedProjects(configuration));
// }
//
// @Override
// protected IProject[] getProjectsForProblemSearch(ILaunchConfiguration configuration, String mode) throws CoreException {
// return LaunchPluginValidator.getAffectedProjects(configuration);
// }

// @Override
// protected boolean isLaunchProblem(IMarker problemMarker) throws CoreException {
// return super.isLaunchProblem(problemMarker) && (problemMarker.getType().equals(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER) ||
// problemMarker.getType().equals(PDEMarkerFactory.MARKER_ID));
// }

// /**
// * Checks for old-style plugin.xml files that have become stale since the last launch. For any stale plugin.xml files found, the corresponding MANIFEST.MF
// * is deleted from the runtime configuration area so that it gets regenerated upon startup.
// *
// * @param configuration
// * @param monitor
// */
// protected void synchronizeManifests(LaunchConfiguration configuration) {
// // LaunchConfigurationHelper.synchronizeManifests(configuration, getConfigDir(configuration));
// // monitor.done();
// }
//
// /**
// * Checks if the Automated Management of Dependencies option is turned on. If so, it makes aure all manifests are updated with the correct dependencies.
// *
// * @param configuration
// * @param monitor
// */
// protected void validateProjectDependencies(LaunchConfiguration configuration) {
// LauncherUtils.validateProjectDependencies(configuration, monitor);
// }
//
// /**
// * By default, this method does nothing. Clients should override, if appropriate.
// *
// * @param configuration
// * @throws IOException
// */
// protected void clear(LaunchConfiguration configuration) throws IOException {
// }
//
// /**
// * Validates inter-bundle dependencies automatically prior to launching if that option is turned on.
// *
// * @param configuration
// * @throws IOException
// */
// protected void validatePluginDependencies(LaunchConfiguration configuration) throws IOException {
// EclipsePluginValidationOperation op = new EclipsePluginValidationOperation(configuration);
// LaunchPluginValidator.runValidationOperation(op, monitor);
// }

// public VMRunner getVMRunner(LaunchConfiguration configuration) throws IOException {
// VMInstallation launcher = VMHelper.createLauncher(configuration);
// return launcher.getVMRunner(mode);
// return new AbstractVMRunner();
// return new ScriptVMRunner();
// }

// /**
// * Adds a listener to the launch to be notified at interesting launch lifecycle events such as when the launch terminates.
// *
// * @param launch
// */
// protected void manageLaunch(LaunchHandler launch) {
// PDELaunchingPlugin.getDefault().getLaunchListener().manage(launch);
// }

// vmRunnerConfig.setEnvironment(getEnvironment(configuration));
// vmRunnerConfig.setVMSpecificAttributesMap(getVMSpecificAttributesMap(configuration));
// setDefaultSourceLocator(configuration);
// manageLaunch(launch);

// protected File getBinDir(LaunchConfiguration configuration) throws IOException {
// return LaunchConfigurationHelper.getBinFolder(configuration);
// }
