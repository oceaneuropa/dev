package org.origin.core.resources;

import java.io.File;

import org.origin.core.resources.internal.FileImpl;
import org.origin.core.resources.internal.FolderImpl;
import org.origin.core.resources.internal.FsRoot;

public class ResourceFactory {

	private static ResourceFactory INSTANCE = new ResourceFactory();

	public static ResourceFactory getInstance() {
		return INSTANCE;
	}

	private ResourceFactory() {
	}

	/**
	 * 
	 * @param underlyingRootResource
	 * @return
	 */
	public IRoot createFsRoot(File underlyingRootResource) {
		return new FsRoot(underlyingRootResource);
	}

	/**
	 * 
	 * @param root
	 * @param fullpath
	 * @return
	 */
	public IFile newFileInstance(IRoot root, IPath fullpath) {
		IFile newFile = null;
		ResourceProvider[] providers = ResourceProviderRegistry.getInstance().getResourceProviders();
		for (ResourceProvider provider : providers) {
			if (provider.isSupported(root, fullpath)) {
				newFile = provider.newFileInstance(root, fullpath);
				if (newFile != null) {
					break;
				}
			}
		}

		// New IFile is not created by providers
		// - Create default IFile
		if (newFile == null) {
			newFile = createFile(root, fullpath);
		}

		return newFile;
	}

	/**
	 * 
	 * @param root
	 * @param fullpath
	 * @return
	 */
	public IFolder newFolderInstance(IRoot root, IPath fullpath) {
		IFolder newFolder = null;

		// Ask providers to create new IFolder
		ResourceProvider[] providers = ResourceProviderRegistry.getInstance().getResourceProviders();
		for (ResourceProvider provider : providers) {
			if (provider.isSupported(root, fullpath)) {
				newFolder = provider.newFolderInstance(root, fullpath);
				if (newFolder != null) {
					break;
				}
			}
		}

		// New IFolder is not created by providers
		// - Create default IFolder
		if (newFolder == null) {
			newFolder = createFolder(root, fullpath);
		}

		return newFolder;
	}

	/**
	 * 
	 * @param root
	 * @param fullpath
	 * @param clazz
	 * @return
	 */
	public <FILE extends IFile> FILE newFileInstance(IRoot root, IPath fullpath, Class<FILE> clazz) {
		FILE newFILE = null;

		// Ask providers to create new FILE
		ResourceProvider[] providers = ResourceProviderRegistry.getInstance().getResourceProviders();
		for (ResourceProvider provider : providers) {
			if (provider.isSupported(root, fullpath, clazz)) {
				newFILE = provider.newFileInstance(root, fullpath, clazz);
				if (newFILE != null) {
					break;
				}
			}
		}
		if (newFILE == null) {
			throw new RuntimeException("Class \"" + clazz.getName() + "\" is not supported by ResourceProviders for creating new file instance.");
		}

		return newFILE;
	}

	/**
	 * 
	 * @param root
	 * @param fullpath
	 * @param clazz
	 * @return
	 */
	public <FOLDER extends IFolder> FOLDER newFolderInstance(IRoot root, IPath fullpath, Class<FOLDER> clazz) {
		FOLDER newFOLDER = null;

		// Ask providers to create new FOLDER
		ResourceProvider[] providers = ResourceProviderRegistry.getInstance().getResourceProviders();
		for (ResourceProvider provider : providers) {
			if (provider.isSupported(root, fullpath, clazz)) {
				newFOLDER = provider.newFolderInstance(root, fullpath, clazz);
				if (newFOLDER != null) {
					break;
				}
			}
		}
		if (newFOLDER == null) {
			throw new RuntimeException("Class \"" + clazz.getName() + "\" is not supported by ResourceProviders for creating new folder instance.");
		}

		return newFOLDER;
	}

	/**
	 * 
	 * @param root
	 * @param fullpath
	 * @return
	 */
	public IFolder createFolder(IRoot root, IPath fullpath) {
		return new FolderImpl(root, fullpath);
	}

	/**
	 * 
	 * @param root
	 * @param fullpath
	 * @return
	 */
	public IFile createFile(IRoot root, IPath fullpath) {
		return new FileImpl(root, fullpath);
	}

}
