package org.orbit.os.runtime.programs;

import java.util.List;

import org.orbit.os.api.apps.ProgramManifest;
import org.origin.common.runtime.Problem;

public interface ProgramHandler {

	public enum RUNTIME_STATE {
		ACTIVATED("ACTIVATED"), //
		DEACTIVATED("DEACTIVATED"); //

		protected String state;

		RUNTIME_STATE(String state) {
			this.state = state;
		}

		public boolean isActivated() {
			return ("ACTIVATED").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isDeactivated() {
			return ("DEACTIVATED").equalsIgnoreCase(this.state) ? true : false;
		}
	}

	ProgramManifest getManifest();

	Program getProgram();

	RUNTIME_STATE getRuntimeState();

	void activate() throws ProgramException;

	void deactivate() throws ProgramException;

	void start() throws ProgramException;

	void stop() throws ProgramException;

	List<Problem> getProblems();

}
