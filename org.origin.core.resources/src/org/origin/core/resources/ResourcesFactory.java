package org.origin.core.resources;

import java.io.File;

import org.origin.core.resources.extension.ResourceProvider;
import org.origin.core.resources.extension.ResourceProviderExtensionRegistry;
import org.origin.core.resources.impl.FileImpl;
import org.origin.core.resources.impl.FolderImpl;
import org.origin.core.resources.impl.local.WorkspaceLocalImpl;

public class ResourcesFactory {

	private static ResourcesFactory DEFAULT = new ResourcesFactory();
	private static ResourcesFactory factory;

	public synchronized static ResourcesFactory getInstance() {
		if (factory == null) {
			factory = DEFAULT;
		}
		return factory;
	}

	public synchronized static void setInstance(ResourcesFactory factory) {
		ResourcesFactory.factory = factory;
	}

	private ResourcesFactory() {
	}

	/**
	 * 
	 * @param workspaceFolder
	 * @return
	 */
	public IWorkspace createWorkspace(File workspaceFolder) {
		return new WorkspaceLocalImpl(workspaceFolder);
	}

	/**
	 * 
	 * @param workspace
	 * @param fullpath
	 * @return
	 */
	public IFile createFile(IWorkspace workspace, IPath fullpath) {
		IFile newFile = null;
		ResourceProvider[] providers = ResourceProviderExtensionRegistry.getInstance().getResourceProviders();
		for (ResourceProvider provider : providers) {
			if (provider.isSupported(workspace, fullpath)) {
				newFile = provider.newFileInstance(workspace, fullpath);
				if (newFile != null) {
					break;
				}
			}
		}

		// New IFile is not created by providers
		// - Create IFile by default
		if (newFile == null) {
			newFile = new FileImpl(workspace, fullpath);
		}

		return newFile;
	}

	/**
	 * 
	 * @param workspace
	 * @param fullpath
	 * @param clazz
	 * @return
	 */
	public <FILE extends IFile> FILE createFile(IWorkspace workspace, IPath fullpath, Class<FILE> clazz) {
		FILE newFILE = null;

		// Ask providers to create new FILE
		ResourceProvider[] providers = ResourceProviderExtensionRegistry.getInstance().getResourceProviders();
		for (ResourceProvider provider : providers) {
			if (provider.isSupported(workspace, fullpath, clazz)) {
				newFILE = provider.newFileInstance(workspace, fullpath, clazz);
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
	 * @param workspace
	 * @param fullpath
	 * @return
	 */
	public IFolder createFolder(IWorkspace workspace, IPath fullpath) {
		IFolder newFolder = null;

		// Ask providers to create new IFolder
		ResourceProvider[] providers = ResourceProviderExtensionRegistry.getInstance().getResourceProviders();
		for (ResourceProvider provider : providers) {
			if (provider.isSupported(workspace, fullpath)) {
				newFolder = provider.newFolderInstance(workspace, fullpath);
				if (newFolder != null) {
					break;
				}
			}
		}

		// New IFolder is not created by providers
		// - Create IFolder by default
		if (newFolder == null) {
			newFolder = new FolderImpl(workspace, fullpath);
		}

		return newFolder;
	}

	/**
	 * 
	 * @param workspace
	 * @param fullpath
	 * @param clazz
	 * @return
	 */
	public <FOLDER extends IFolder> FOLDER createFolder(IWorkspace workspace, IPath fullpath, Class<FOLDER> clazz) {
		FOLDER newFOLDER = null;

		// Ask providers to create new FOLDER
		ResourceProvider[] providers = ResourceProviderExtensionRegistry.getInstance().getResourceProviders();
		for (ResourceProvider provider : providers) {
			if (provider.isSupported(workspace, fullpath, clazz)) {
				newFOLDER = provider.newFolderInstance(workspace, fullpath, clazz);
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

}
