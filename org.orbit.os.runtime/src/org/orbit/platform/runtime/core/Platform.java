/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.runtime.core;

import org.orbit.platform.runtime.cli.PlatformCommand;
import org.orbit.platform.runtime.command.service.CommandService;
import org.orbit.platform.runtime.programs.ProgramsAndFeatures;
import org.orbit.sdk.extension.IProgramExtensionService;
import org.origin.common.adapter.IAdaptable;
import org.osgi.framework.BundleContext;

public interface Platform extends IAdaptable {

	String getName();

	String getVersion();

	void start(BundleContext bundleContext) throws Exception;

	void stop(BundleContext bundleContext) throws Exception;

	IProgramExtensionService getProgramExtensionService();

	CommandService getCommandService();

	PlatformCommand getOSCommand();

	ProgramsAndFeatures getProgramsAndFeatures();

}
