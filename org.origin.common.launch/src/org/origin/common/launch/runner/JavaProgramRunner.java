package org.origin.common.launch.runner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.origin.common.launch.LaunchInstance;

/**
 * @see org.eclipse.jdt.internal.launching.StandardVMRunner
 * @see org.eclipse.jdt.internal.launching.StandardVMDebugger
 *
 */
public class JavaProgramRunner extends AbstractProgramRunner {

	public JavaProgramRunner() {
	}

	@Override
	public void run(ProgramConfiguration config, LaunchInstance launchInstance) throws IOException {
		// String program = constructProgramString(config);
		String program = config.getProgram();

		// Normal
		// [/Library/Java/JavaVirtualMachines/jdk1.8.0_05.jdk/Contents/Home/jre/bin/java, -XstartOnFirstThread, -Declipse.ignoreApp=true,
		// -Dosgi.noShutdown=true, -Dfile.encoding=UTF-8, -classpath,
		// /Users/yayang/tibco-build/bw/tibco.home/bw/6.5/devutils/design/../../../../../eclipse/4.4/plugins/org.eclipse.equinox.launcher_1.3.0.v20140415-2008.jar,
		// org.eclipse.equinox.launcher.Main, -dev, file:/Users/yayang/tibco/workspaces/BW642_V2/.metadata/.plugins/org.eclipse.pde.core/config1/dev.properties,
		// -configuration, file:/Users/yayang/tibco/workspaces/BW642_V2/.metadata/.plugins/org.eclipse.pde.core/config1/, -os, macosx, -ws, cocoa, -arch,
		// x86_64, -nl, en_US, -consoleLog, -console]

		// Created
		// [java, -Declipse.ignoreApp=true, -Dosgi.noShutdown=true, -jar, /Users/yayang/origin/ta1/plugins/org.eclipse.osgi_3.10.100.v20150529-1857.jar,
		// -configuration, file:/Users/yayang/origin/ta1/nodespaces/node0/configuration, -consoleLog, -console]

		List<String> arguments = new ArrayList<String>();

		// program arguments
		// e.g.
		// java
		arguments.add(program);

		// VM arguments
		// e.g.
		// -Declipse.ignoreApp=true
		// -Dosgi.noShutdown=true
		String[] vmArguments = config.getVMArguments();
		for (String vmArgument : vmArguments) {
			arguments.add(vmArgument);
		}

		// System arguments
		// e.g.
		// -jar,
		// "/Users/yayang/origin/ta1/plugins/org.eclipse.osgi_3.10.100.v20150529-1857.jar,
		// -configuration,
		// "/Users/yayang/origin/ta1/nodespaces/node0/configuration
		String[] systemArguments = config.getSystemArguments();
		for (String systemArgument : systemArguments) {
			arguments.add(systemArgument);
		}

		// main
		// String mainClass = config.getMainClass();
		// if (mainClass != null) {
		// arguments.add(mainClass);
		// }

		// program arguments
		// e.g. -consoleLog, -console
		String[] programArguments = config.getProgramArguments();
		for (String programArgument : programArguments) {
			arguments.add(programArgument);
		}

		String[] cmdLine = arguments.toArray(new String[arguments.size()]);
		System.out.println(Arrays.toString(cmdLine));

		File workingDirectory = getWorkingDirectory(config);

		Process process = exec(cmdLine, workingDirectory, null); // envp is null
		if (process == null) {
			return;
		}

		// String timestamp = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new Date(System.currentTimeMillis()));
		// newProcess(launchHandler, process, renderProcessLabel(cmdLine, timestamp), getDefaultProcessMap());
		newProcessInstance(launchInstance, process, cmdLine[0], getDefaultProcessAttributes());

		// process.setAttribute(DebugPlugin.ATTR_PATH, cmdLine[0]);
		// process.setAttribute(RuntimeProcess.ATTR_CMDLINE, renderCommandLine(cmdLine));
		// String ltime = launch.getAttribute(DebugPlugin.ATTR_LAUNCH_TIMESTAMP);
		// process.setAttribute(DebugPlugin.ATTR_LAUNCH_TIMESTAMP, ltime != null ? ltime : timestamp);
		// if (workingDir != null) {
		// process.setAttribute(DebugPlugin.ATTR_WORKING_DIRECTORY, workingDir.getAbsolutePath());
		// }
		// if (envp != null) {
		// Arrays.sort(envp);
		// StringBuffer buff = new StringBuffer();
		// for (int i = 0; i < envp.length; i++) {
		// buff.append(envp[i]);
		// if (i < envp.length - 1) {
		// buff.append('\n');
		// }
		// }
		// process.setAttribute(DebugPlugin.ATTR_ENVIRONMENT, buff.toString());
		// }
	}

	protected void addArguments(String[] args, List<String> v) {
		if (args == null) {
			return;
		}
		for (int i = 0; i < args.length; i++) {
			v.add(args[i]);
		}
	}

	protected File getWorkingDirectory(ProgramConfiguration config) throws IOException {
		String path = config.getWorkingDirectory();
		if (path == null) {
			return null;
		}
		File dir = new File(path);
		if (!dir.isDirectory()) {
			// abort(NLS.bind(LaunchingMessages.StandardVMRunner_Specified_working_directory_does_not_exist_or_is_not_a_directory___0__3, new String[] {path}),
			// null, IJavaLaunchConfigurationConstants.ERR_WORKING_DIRECTORY_DOES_NOT_EXIST);
		}
		return dir;
	}

}
