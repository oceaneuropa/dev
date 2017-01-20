package org.origin.common.resource;

import java.net.URI;

public interface ResourceFactory<RES extends Resource> {

	/**
	 * Check whether a URI is supported by a resource factory.
	 * 
	 * @param uri
	 * @return
	 */
	boolean isSupported(URI uri);

	/**
	 * Create a resource for a URI.
	 * 
	 * @param uri
	 * @return
	 */
	RES createResource(URI uri);

}
