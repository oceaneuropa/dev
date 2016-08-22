package org.origin.core.workspace.internal.resource;

import java.io.File;

import org.origin.common.resource.ResourceFactory;
import org.origin.common.resource.ResourceFactoryRegistry;
import org.origin.common.workingcopy.AbstractWorkingCopyFactory;
import org.origin.common.workingcopy.WorkingCopy;

public class WorkspaceResourceFactory extends AbstractWorkingCopyFactory<WorkspaceResource, WorkspaceDescription> {

	public static final String FACTORY_NAME = "WorkspaceResourceFactory";
	public static WorkspaceResourceFactory INSTANCE = new WorkspaceResourceFactory();

	/**
	 * Register the workspace resource factory.
	 * 
	 */
	public static void register() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (!(factory instanceof WorkspaceResourceFactory)) {
			ResourceFactoryRegistry.INSTANCE.register(FACTORY_NAME, WorkspaceResourceFactory.INSTANCE);
		}
	}

	/**
	 * Unregister the workspace resource factory.
	 * 
	 */
	public static void unregister() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (factory instanceof WorkspaceResourceFactory) {
			ResourceFactoryRegistry.INSTANCE.unregister(FACTORY_NAME);
		}
	}

	private WorkspaceResourceFactory() {
	}

	@Override
	public String getName() {
		return FACTORY_NAME;
	}

	@Override
	public boolean isSupported(File file) {
		File parent = file.getParentFile();
		String fileName = file.getName();
		if (parent != null && parent.isDirectory() && ".metadata".equals(parent.getName()) && "workspace.json".equals(fileName)) {
			try {
				WorkspaceResource resource = new WorkspaceResource(file.toURI());
				resource.load(file);
				if (resource.getWorkspace() != null) {
					return true;
				}
			} catch (Exception e) {
				// ignore
			}
		}
		return false;
	}

	@Override
	public WorkspaceResource createResource(File file) {
		return new WorkspaceResource(file.toURI());
	}

	@Override
	protected WorkingCopy<WorkspaceDescription> createWorkingCopy(File file) {
		return new WorkspaceWorkingCopy(file);
	}

}