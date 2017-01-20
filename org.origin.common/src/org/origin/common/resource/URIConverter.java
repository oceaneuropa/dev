package org.origin.common.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @see org.eclipse.emf.ecore.resource.URIConverter
 * @see org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface URIConverter {

	/**
	 * An option to pass the calling URIController to the URIHandlers.
	 */
	String OPTION_URI_CONTROLLER = "URI_CONTROLLER";

	/**
	 * An option to pass a Map<Object, Object> to yield results in addition to the returned value of the method.
	 */
	String OPTION_RESPONSE = "RESPONSE";

	/**
	 * An option to pass a milliseconds timeout value. If the operation cannot be completed within timeout value, for the URI supports timeouts, an IOException is thrown. The default value is 0 for
	 * infinite timeout.
	 */
	String OPTION_TIMEOUT = "TIMEOUT";

	/**
	 * A property of the OPTION_RESPONSE response option to yield the time stamp associated with the creation of an input stream or an output stream.
	 */
	String RESPONSE_TIME_STAMP_PROPERTY = "TIME_STAMP";

	/**
	 * 
	 * @param uri
	 * @return
	 */
	URI normalize(URI uri);

	/**
	 * 
	 * @param options
	 * @param keyValuePairs
	 * @return
	 */
	Map<Object, Object> normalize(Map<Object, Object> options, Object[]... keyValuePairs);

	/**
	 * 
	 * @return
	 */
	List<URIHandler> getURIHandlers();

	/**
	 * 
	 * @param uri
	 * @return
	 */
	URIHandler getURIHandler(URI uri);

	/**
	 * 
	 * @param uri
	 * @return
	 */
	boolean exists(URI uri);

	/**
	 * 
	 * @param uri
	 * @param options
	 * @return
	 */
	boolean exists(URI uri, Map<Object, Object> options);

	/**
	 * 
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	boolean createNewResource(URI uri) throws IOException;

	/**
	 * 
	 * @param uri
	 * @param options
	 * @return
	 * @throws IOException
	 */
	boolean createNewResource(URI uri, Map<Object, Object> options) throws IOException;

	/**
	 * 
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	InputStream createInputStream(URI uri) throws IOException;

	/**
	 * 
	 * @param uri
	 * @param options
	 * @return
	 * @throws IOException
	 */
	InputStream createInputStream(URI uri, Map<Object, Object> options) throws IOException;

	/**
	 * 
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	OutputStream createOutputStream(URI uri) throws IOException;

	/**
	 * 
	 * @param uri
	 * @param options
	 * @return
	 * @throws IOException
	 */
	OutputStream createOutputStream(URI uri, Map<Object, Object> options) throws IOException;

	/**
	 * 
	 * @param uri
	 * @throws IOException
	 */
	boolean delete(URI uri) throws IOException;

	/**
	 * 
	 * @param uri
	 * @param options
	 * @throws IOException
	 */
	boolean delete(URI uri, Map<Object, Object> options) throws IOException;

}
