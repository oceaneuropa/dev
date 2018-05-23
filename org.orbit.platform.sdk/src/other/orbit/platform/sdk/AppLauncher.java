/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package other.orbit.platform.sdk;

import org.origin.common.adapter.IAdaptable;
import org.osgi.framework.BundleContext;

public interface AppLauncher {

	public interface Context extends IAdaptable {

	}

	public static final String EXTENSION_TYPE_ID = "platform.app.launcher";

	/**
	 * Launch a new instance of the App.
	 * 
	 * @param bundleContext
	 * @param context
	 */
	AppInstance newInstance(BundleContext bundleContext, Context context);

	/**
	 * Terminate an instance of the App.
	 * 
	 * @param bundleContext
	 * @param appInstance
	 */
	void exit(BundleContext bundleContext, AppInstance appInstance);

}
