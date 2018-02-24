/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.runtime.platform;

import java.util.Map;

import org.orbit.platform.runtime.command.service.CommandService;
import org.orbit.platform.runtime.programs.ProgramsAndFeatures;
import org.orbit.platform.sdk.extension.IProgramExtensionService;
import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.editpolicy.WSEditPolicies;

public interface Platform extends IAdaptable {

	String getName();

	String getVersion();

	String getHostURL();

	String getContextRoot();

	String getHome();

	void updateProperties(Map<Object, Object> properties);

	WSEditPolicies getEditPolicies();

	IProgramExtensionService getProgramExtensionService();

	CommandService getCommandService();

	ProgramsAndFeatures getProgramsAndFeatures();

	// void stop(IProcess process);

}
