/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.sprit.runtime.gaia;

import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.sprit.runtime.gaia.extensions.GAIAServiceActivator;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.Parameter;
import org.origin.common.extensions.ProgramExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spirit extensions
 * 
 */
public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.spirit.runtime");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createServiceActivatorExtensions();
	}

	protected void createServiceActivatorExtensions() {
		String typeId = ServiceActivator.TYPE_ID;

		// GAIA
		Extension gaiaExtension = new Extension(typeId, GAIAServiceActivator.ID, "GAIA activator", "GAIA activator description");
		InterfaceDescription gaiaDesc = new InterfaceDescription("GAIA", ServiceActivator.class, GAIAServiceActivator.class);
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