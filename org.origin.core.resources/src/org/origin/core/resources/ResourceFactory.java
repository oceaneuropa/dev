package org.origin.core.resources;

import java.io.File;

import org.origin.core.resources.extension.ResourceProvider;
import org.origin.core.resources.extension.ResourceProviderExtensionRegistry;
import org.origin.core.resources.impl.FileImpl;
import org.origin.core.resources.impl.FolderImpl;
import org.origin.core.resources.impl.local.WorkspaceLocalImpl;
import org.origin.core.resources.IWorkspace;

public class ResourceFactory {

	private static ResourceFactory INSTANCE = new ResourceFactory();

	public static ResourceFactory getInstance() {
		return INSTANCE;
	}

	private ResourceFactory() {
	}

	/**
	 * 
	 * @param workspaceFolder
	 * @return
	 */
	public IWorkspace newWorkspaceInstanceForFileSystem(File workspaceFolder) {
		return new WorkspaceLocalImpl(workspaceFolder);
	}

	/**
	 * 
	 * @return
	 */
	public IWorkspace newWorkspaceInstanceForFileSystem() {
		return new WorkspaceLocalImpl();
	}

	/**
	 * 
	 * @param root
	 * @param fullpath
	 * @return
	 */
	public IFile newFileInstance(IWorkspace root, IPath fullpath) {
		IFile newFile = null;
		ResourceProvider[] providers = ResourceProviderExtensionRegistry.getInstance().getResourceProviders();
		for (ResourceProvider provider : providers) {
			if (provider.isSupported(root, fullpath)) {
				newFile = provider.newFileInstance(root, fullpath);
				if (newFile != null) {
					break;
				}
			}
		}

		// New IFile is not created by providers
		// - Create IFile by default
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
	public IFolder newFolderInstance(IWorkspace root, IPath fullpath) {
		IFolder newFolder = null;

		// Ask providers to create new IFolder
		ResourceProvider[] providers = ResourceProviderExtensionRegistry.getInstance().getResourceProviders();
		for (ResourceProvider provider : providers) {
			if (provider.isSupported(root, fullpath)) {
				newFolder = provider.newFolderInstance(root, fullpath);
				if (newFolder != null) {
					break;
				}
			}
		}

		// New IFolder is not created by providers
		// - Create IFolder by default
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
	public <FILE extends IFile> FILE newFileInstance(IWorkspace root, IPath fullpath, Class<FILE> clazz) {
		FILE newFILE = null;

		// Ask providers to create new FILE
		ResourceProvider[] providers = ResourceProviderExtensionRegistry.getInstance().getResourceProviders();
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
	public <FOLDER extends IFolder> FOLDER newFolderInstance(IWorkspace root, IPath fullpath, Class<FOLDER> clazz) {
		FOLDER newFOLDER = null;

		// Ask providers to create new FOLDER
		ResourceProvider[] providers = ResourceProviderExtensionRegistry.getInstance().getResourceProviders();
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
	public IFolder createFolder(IWorkspace root, IPath fullpath) {
		return new FolderImpl(root, fullpath);
	}

	/**
	 * 
	 * @param root
	 * @param fullpath
	 * @return
	 */
	public IFile createFile(IWorkspace root, IPath fullpath) {
		return new FileImpl(root, fullpath);
	}

}
