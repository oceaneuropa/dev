package org.origin.common.resources.impl.misc;

import org.origin.common.resources.IFile;
import org.origin.common.resources.IFolder;
import org.origin.common.resources.IPath;
import org.origin.common.resources.IResource;
import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.impl.FolderImpl;
import org.origin.common.resources.util.ResourceProvider;
import org.origin.common.resources.util.ResourceProviderExtensionRegistry;

public class FolderResourceProvider implements ResourceProvider {

	public static FolderResourceProvider INSTANCE = new FolderResourceProvider();
	public static String ID = "FolderResourceProvider";

	public static void register() {
		ResourceProviderExtensionRegistry.getInstance().register(INSTANCE);
	}

	public static void unregister() {
		ResourceProviderExtensionRegistry.getInstance().unregister(INSTANCE);
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public boolean isSupported(IWorkspace root, IPath fullpath) {
		IPath dotFolderFullpath = fullpath.append(".metadata").append(".folder");
		if (root.underlyingResourceExists(dotFolderFullpath)) {
			return true;
		}
		return false;
	}

	@Override
	public IFile newFileInstance(IWorkspace root, IPath fullpath) {
		return null;
	}

	@Override
	public IFolder newFolderInstance(IWorkspace root, IPath fullpath) {
		IFolder newFolderInstance = null;
		IPath dotFolderFullpath = fullpath.append(".metadata").append(".folder");
		if (root.underlyingResourceExists(dotFolderFullpath)) {
			newFolderInstance = new FolderImpl(root, fullpath);
		}
		return newFolderInstance;
	}

	@Override
	public <RESOURCE extends IResource> boolean isSupported(IWorkspace root, IPath fullpath, Class<RESOURCE> clazz) {
		if (IFolder.class.equals(clazz)) {
			return true;
		}
		return false;
	}

	@Override
	public <FILE extends IFile> FILE newFileInstance(IWorkspace root, IPath fullpath, Class<FILE> clazz) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <FOLDER extends IFolder> FOLDER newFolderInstance(IWorkspace root, IPath fullpath, Class<FOLDER> clazz) {
		FOLDER newFolderInstance = null;
		if (IFolder.class.equals(clazz)) {
			newFolderInstance = (FOLDER) new FolderImpl(root, fullpath);
		}
		return newFolderInstance;
	}

	@Override
	public boolean isSupported(IWorkspace root, IPath fullpath, String className) {
		if (IFolder.class.getName().equals(className) || FolderImpl.class.getName().equals(className)) {
			return true;
		}
		return false;
	}

	@Override
	public IFile newFileInstance(IWorkspace root, IPath fullpath, String className) {
		return null;
	}

	@Override
	public IFolder newFolderInstance(IWorkspace root, IPath fullpath, String className) {
		IFolder newFolderInstance = null;
		if (IFolder.class.getName().equals(className) || FolderImpl.class.getName().equals(className)) {
			newFolderInstance = new FolderImpl(root, fullpath);
		}
		return newFolderInstance;
	}

}
