package org.origin.common.resources.util;

import org.origin.common.resources.IFile;
import org.origin.common.resources.IFolder;
import org.origin.common.resources.IPath;
import org.origin.common.resources.IResource;
import org.origin.common.resources.IWorkspace;

public interface ResourceProvider {

	String getId();

	boolean isSupported(IWorkspace workspace, IPath fullpath);

	IFile newFileInstance(IWorkspace workspace, IPath fullpath);

	IFolder newFolderInstance(IWorkspace workspace, IPath fullpath);

	<RES extends IResource> boolean isSupported(IWorkspace workspace, IPath fullpath, Class<RES> clazz);

	<FILE extends IFile> FILE newFileInstance(IWorkspace workspace, IPath fullpath, Class<FILE> clazz);

	<FOLDER extends IFolder> FOLDER newFolderInstance(IWorkspace workspace, IPath fullpath, Class<FOLDER> clazz);

	boolean isSupported(IWorkspace workspace, IPath fullpath, String className);

	IFile newFileInstance(IWorkspace workspace, IPath fullpath, String className);

	IFolder newFolderInstance(IWorkspace workspace, IPath fullpath, String className);

}
