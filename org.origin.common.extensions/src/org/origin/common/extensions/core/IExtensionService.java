package org.origin.common.extensions.core;

public interface IExtensionService {

	// OSGi service property names
	public static final String PROP_EXTENSION_TYPE_ID = "extensionTypeId";
	public static final String PROP_EXTENSION_ID = "extensionId";

	/**
	 * Get extension typeIds.
	 * 
	 * @return
	 */
	String[] getExtensionTypeIds();

	/**
	 * Get all extensions.
	 * 
	 * @return
	 */
	IExtension[] getExtensions();

	/**
	 * Get extensions for a given extension type.
	 * 
	 * @param typeId
	 *            Extension type Id
	 * @return
	 */
	IExtension[] getExtensions(String typeId);

	/**
	 * Get extension for a given extension type and extension Id.
	 * 
	 * @param typeId
	 *            Extension type Id
	 * @param extensionId
	 *            Extension Id
	 * @return
	 */
	IExtension getExtension(String typeId, String extensionId);

}
