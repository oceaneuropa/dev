package org.origin.common.launch.launcher;

import java.io.IOException;

import org.origin.common.launch.LaunchConfig;
import org.origin.common.launch.LaunchInstance;
import org.origin.common.launch.Launcher;
import org.origin.common.launch.runner.DefaultProgramRunner;
import org.origin.common.launch.runner.ProgramRunner;
import org.origin.common.launch.runner.ProgramRunner.ProgramConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptLauncher implements Launcher {

	protected static Logger LOG = LoggerFactory.getLogger(ScriptLauncher.class);

	public static String ID = "org.origin.launch.ScriptLauncher";

	public static String WORKING_DIRECTORY_LOCATION = "workingDirectoryLocation";
	public static String START_SCRIPT_LOCATION = "startScriptLocation";

	@Override
	public void launch(LaunchConfig launchConfig, LaunchInstance launchInstance) throws IOException {
		String scriptLocation = launchConfig.getAttribute(START_SCRIPT_LOCATION, (String) null);
		if (scriptLocation == null || scriptLocation.isEmpty()) {
			LOG.error(START_SCRIPT_LOCATION + " attribute is empty.");
			return;
		}
		String workingDirectoryLocation = launchConfig.getAttribute(WORKING_DIRECTORY_LOCATION, (String) null);
		if (workingDirectoryLocation == null || workingDirectoryLocation.isEmpty()) {
			LOG.error(WORKING_DIRECTORY_LOCATION + " attribute is empty.");
			return;
		}

		LOG.info(WORKING_DIRECTORY_LOCATION + " is " + workingDirectoryLocation);
		LOG.info(START_SCRIPT_LOCATION + " is " + scriptLocation);

		ProgramConfiguration runnerConfig = new ProgramConfiguration(scriptLocation);
		runnerConfig.setWorkingDirectory(workingDirectoryLocation);

		ProgramRunner runner = new DefaultProgramRunner();
		runner.run(runnerConfig, launchInstance);
	}

}

// public static String CONFIG_INI_LOCATION = "configIniLocation";

// Path workingDirectoryPath = Paths.get(workingDirectoryLocation).toAbsolutePath();
// File workingDirectory = workingDirectoryPath.toFile();
// Path scriptPath = Paths.get(scriptLocation).toAbsolutePath();
// String[] command = new String[] { scriptPath.toAbsolutePath().toString() };
// Process process = new ProcessBuilder().redirectErrorStream(true).command(command).directory(workingDirectory).start();
