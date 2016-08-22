package org.origin.common.resource;

import java.io.File;

public interface ResourceFactory<RES extends Resource> {

	/**
	 * Check whether a file is supported by a resource factory.
	 * 
	 * @param file
	 * @return
	 */
	public boolean isSupported(File file);

	/**
	 * Create a resource for a file.
	 * 
	 * @param file
	 * @return
	 */
	public RES createResource(File file);

}
