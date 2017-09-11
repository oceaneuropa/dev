package org.origin.core.resources.extension;

import org.origin.core.resources.IFile;
import org.origin.core.resources.IFolder;
import org.origin.core.resources.IPath;
import org.origin.core.resources.IResource;
import org.origin.core.resources.IWorkspace;

public interface ResourceProvider {

	String getId();

	boolean isSupported(IWorkspace root, IPath fullpath);

	IFile newFileInstance(IWorkspace root, IPath fullpath);

	IFolder newFolderInstance(IWorkspace root, IPath fullpath);

	<RES extends IResource> boolean isSupported(IWorkspace root, IPath fullpath, Class<RES> clazz);

	<FILE extends IFile> FILE newFileInstance(IWorkspace root, IPath fullpath, Class<FILE> clazz);

	<FOLDER extends IFolder> FOLDER newFolderInstance(IWorkspace root, IPath fullpath, Class<FOLDER> clazz);

	boolean isSupported(IWorkspace root, IPath fullpath, String className);

	IFile newFileInstance(IWorkspace root, IPath fullpath, String className);

	IFolder newFolderInstance(IWorkspace root, IPath fullpath, String className);

}
