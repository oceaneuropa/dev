package other.orbit.platform.sdk;

import org.origin.common.extensions.core.IExtension.Context;

public interface IProgramInstance {

	/**
	 * Get the BundleContext from which the program instance is launched.
	 * 
	 * @return
	 */
	Context getContext();

	/**
	 * Get the ProgramLauncher from which the program instance is launched.
	 * 
	 * @return
	 */
	IProgramLauncher getLauncher();

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
