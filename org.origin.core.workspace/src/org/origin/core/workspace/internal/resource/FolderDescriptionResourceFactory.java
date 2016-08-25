package org.origin.core.workspace.internal.resource;

import java.io.File;

import org.origin.common.resource.ResourceFactory;
import org.origin.common.resource.ResourceFactoryRegistry;
import org.origin.common.workingcopy.AbstractWorkingCopyFactory;
import org.origin.common.workingcopy.WorkingCopy;
import org.origin.core.workspace.IFolderDescription;
import org.origin.core.workspace.WorkspaceConstants;

public class FolderDescriptionResourceFactory extends AbstractWorkingCopyFactory<FolderDescriptionResource, IFolderDescription> {

	public static final String FACTORY_NAME = "FolderDescriptionResourceFactory";
	public static FolderDescriptionResourceFactory INSTANCE = new FolderDescriptionResourceFactory();

	/**
	 * Register the folder description resource factory.
	 * 
	 */
	public static void register() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (!(factory instanceof FolderDescriptionResourceFactory)) {
			ResourceFactoryRegistry.INSTANCE.register(FACTORY_NAME, FolderDescriptionResourceFactory.INSTANCE);
		}
	}

	/**
	 * Unregister the folder description resource factory.
	 * 
	 */
	public static void unregister() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (factory instanceof FolderDescriptionResourceFactory) {
			ResourceFactoryRegistry.INSTANCE.unregister(FACTORY_NAME);
		}
	}

	@Override
	public String getName() {
		return FACTORY_NAME;
	}

	@Override
	public boolean isSupported(File file) {
		if (file != null && file.isFile()) {
			File parent = file.getParentFile();
			String fileName = file.getName();
			if (parent != null && parent.isDirectory() && WorkspaceConstants.METADATA_FOLDER.equals(parent.getName()) && fileName.endsWith(WorkspaceConstants.DOT_FOLDER_JSON)) {
				try {
					// FolderDescriptionResource resource = new FolderDescriptionResource(file.toURI());
					// resource.load(file);
					// if (resource.getFolderDescription() != null) {
					// return true;
					// }
					return true;
				} catch (Exception e) {
					// ignore
				}
			}
		}
		return false;
	}

	@Override
	public FolderDescriptionResource createResource(File file) {
		return new FolderDescriptionResource(file.toURI());
	}

	@Override
	protected WorkingCopy<IFolderDescription> createWorkingCopy(File file) {
		return new FolderDescriptionWorkingCopy(file);
	}

}