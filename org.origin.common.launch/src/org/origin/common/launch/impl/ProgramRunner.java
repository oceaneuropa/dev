package org.origin.common.launch.impl;

import java.io.IOException;

import org.origin.common.launch.LaunchHandler;

public interface ProgramRunner {

	public static class ProgramConfiguration {
		protected static final String[] EMPTY = new String[0];

		protected String program;
		protected String mainClass;
		protected String[] systemArgs;
		protected String[] vmArgs;
		protected String[] programArgs;
		protected String[] classPath;
		protected String workingDirectory;

		/**
		 * 
		 * @param program
		 * @param mainClass
		 * @param classPath
		 */
		public ProgramConfiguration(String program, String mainClass, String[] classPath) {
			this.program = program;
			this.mainClass = mainClass;
			this.classPath = classPath;
		}

		public String getProgram() {
			return this.program;
		}

		public void setProgram(String program) {
			this.program = program;
		}

		public String getMainClass() {
			return this.mainClass;
		}

		public String[] getClassPath() {
			return this.classPath;
		}

		public void setSystemArguments(String[] systemArgs) {
			this.systemArgs = systemArgs;
		}

		public String[] getSystemArguments() {
			return this.systemArgs;
		}

		public void setVMArguments(String[] vmArgs) {
			this.vmArgs = vmArgs;
		}

		public String[] getVMArguments() {
			if (this.vmArgs == null) {
				return EMPTY;
			}
			return this.vmArgs;
		}

		public void setProgramArguments(String[] programArgs) {
			this.programArgs = programArgs;
		}

		public String[] getProgramArguments() {
			if (this.programArgs == null) {
				return EMPTY;
			}
			return this.programArgs;
		}

		public void setWorkingDirectory(String path) {
			this.workingDirectory = path;
		}

		public String getWorkingDirectory() {
			return this.workingDirectory;
		}
	}

	/**
	 * 
	 * @param config
	 * @param launchHandler
	 * @throws IOException
	 */
	void run(ProgramConfiguration config, LaunchHandler launchHandler) throws IOException;

}
