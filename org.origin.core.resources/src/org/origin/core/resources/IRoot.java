package org.origin.core.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IRoot {

	IResource[] getRootMembers();

	IResource findRootMember(String name);

	<RESOURCE extends IResource> RESOURCE findRootMember(String name, Class<RESOURCE> clazz);

	IResource[] getMembers(IPath fullpath);

	IFolder getParent(IPath fullpath) throws IOException;

	IFile getFile(IPath fullpath) throws IOException;

	IFolder getFolder(IPath fullpath) throws IOException;

	<FILE extends IFile> FILE getFile(IPath fullpath, Class<FILE> clazz) throws IOException;

	<FOLDER extends IFolder> FOLDER getFolder(IPath fullpath, Class<FOLDER> clazz) throws IOException;

	InputStream getInputStream(IPath fullpath) throws IOException;

	OutputStream getOutputStream(IPath fullpath) throws IOException;

	boolean delete(IPath fullpath);

	boolean underlyingResourceExists(IPath fullpath);

	Object getUnderlyingResource(IPath fullpath);

	boolean createUnderlyingFile(IPath fullpath) throws IOException;

	boolean createUnderlyingFile(IPath fullpath, InputStream input) throws IOException;

	boolean createUnderlyingFolder(IPath fullpath) throws IOException;

	void dispose();

}
