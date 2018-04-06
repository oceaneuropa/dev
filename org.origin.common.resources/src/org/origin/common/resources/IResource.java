package org.origin.common.resources;

import java.io.IOException;

import org.origin.common.adapter.IAdaptable;

public interface IResource extends IAdaptable {

	IWorkspace getWorkspace();

	IFolder getParent() throws IOException;

	String getName();

	IPath getFullPath();

	boolean exists();

	boolean create() throws IOException;

	boolean delete() throws IOException;

	void dispose();

}
