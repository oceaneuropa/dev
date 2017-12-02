package org.orbit.os.server.apps;

import java.util.List;

import org.orbit.app.AppManifest;
import org.origin.common.runtime.Problem;

public interface AppHandler {

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

	AppManifest getAppManifest();

	App getApp();

	AppHandler.RUNTIME_STATE getRuntimeState();

	void activate() throws AppException;

	void deactivate() throws AppException;

	void startApp() throws AppException;

	void stopApp() throws AppException;

	List<Problem> getProblems();

}
