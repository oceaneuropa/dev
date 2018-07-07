/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.runtime.core;

import java.util.Map;

import org.orbit.platform.runtime.command.service.CommandService;
import org.orbit.platform.runtime.processes.ProcessManager;
import org.orbit.platform.runtime.programs.ProgramsAndFeatures;
import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.common.service.WebServiceAware;

/*
 * stopped -> started | start_failed
 * 
 * start_failed -> started | start_failed | stopped
 * 
 * 
 * started -> stopped | stop_failed
 * 
 * stop_failed -> stopped | stop_failed
 * 
 */
public interface Platform extends WebServiceAware, IAdaptable {

	public enum RUNTIME_STATE {
		STARTED("STARTED"), //
		START_FAILED("START_FAILED"), //
		STOPPED("STOPPED"), //
		STOP_FAILED("STOP_FAILED"); //

		protected String state;

		RUNTIME_STATE(String state) {
			this.state = state;
		}

		public String getValue() {
			return this.state;
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

	RUNTIME_STATE getRuntimeState();

	String getId();

	String getName();

	String getVersion();

	String getParentId();

	String getType();

	String getHostURL();

	String getContextRoot();

	String getHome();

	void updateProperties(Map<Object, Object> properties);

	WSEditPolicies getEditPolicies();

	CommandService getCommandService();

	ProgramsAndFeatures getProgramsAndFeatures();

	ProcessManager getProcessManager();

}

// boolean canStart();
//
// boolean canStop();
//
// void start();
//
// void stop();
//
// void shutdown();
