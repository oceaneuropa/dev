package org.origin.common.workingcopy;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.resource.Resource;

public interface WorkingCopy extends IAdaptable {

	/**
	 * Get resource URI.
	 * 
	 * @return
	 */
	URI getURI();

	/**
	 * Set working copy factory.
	 * 
	 * @param factory
	 */
	void setFactory(WorkingCopyFactory factory);

	/**
	 * Get working copy factory.
	 * 
	 * @return
	 */
	WorkingCopyFactory getFactory();

	// -----------------------------------------------------------------------------
	// Resource instance
	// -----------------------------------------------------------------------------
	/**
	 * Get the resource.
	 * 
	 * @return
	 * @throws IOException
	 */
	Resource getResource() throws IOException;

	// -----------------------------------------------------------------------------
	// Load
	// -----------------------------------------------------------------------------
	/**
	 * Check whether the working copy is loaded.
	 * 
	 * @return
	 */
	boolean isLoaded();

	/**
	 * Reload the working copy.
	 * 
	 * @throws IOException
	 */
	void reload() throws IOException;

	/**
	 * Unload the working copy.
	 */
	void unload();

	// -----------------------------------------------------------------------------
	// Save
	// -----------------------------------------------------------------------------
	/**
	 * Save the working copy.
	 * 
	 * @throws IOException
	 */
	void save() throws IOException;

	// -----------------------------------------------------------------------------
	// Root content
	// -----------------------------------------------------------------------------
	/**
	 * Get root element of the resource.
	 * 
	 * @param <T>
	 * 
	 * @return
	 * @throws IOException
	 */
	<T> T getRootElement(Class<T> elementClass) throws IOException;

	/**
	 * Get the contents.
	 *
	 * @return
	 * @throws IOException
	 */
	List<Object> getContents() throws IOException;

	/**
	 * Get the contents of specified class.
	 * 
	 * @param elementClass
	 * @return
	 * @throws IOException
	 */
	<T> List<T> getContents(Class<T> elementClass) throws IOException;

}
