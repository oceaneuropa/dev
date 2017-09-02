package org.origin.core.resources;

public interface ResourceProvider {

	String getId();

	boolean isSupported(IRoot root, IPath fullpath);

	IFile newFileInstance(IRoot root, IPath fullpath);

	IFolder newFolderInstance(IRoot root, IPath fullpath);

	<RES extends IResource> boolean isSupported(IRoot root, IPath fullpath, Class<RES> clazz);

	<FILE extends IFile> FILE newFileInstance(IRoot root, IPath fullpath, Class<FILE> clazz);

	<FOLDER extends IFolder> FOLDER newFolderInstance(IRoot root, IPath fullpath, Class<FOLDER> clazz);

}
