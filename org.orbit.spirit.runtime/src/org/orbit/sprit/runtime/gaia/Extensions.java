/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.sprit.runtime.gaia;

import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.extension.desc.InterfaceDescription;
import org.orbit.platform.sdk.extension.desc.Parameter;
import org.orbit.platform.sdk.extension.desc.ProgramExtension;
import org.orbit.platform.sdk.extension.desc.ProgramExtensions;
import org.orbit.sprit.runtime.gaia.extensions.GAIAServiceActivator;

/**
 * Spirit extensions
 * 
 */
public class Extensions extends ProgramExtensions {

	public static Extensions INSTANCE = new Extensions();

	@Override
	public void createExtensions() {
		createServiceActivatorExtensions();
	}

	protected void createServiceActivatorExtensions() {
		String typeId = ServiceActivator.TYPE_ID;

		// 1. GAIA
		ProgramExtension gaiaExtension = new ProgramExtension(typeId, GAIAServiceActivator.ID);
		gaiaExtension.setName("GAIA activator");
		gaiaExtension.setDescription("GAIA activator description");
		InterfaceDescription gaiaDesc = new InterfaceDescription("GAIA");
		gaiaDesc.setParameters( //
				new Parameter("gaia.name", "GAIA instance name"), //
				new Parameter("gaia.context_root", "web service context root") //
		// new Parameter("gaia.host.url", "host url", true), //
		// new Parameter("orbit.host.url", "generic host url", true) //
		);
		gaiaExtension.addInterface(ServiceActivator.class, GAIAServiceActivator.INSTANCE, gaiaDesc);
		addExtension(gaiaExtension);
	}

}
