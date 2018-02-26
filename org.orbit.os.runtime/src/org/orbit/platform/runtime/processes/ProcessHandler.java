/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.runtime.processes;

import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.extension.IProgramExtension;

public interface ProcessHandler {

	public enum RUNTIME_STATE {
		STARTED("STARTED"), //
		START_FAILED("START_FAILED"), //
		STOPPED("STOPPED"), //
		STOP_FAILED("STOP_FAILED"); //

		protected String state;

		RUNTIME_STATE(String state) {
			this.state = state;
		}

		public boolean isStarted() {
			return ("STARTED").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isStartFailed() {
			return ("START_FAILED").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isStopped() {
			return ("STOPPED").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isStopFailed() {
			return ("STOP_FAILED").equalsIgnoreCase(this.state) ? true : false;
		}
	}

	IProgramExtension getExtension();

	IProcess getProcess();

	RUNTIME_STATE getRuntimeState();

}
