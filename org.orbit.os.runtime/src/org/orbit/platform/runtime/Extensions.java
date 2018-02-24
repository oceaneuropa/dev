/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.runtime;

import org.orbit.platform.runtime.extensions.servicecontrol.GAIAServiceActivator;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.extension.util.ProgramExtension;
import org.orbit.platform.sdk.extension.util.ProgramExtensions;

public class Extensions extends ProgramExtensions {

	public static Extensions INSTANCE = new Extensions();

	@Override
	public void createExtensions() {
		createServiceActivatorExtensions();
	}

	protected void createServiceActivatorExtensions() {
		String typeId = ServiceActivator.TYPE_ID;

		ProgramExtension gaiaActivatorExtension = new ProgramExtension(typeId, GAIAServiceActivator.ID);
		gaiaActivatorExtension.setName("GAIA service activator");
		gaiaActivatorExtension.setDescription("GAIA service activator description");
		gaiaActivatorExtension.adapt(ServiceActivator.class, GAIAServiceActivator.INSTANCE);
		addExtension(gaiaActivatorExtension);
	}

}
