package org.origin.core.workspace.internal.resource;

import java.io.File;
import java.net.URI;

import org.origin.common.resource.ResourceFactory;
import org.origin.common.resource.ResourceFactoryRegistry;
import org.origin.common.workingcopy.AbstractWorkingCopyFactory;
import org.origin.common.workingcopy.WorkingCopy;
import org.origin.core.workspace.WorkspaceConstants;

public class WorkspaceDescriptionResourceFactory extends AbstractWorkingCopyFactory<WorkspaceDescriptionResource> {

	public static final String FACTORY_NAME = "WorkspaceDescriptionResourceFactory";
	public static WorkspaceDescriptionResourceFactory INSTANCE = new WorkspaceDescriptionResourceFactory();

	/**
	 * Register resource factory.
	 * 
	 */
	public static void register() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (!(factory instanceof WorkspaceDescriptionResourceFactory)) {
			ResourceFactoryRegistry.INSTANCE.register(FACTORY_NAME, WorkspaceDescriptionResourceFactory.INSTANCE);
		}
	}

	/**
	 * Unregister resource factory.
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
	public boolean isSupported(URI uri) {
		File file = new File(uri);
		File parent = file.getParentFile();
		String fileName = file.getName();
		if (parent != null && parent.isDirectory() && WorkspaceConstants.METADATA_FOLDER.equals(parent.getName()) && WorkspaceConstants.WORKSPACE_JSON.equals(fileName)) {
			return true;
		}
		return false;
	}

	@Override
	public WorkspaceDescriptionResource createResource(URI uri) {
		return new WorkspaceDescriptionResource(uri);
	}

	@Override
	protected WorkingCopy createWorkingCopy(URI uri) {
		return new WorkspaceDescriptionWorkingCopy(uri);
	}

}
