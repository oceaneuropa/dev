/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.runtime;

import org.orbit.platform.runtime.extensions.servicecontrol.GAIAServiceControl;
import org.orbit.platform.sdk.extension.util.ProgramExtension;
import org.orbit.platform.sdk.extension.util.ProgramExtensions;
import org.orbit.platform.sdk.servicecontrol.ServiceControl;

public class Extensions extends ProgramExtensions {

	public static Extensions INSTANCE = new Extensions();

	public static final String GAIA_SERVICE_EXTENSION_ID = "component.gaia.service";

	@Override
	public void createExtensions() {
		createServiceControlExtensions();
	}

	protected void createServiceControlExtensions() {
		String extensionTypeId = ServiceControl.EXTENSION_TYPE_ID;

		ProgramExtension gaiaServiceExtension = new ProgramExtension(extensionTypeId, GAIA_SERVICE_EXTENSION_ID);
		gaiaServiceExtension.setName("GAIA service");
		gaiaServiceExtension.setDescription("GAIA service description");
		gaiaServiceExtension.adapt(ServiceControl.class, GAIAServiceControl.INSTANCE);
		addExtension(gaiaServiceExtension);
	}

}
