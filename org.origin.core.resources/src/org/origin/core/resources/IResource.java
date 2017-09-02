package org.origin.core.resources;

import java.io.IOException;

import org.origin.common.adapter.IAdaptable;

public interface IResource extends IAdaptable {

	IRoot getRoot();

	IFolder getParent() throws IOException;

	String getName();

	IPath getFullPath();

	boolean exists();

	boolean create() throws IOException;

	boolean delete();

	void dispose();

}
