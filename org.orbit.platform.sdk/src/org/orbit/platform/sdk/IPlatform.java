/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.sdk;

import org.origin.common.adapter.IAdaptable;

public interface IPlatform extends IAdaptable {

	String getId();

	String getName();

	String getVersion();

	String getParentId();

	String getType();

	String getHostURL();

	String getContextRoot();

	String getHome();

}
