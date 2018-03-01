/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.sdk.extension;

public interface IProgramExtensionService {

	// OSGi service property names
	public static final String PROP_EXTENSION_TYPE_ID = "extensionTypeId";
	public static final String PROP_EXTENSION_ID = "extensionId";
	public static IProgramExtension[] EMPTY_ARRAY = new IProgramExtension[] {};

	/**
	 * 
	 * @return
	 */
	String[] getExtensionTypeIds();

	/**
	 * Get all program extensions.
	 * 
	 * @return
	 */
	IProgramExtension[] getExtensions();

	/**
	 * Get program extensions for a given extension type.
	 * 
	 * @param typeId
	 *            Extension type Id
	 * @return
	 */
	IProgramExtension[] getExtensions(String typeId);

	/**
	 * Get program extension for a given extension type and extension Id.
	 * 
	 * @param typeId
	 *            Extension type Id
	 * @param extensionId
	 *            Extension Id
	 * @return
	 */
	IProgramExtension getExtension(String typeId, String extensionId);

}
