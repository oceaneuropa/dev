
/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.sdk;

import org.origin.common.adapter.IAdaptable;

public interface IProcess extends IAdaptable {

	int getPID();

	String getName();

}
