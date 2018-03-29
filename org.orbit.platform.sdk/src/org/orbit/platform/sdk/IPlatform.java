/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.sdk;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.extensions.core.IExtensionService;

public interface IPlatform extends IAdaptable {

	IProcessManager getIProcessManager();

	IExtensionService getExtensionService();

}
