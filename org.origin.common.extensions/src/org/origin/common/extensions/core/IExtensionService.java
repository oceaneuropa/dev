package org.origin.common.extensions.core;

public interface IExtensionService {

	// OSGi service property names
	public static final String PROP_REALM = "realm";
	public static final String PROP_EXTENSION_TYPE_ID = "extensionTypeId";
	public static final String PROP_EXTENSION_ID = "extensionId";

	/**
	 * Get extension typeIds from a realm.
	 * 
	 * @param realm
	 * @return
	 */
	String[] getExtensionTypeIds(String realm);

	/**
	 * Get all extensions from a realm.
	 * 
	 * @param realm
	 * @return
	 */
	IExtension[] getExtensions(String realm);

	/**
	 * Get extensions for a given extension type from a realm.
	 * 
	 * @param realm
	 * @param typeId
	 *            Extension type Id
	 * @return
	 */
	IExtension[] getExtensions(String realm, String typeId);

	/**
	 * Get extension for a given extension type and extension Id from a realm.
	 * 
	 * @param realm
	 * @param typeId
	 *            Extension type Id
	 * @param extensionId
	 *            Extension Id
	 * @return
	 */
	IExtension getExtension(String realm, String typeId, String extensionId);

}
