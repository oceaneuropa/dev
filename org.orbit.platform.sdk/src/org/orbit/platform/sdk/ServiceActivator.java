/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.sdk;

public interface ServiceActivator {

	public static final String TYPE_ID = "platform.extension.ServiceActivator";

	/**
	 * Start a service.
	 * 
	 * @param context
	 * @param process
	 * @throws Exception
	 */
	void start(IPlatformContext context, IProcess process) throws Exception;

	/**
	 * Stop a service.
	 * 
	 * @param context
	 * @param process
	 * @throws Exception
	 */
	void stop(IPlatformContext context, IProcess process) throws Exception;

}

// /**
// * Whether the service should be started automatically.
// *
// * @param context
// * @return
// */
// boolean isAutoStart();
// /**
// * Whether the service is single instance.
// *
// * @param context
// * @return
// */
// boolean isSingleton();
// /**
// * Get the service name.
// *
// * @return
// */
// String getName();
