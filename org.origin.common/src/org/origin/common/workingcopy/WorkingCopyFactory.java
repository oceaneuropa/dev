package org.origin.common.workingcopy;

import java.net.URI;

import org.origin.common.runtime.ActivationAware;

public interface WorkingCopyFactory extends ActivationAware {

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
	 * Check whether a URI is supported by the factory.
	 * 
	 * @param uri
	 * @return
	 */
	public boolean isSupported(URI uri);

	/**
	 * Check whether the factory contains a cache of the URI.
	 * 
	 * @param uri
	 * @return
	 */
	public boolean hasWorkingCopy(URI uri);

	/**
	 * Get the cache of a URI.
	 * 
	 * @param uri
	 * @return
	 */
	public WorkingCopy getWorkingCopy(URI uri);

	/**
	 * Remove the cache of a URI.
	 * 
	 * @param uri
	 * @return
	 */
	public WorkingCopy removeWorkingCopy(URI uri);

}
