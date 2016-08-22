package org.origin.common.workingcopy;

import java.io.File;

import org.origin.common.runtime.ActivationAware;

public interface WorkingCopyFactory<ELEMENT> extends ActivationAware {

	/**
	 * Get factory name.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Called when the factory is added to registry.
	 * 
	 */
	public void activate();

	/**
	 * Called when the factory is removed from registry.
	 * 
	 */
	public void deactivate();

	/**
	 * Check whether a file is supported by the factory.
	 * 
	 * @param file
	 * @return
	 */
	public boolean isSupported(File file);

	/**
	 * Check whether the factory contains a cache of the file.
	 * 
	 * @param file
	 * @return
	 */
	public boolean hasWorkingCopy(File file);

	/**
	 * Get the cache of a file.
	 * 
	 * @param file
	 * @return
	 */
	public WorkingCopy<ELEMENT> getWorkingCopy(File file);

	/**
	 * Remove the cache of a file.
	 * 
	 * @param file
	 * @return
	 */
	public WorkingCopy<ELEMENT> removeWorkingCopy(File file);

}
