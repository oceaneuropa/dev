package org.origin.common.launch.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.origin.common.launch.LaunchConstants;
import org.origin.common.launch.LaunchHandler;
import org.origin.common.launch.ProcessHandler;

/**
 * @see org.eclipse.jdt.launching.AbstractVMRunner
 * @see org.eclipse.debug.core.DebugPlugin
 * 
 */
public abstract class AbstractProgramRunner implements ProgramRunner {

	@Override
	public void run(ProgramConfiguration configuration, LaunchHandler launch) throws IOException {
		String program = configuration.getProgram();

		String workingDirectoryLocation = configuration.getWorkingDirectory();
		File workingDirectory = new File(workingDirectoryLocation);

		String[] cmdLine = new String[] { program };
		Process process = exec(cmdLine, workingDirectory);
		if (process == null) {
			throw new IOException("System process cannot be created.");
		}

		Map<String, String> processAttributes = getDefaultProcessAttributes();
		newProcessHandler(launch, process, program, processAttributes);
	}

	protected Process exec(String[] cmdLine, File workingDirectory) throws IOException {
		return exec(cmdLine, workingDirectory, null);
	}

	protected Process exec(String[] cmdLine, File workingDirectory, String[] envp) throws IOException {
		Process process = null;
		try {
			if (workingDirectory == null) {
				process = Runtime.getRuntime().exec(cmdLine, envp);
			} else {
				process = Runtime.getRuntime().exec(cmdLine, envp, workingDirectory);
			}
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
		return process;
	}

	protected Map<String, String> getDefaultProcessAttributes() {
		Map<String, String> processAttributes = new HashMap<String, String>();
		processAttributes.put(LaunchConstants.ATTR_PROCESS_TYPE, LaunchConstants.ID_JAVA_PROCESS_TYPE);
		return processAttributes;
	}

	protected ProcessHandler newProcessHandler(LaunchHandler launchHandler, Process process, String label, Map<String, String> attributes) throws IOException {
		ProcessHandler processHandler = new ProcessHandlerImpl(launchHandler, process, label, attributes);
		return processHandler;
	}

}
