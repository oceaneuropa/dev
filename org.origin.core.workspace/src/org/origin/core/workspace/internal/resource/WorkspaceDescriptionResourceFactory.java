package org.origin.core.workspace.internal.resource;

import java.io.File;

import org.origin.common.resource.ResourceFactory;
import org.origin.common.resource.ResourceFactoryRegistry;
import org.origin.common.workingcopy.AbstractWorkingCopyFactory;
import org.origin.common.workingcopy.WorkingCopy;
import org.origin.core.workspace.IWorkspaceDescription;
import org.origin.core.workspace.WorkspaceConstants;

public class WorkspaceDescriptionResourceFactory extends AbstractWorkingCopyFactory<WorkspaceDescriptionResource, IWorkspaceDescription> {

	public static final String FACTORY_NAME = "WorkspaceDescriptionResourceFactory";
	public static WorkspaceDescriptionResourceFactory INSTANCE = new WorkspaceDescriptionResourceFactory();

	/**
	 * Register the workspace description resource factory.
	 * 
	 */
	public static void register() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (!(factory instanceof WorkspaceDescriptionResourceFactory)) {
			ResourceFactoryRegistry.INSTANCE.register(FACTORY_NAME, WorkspaceDescriptionResourceFactory.INSTANCE);
		}
	}

	/**
	 * Unregister the workspace description resource factory.
	 * 
	 */
	public static void unregister() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (factory instanceof WorkspaceDescriptionResourceFactory) {
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
			if (parent != null && parent.isDirectory() && WorkspaceConstants.METADATA_FOLDER.equals(parent.getName()) && WorkspaceConstants.WORKSPACE_JSON.equals(fileName)) {
				try {
					// WorkspaceDescriptionResource resource = new WorkspaceDescriptionResource(file.toURI());
					// resource.load(file);
					// if (resource.getWorkspaceDescription() != null) {
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
	public WorkspaceDescriptionResource createResource(File file) {
		return new WorkspaceDescriptionResource(file.toURI());
	}

	@Override
	protected WorkingCopy<IWorkspaceDescription> createWorkingCopy(File file) {
		return new WorkspaceDescriptionWorkingCopy(file);
	}

}