package org.origin.common.resource.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.origin.common.resource.URIConverter;
import org.origin.common.resource.URIHandler;

/**
 * @see org.eclipse.emf.ecore.resource.URIConverter
 * @see org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl
 *
 */
public class URIConverterImpl implements URIConverter {

	public static List<URIHandler> DEFAULT_HANDLERS = Collections.unmodifiableList( //
			Arrays.asList( //
					new URIHandler[] { //
							new URIHandlerFileImpl(), //
							new URIHandlerImpl() //
	}));

	protected List<URIHandler> uriHandlers;

	/**
	 * 
	 */
	public URIConverterImpl() {
		this(DEFAULT_HANDLERS);
	}

	/**
	 * 
	 * @param uriHandlers
	 */
	public URIConverterImpl(List<URIHandler> uriHandlers) {
		this.uriHandlers = uriHandlers;
		if (this.uriHandlers == null) {
			this.uriHandlers = DEFAULT_HANDLERS;
		}
	}

	@Override
	public URI normalize(URI uri) {
		return uri;
	}

	@Override
	public Map<Object, Object> normalize(Map<Object, Object> options, Object[]... keyValuePairs) {
		if (options == null) {
			options = new HashMap<Object, Object>();
		}
		if (keyValuePairs != null) {
			for (Object[] keyValuePair : keyValuePairs) {
				if (keyValuePair.length >= 2) {
					Object key = keyValuePair[0];
					Object value = keyValuePair[1];
					if (key != null) {
						options.put(key, value);
					}
				}
			}
		}

		if (options.containsKey(OPTION_URI_CONTROLLER)) {
			options.put(OPTION_URI_CONTROLLER, this);
		}

		return options;
	}

	@Override
	public List<URIHandler> getURIHandlers() {
		return this.uriHandlers;
	}

	@Override
	public URIHandler getURIHandler(URI uri) {
		URIHandler result = null;
		for (URIHandler uriHandler : this.uriHandlers) {
			if (uriHandler.isSupported(uri)) {
				result = uriHandler;
				break;
			}
		}
		if (result == null) {
			throw new RuntimeException("There is no URIHandler to handle: " + uri);
		}
		return result;
	}

	@Override
	public boolean exists(URI uri) {
		return exists(uri, null);
	}

	@Override
	public boolean exists(URI uri, Map<Object, Object> options) {
		URI normalizedURI = normalize(uri);
		Map<Object, Object> normalizedOptions = normalize(options);
		return getURIHandler(normalizedURI).exists(normalizedURI, normalizedOptions);
	}

	@Override
	public boolean createNewResource(URI uri) throws IOException {
		return createNewResource(uri, null);
	}

	@Override
	public boolean createNewResource(URI uri, Map<Object, Object> options) throws IOException {
		URI normalizedURI = normalize(uri);
		Map<Object, Object> normalizedOptions = normalize(options);
		return getURIHandler(normalizedURI).createNewResource(normalizedURI, normalizedOptions);
	}

	@Override
	public InputStream createInputStream(URI uri) throws IOException {
		return createInputStream(uri, null);
	}

	@Override
	public InputStream createInputStream(URI uri, Map<Object, Object> options) throws IOException {
		URI normalizedURI = normalize(uri);
		Map<Object, Object> normalizedOptions = normalize(options);
		return getURIHandler(normalizedURI).createInputStream(normalizedURI, normalizedOptions);
	}

	@Override
	public OutputStream createOutputStream(URI uri) throws IOException {
		return createOutputStream(uri, null);
	}

	@Override
	public OutputStream createOutputStream(URI uri, Map<Object, Object> options) throws IOException {
		URI normalizedURI = normalize(uri);
		Map<Object, Object> normalizedOptions = normalize(options);
		return getURIHandler(normalizedURI).createOutputStream(normalizedURI, normalizedOptions);
	}

	@Override
	public boolean delete(URI uri) throws IOException {
		return delete(uri, null);
	}

	@Override
	public boolean delete(URI uri, Map<Object, Object> options) throws IOException {
		URI normalizedURI = normalize(uri);
		Map<Object, Object> normalizedOptions = normalize(options);
		return getURIHandler(normalizedURI).delete(normalizedURI, normalizedOptions);
	}

}
