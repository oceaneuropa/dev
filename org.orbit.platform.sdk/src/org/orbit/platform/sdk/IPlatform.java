/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.sdk;

import org.orbit.platform.sdk.extension.IProgramExtensionService;
import org.origin.common.adapter.IAdaptable;

public interface IPlatform extends IAdaptable {

	IProcessManager getIProcessManager();

	IProgramExtensionService getExtensionService();

}
