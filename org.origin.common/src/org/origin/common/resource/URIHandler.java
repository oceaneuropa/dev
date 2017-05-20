package org.origin.common.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

/**
 * @see org.eclipse.emf.ecore.resource.URIHandler
 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl
 *
 */
public interface URIHandler {

	/**
	 * Check whether the URI is supported by this handler.
	 * 
	 * @param uri
	 * @return
	 */
	boolean isSupported(URI uri);

	/**
	 * Check whether the URI exists.
	 * 
	 * @param uri
	 * @param options
	 * @return
	 */
	boolean exists(URI uri, Map<?, ?> options);

	/**
	 * Create an empty resource.
	 * 
	 * @param uri
	 * @param options
	 * @return
	 * @throws IOException
	 */
	boolean createNewResource(URI uri, Map<Object, Object> options) throws IOException;

	/**
	 * Create input stream from the URI.
	 * 
	 * @param uri
	 * @param options
	 * @return
	 * @throws IOException
	 */
	InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException;

	/**
	 * Create output stream for the URI.
	 * 
	 * @param uri
	 * @param options
	 * @return
	 * @throws IOException
	 */
	OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException;

	/**
	 * Deletes the contents of the URI.
	 * 
	 * @param uri
	 * @param options
	 * @throws IOException
	 */
	boolean delete(URI uri, Map<?, ?> options) throws IOException;

}
