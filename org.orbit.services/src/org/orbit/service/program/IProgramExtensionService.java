package org.orbit.service.program;

public interface IProgramExtensionService {

	// OSGi service property names
	public static final String PROP_EXTENSION_TYPE_ID = "extensionTypeId";
	public static final String PROP_EXTENSION_ID = "extensionId";
	public static IProgramExtension[] EMPTY_ARRAY = new IProgramExtension[] {};

	/**
	 * Get program extensions for a given extension type.
	 * 
	 * @param typeId
	 *            Extension type Id
	 * @return
	 */
	IProgramExtension[] getProgramExtensions(String typeId);

	/**
	 * Get program extension for a given extension type and extension Id.
	 * 
	 * @param typeId
	 *            Extension type Id
	 * @param extensionId
	 *            Extension Id
	 * @return
	 */
	IProgramExtension getProgramExtension(String typeId, String extensionId);

}
