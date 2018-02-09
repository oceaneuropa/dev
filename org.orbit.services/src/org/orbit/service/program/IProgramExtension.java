package org.orbit.service.program;

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
	 * 
	 * @return
	 */
	Map<Object, Object> getProperties();

	/**
	 * 
	 * @return
	 */
	IProgramLauncher getLauncher();

	/**
	 * Get filter of the program extension.
	 * 
	 * @return
	 */
	IProgramExtensionFilter getFilter();

}
