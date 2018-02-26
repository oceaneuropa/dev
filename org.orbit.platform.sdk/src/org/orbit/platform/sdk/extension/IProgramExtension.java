/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.sdk.extension;

import java.util.Map;

import org.origin.common.adapter.IAdaptable;

public interface IProgramExtension extends IAdaptable {

	public interface Context extends IAdaptable {

		Object getElement();
	}

	/**
	 * Get extension type id.
	 * 
	 * @return
	 */
	String getTypeId();

	/**
	 * Get extension id.
	 * 
	 * @return
	 */
	String getId();

	/**
	 * Get extension name.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Get extension description.
	 * 
	 * @return
	 */
	String getDescription();

	/**
	 * Get properties of the extension.
	 * 
	 * @return
	 */
	Map<Object, Object> getProperties();

	/**
	 * Get program extension filter.
	 * 
	 * @return
	 */
	IProgramExtensionFilter getFilter();

}

// /**
// * Get launcher from the extension for launching new program instance.
// *
// * @return
// */
// IProgramLauncher getLauncher();
