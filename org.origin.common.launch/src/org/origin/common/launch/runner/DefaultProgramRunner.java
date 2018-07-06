package org.origin.common.launch.runner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.origin.common.launch.LaunchConstants;
import org.origin.common.launch.LaunchInstance;
import org.origin.common.launch.ProcessInstance;
import org.origin.common.launch.impl.ProcessInstanceImpl;

/**
 * @see org.eclipse.jdt.launching.AbstractVMRunner
 * @see org.eclipse.debug.core.DebugPlugin
 * 
 */
public class DefaultProgramRunner implements ProgramRunner {

	@Override
	public void run(ProgramConfiguration launchConfig, LaunchInstance launchInstsance) throws IOException {
		String program = launchConfig.getProgram();

		String workingDirectoryLocation = launchConfig.getWorkingDirectory();
		File workingDirectory = new File(workingDirectoryLocation);

		String[] cmdLine = new String[] { program };

		Process process = exec(cmdLine, workingDirectory);
		if (process == null) {
			throw new IOException("System process cannot be created.");
		}

		Map<String, String> processAttributes = getDefaultProcessAttributes();
		newProcessInstance(launchInstsance, process, program, processAttributes);
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

	protected ProcessInstance newProcessInstance(LaunchInstance launchInstance, Process process, String label, Map<String, String> attributes) throws IOException {
		return new ProcessInstanceImpl(launchInstance, process, label, attributes);
	}

}
