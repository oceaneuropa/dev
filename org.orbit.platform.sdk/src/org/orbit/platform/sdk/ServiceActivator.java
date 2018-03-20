/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.sdk;

public interface ServiceActivator {

	public static final String TYPE_ID = "platform.sdk.ServiceActivator";

	/**
	 * Start service.
	 * 
	 * @param context
	 * @return
	 */
	void start(IPlatformContext context, IProcess process);

	/**
	 * Stop service.
	 * 
	 * @param context
	 */
	void stop(IPlatformContext context, IProcess process);

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
