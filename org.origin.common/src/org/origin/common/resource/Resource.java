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
	URI getURI();

	/**
	 * Get URIConverter.
	 * 
	 * @return
	 */
	URIConverter getURIConverter();

	/**
	 * Set URIConverter.
	 * 
	 * @param uriConverter
	 */
	void setURIConverter(URIConverter uriConverter);

	/**
	 * Check whether URI exists.
	 * 
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	boolean exists() throws IOException;

	/**
	 * Create new resource.
	 * 
	 * @return
	 * @throws IOException
	 */
	boolean createNewResource() throws IOException;

	/**
	 * Load resource from URI.
	 * 
	 * @param uri
	 * @throws IOException
	 */
	void load() throws IOException;

	/**
	 * Load resource from input stream.
	 * 
	 * @param input
	 * @throws IOException
	 */
	void load(InputStream input) throws IOException;

	/**
	 * Save resource to URI.
	 * 
	 * @param uri
	 * @throws IOException
	 */
	void save() throws IOException;

	/**
	 * Save resource to output stream.
	 * 
	 * @param output
	 * @throws IOException
	 */
	void save(OutputStream output) throws IOException;

	/**
	 * Delete the resource.
	 * 
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	boolean delete() throws IOException;

	/**
	 * Check whether resource's contents are empty.
	 * 
	 * @return
	 */
	boolean isEmpty();

	/**
	 * Get the contents of the resource.
	 * 
	 * @return
	 */
	List<Object> getContents();

	/**
	 * Clear the contents of the resource.
	 */
	void clear();

}
