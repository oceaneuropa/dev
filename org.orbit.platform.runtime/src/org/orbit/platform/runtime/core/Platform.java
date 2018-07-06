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

public interface Platform extends WebServiceAware, IAdaptable {

	String getId();

	String getName();

	String getVersion();

	String getHostURL();

	String getContextRoot();

	String getHome();

	void updateProperties(Map<Object, Object> properties);

	WSEditPolicies getEditPolicies();

	CommandService getCommandService();

	ProgramsAndFeatures getProgramsAndFeatures();

	ProcessManager getProcessManager();

}
