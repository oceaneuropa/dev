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

		// GAIA
		ProgramExtension gaiaExtension = new ProgramExtension(typeId, GAIAServiceActivator.ID, "GAIA activator", "GAIA activator description");
		InterfaceDescription gaiaDesc = new InterfaceDescription("GAIA", ServiceActivator.class, GAIAServiceActivator.class.getName());
		gaiaDesc.setParameters( //
				new Parameter("gaia.name", "GAIA instance name"), //
				new Parameter("gaia.context_root", "web service context root") //
		);
		gaiaExtension.addInterface(gaiaDesc);
		addExtension(gaiaExtension);
	}

}

// new Parameter("gaia.host.url", "host url", true), //
// new Parameter("orbit.host.url", "generic host url", true) //