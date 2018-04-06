package org.origin.common.resources.extension;

import java.io.IOException;

import org.origin.common.resources.IResource;
import org.origin.common.resources.IWorkspace;

public interface ResourceConfigurator {

	public static final String TYPE_ID = "origin.common.resources.ResourceConfigurator";

	/**
	 * 
	 * @param workspace
	 * @param folder
	 * @throws IOException
	 */
	void configure(IWorkspace workspace, IResource folder) throws IOException;

}
