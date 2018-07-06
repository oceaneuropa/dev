package org.origin.common.resources.extension;

import java.io.IOException;

import org.origin.common.resources.IResource;
import org.origin.common.resources.IWorkspace;

public interface ResourceBuilder {

	public static final String TYPE_ID = "origin.common.resources.ResourceBuilder";

	/**
	 * 
	 * @param workspace
	 * @param folder
	 * @throws IOException
	 */
	void build(IWorkspace workspace, IResource folder) throws IOException;

}
