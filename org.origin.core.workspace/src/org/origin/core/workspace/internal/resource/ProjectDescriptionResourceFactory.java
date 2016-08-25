package org.origin.core.workspace.internal.resource;

import java.io.File;

import org.origin.common.resource.ResourceFactory;
import org.origin.common.resource.ResourceFactoryRegistry;
import org.origin.common.workingcopy.AbstractWorkingCopyFactory;
import org.origin.common.workingcopy.WorkingCopy;
import org.origin.core.workspace.IProjectDescription;
import org.origin.core.workspace.WorkspaceConstants;

public class ProjectDescriptionResourceFactory extends AbstractWorkingCopyFactory<ProjectDescriptionResource, IProjectDescription> {

	public static final String FACTORY_NAME = "ProjectDescriptionResourceFactory";
	public static ProjectDescriptionResourceFactory INSTANCE = new ProjectDescriptionResourceFactory();

	/**
	 * Register the project description resource factory.
	 * 
	 */
	public static void register() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (!(factory instanceof ProjectDescriptionResourceFactory)) {
			ResourceFactoryRegistry.INSTANCE.register(FACTORY_NAME, ProjectDescriptionResourceFactory.INSTANCE);
		}
	}

	/**
	 * Unregister the project description resource factory.
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
	public boolean isSupported(File file) {
		if (file != null && file.isFile()) {
			File parent = file.getParentFile();
			String fileName = file.getName();
			if (parent != null && parent.isDirectory() && WorkspaceConstants.METADATA_FOLDER.equals(parent.getName()) && WorkspaceConstants.PROJECT_JSON.equals(fileName)) {
				try {
					// ProjectDescriptionResource resource = new ProjectDescriptionResource(file.toURI());
					// resource.load(file);
					// if (resource.getProjectDescription() != null) {
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
	public ProjectDescriptionResource createResource(File file) {
		return new ProjectDescriptionResource(file.toURI());
	}

	@Override
	protected WorkingCopy<IProjectDescription> createWorkingCopy(File file) {
		return new ProjectDescriptionWorkingCopy(file);
	}

}
