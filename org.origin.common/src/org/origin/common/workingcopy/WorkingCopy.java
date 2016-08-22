package org.origin.common.workingcopy;

import java.io.File;
import java.io.IOException;

import org.origin.common.adapter.IAdaptable;

public interface WorkingCopy<ELEMENT> extends IAdaptable {

	/**
	 * Get the file object.
	 * 
	 * @return
	 */
	public File getFile();

	/**
	 * Check whether the cache is loaded.
	 * 
	 * @return
	 */
	public boolean isLoaded();

	/**
	 * Reload the cache.
	 * 
	 * @throws IOException
	 */
	public void reload() throws IOException;

	/**
	 * Save the file.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException;

	/**
	 * Get root element of the resource.
	 * 
	 * @return
	 * @throws IOException
	 */
	public ELEMENT getRootElement();

	/**
	 * Get root element of the resource.
	 * 
	 * @param <T>
	 * 
	 * @return
	 * @throws IOException
	 */
	public <T> T getRootElement(Class<T> elementClass);

}
