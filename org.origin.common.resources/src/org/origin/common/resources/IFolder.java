package org.origin.common.resources;

import java.io.IOException;

public interface IFolder extends IResource {

	IResource[] getMembers();

	IResource findMember(String name);

	<RESOURCE extends IResource> RESOURCE findMember(String name, Class<RESOURCE> clazz);

	boolean exists(IPath path);

	IFile getFile(String name) throws IOException;

	IFile getFile(IPath path) throws IOException;

	IFolder getFolder(String name) throws IOException;

	IFolder getFolder(IPath path) throws IOException;

	boolean create(FolderDescription desc) throws IOException;

	void setDescription(FolderDescription desc) throws IOException;

	FolderDescription getDescription() throws IOException;

}
