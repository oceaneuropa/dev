package org.orbit.service.program;

import org.osgi.framework.BundleContext;

public interface ProgramInstance {

	/**
	 * Get the BundleContext from which the program instance is launched.
	 * 
	 * @return
	 */
	BundleContext getBundleContext();

	/**
	 * Get the ProgramProvider from which the program instance is launched.
	 * 
	 * @return
	 */
	ProgramProvider getProgramProvider();

	/**
	 * Get the referenced real program instance.
	 * 
	 * @return
	 */
	Object getReferenceInstance();

	/**
	 * Exit the program instance.
	 */
	int exit();

}
