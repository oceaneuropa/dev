package org.origin.common.extensions.core;

import java.util.Map;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.extensions.util.InterfacesAware;

public interface IExtension extends IAdaptable, InterfacesAware {

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
	 * 
	 * @param propName
	 * @return
	 */
	Object getProperty(Object propName);

}

// /**
// *
// * @return
// */
// String getRealm();

// public interface Context extends IAdaptable {
// Object getElement();
// }
