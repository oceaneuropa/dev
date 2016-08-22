package org.origin.common.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import org.origin.common.adapter.IAdaptable;

public interface Resource extends IAdaptable {

	/**
	 * 
	 * @return
	 */
	public URI getURI();

	/**
	 * Load resource from input stream.
	 * 
	 * @param input
	 * @throws IOException
	 */
	public void load(InputStream input) throws IOException;

	/**
	 * Save resource to output stream.
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void save(OutputStream output) throws IOException;

	/**
	 * Check whether resource's contents are empty.
	 * 
	 * @return
	 */
	public boolean isEmpty();

	/**
	 * Get the contents of the resource.
	 * 
	 * @return
	 */
	public List<Object> getContents();

	/**
	 * Clear the contents of the resource.
	 */
	public void clear();

}
