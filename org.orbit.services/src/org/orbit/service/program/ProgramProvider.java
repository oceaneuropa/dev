package org.orbit.service.program;

import org.origin.common.adapter.IAdaptable;
import org.osgi.framework.BundleContext;

public interface ProgramProvider extends IAdaptable {

	/**
	 * Get program type id.
	 * 
	 * @return
	 */
	String getTypeId();

	/**
	 * Get program provider id.
	 * 
	 * @return
	 */
	String getId();

	/**
	 * Launch a program.
	 * 
	 * @param context
	 * @return
	 */
	Object launch(BundleContext context);

	/**
	 * Exit the program.
	 * 
	 * @param context
	 * @param instance
	 * @return
	 */
	int exit(BundleContext context, Object instance);

}
