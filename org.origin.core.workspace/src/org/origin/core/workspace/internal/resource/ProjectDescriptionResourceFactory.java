package org.origin.core.workspace.internal.resource;

import java.io.File;
import java.net.URI;

import org.origin.common.resource.ResourceFactory;
import org.origin.common.resource.ResourceFactoryRegistry;
import org.origin.common.workingcopy.AbstractWorkingCopyFactory;
import org.origin.common.workingcopy.WorkingCopy;
import org.origin.core.workspace.WorkspaceConstants;

public class ProjectDescriptionResourceFactory extends AbstractWorkingCopyFactory<ProjectDescriptionResource> {

	public static final String FACTORY_NAME = "ProjectDescriptionResourceFactory";
	public static ProjectDescriptionResourceFactory INSTANCE = new ProjectDescriptionResourceFactory();

	/**
	 * Register resource factory.
	 * 
	 */
	public static void register() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (!(factory instanceof ProjectDescriptionResourceFactory)) {
			ResourceFactoryRegistry.INSTANCE.register(FACTORY_NAME, ProjectDescriptionResourceFactory.INSTANCE);
		}
	}

	/**
	 * Unregister resource factory.
	 * 
	 */
	public static void unregister() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (factory instanceof ProjectDescriptionResourceFactory) {
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
		if (parent != null && parent.isDirectory() && WorkspaceConstants.METADATA_FOLDER.equals(parent.getName()) && WorkspaceConstants.PROJECT_JSON.equals(fileName)) {
			return true;
		}
		return false;
	}

	@Override
	public ProjectDescriptionResource createResource(URI uri) {
		return new ProjectDescriptionResource(uri);
	}

	@Override
	protected WorkingCopy createWorkingCopy(URI uri) {
		return new ProjectDescriptionWorkingCopy(uri);
	}

}
