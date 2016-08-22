package org.origin.core.workspace.internal.resource;

import java.io.File;

import org.origin.common.resource.ResourceFactory;
import org.origin.common.resource.ResourceFactoryRegistry;
import org.origin.common.workingcopy.AbstractWorkingCopyFactory;
import org.origin.common.workingcopy.WorkingCopy;

public class ProjectResourceFactory extends AbstractWorkingCopyFactory<ProjectResource, ProjectDescription> {

	public static final String FACTORY_NAME = "ProjectResourceFactory";
	public static ProjectResourceFactory INSTANCE = new ProjectResourceFactory();

	/**
	 * Register the project resource factory.
	 * 
	 */
	public static void register() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (!(factory instanceof ProjectResourceFactory)) {
			ResourceFactoryRegistry.INSTANCE.register(FACTORY_NAME, ProjectResourceFactory.INSTANCE);
		}
	}

	/**
	 * Unregister the project resource factory.
	 * 
	 */
	public static void unregister() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (factory instanceof ProjectResourceFactory) {
			ResourceFactoryRegistry.INSTANCE.unregister(FACTORY_NAME);
		}
	}

	private ProjectResourceFactory() {
	}

	@Override
	public String getName() {
		return FACTORY_NAME;
	}

	@Override
	public boolean isSupported(File file) {
		File parent = file.getParentFile();
		String fileName = file.getName();
		if (parent != null && parent.isDirectory() && "META-INF".equals(parent.getName()) && "project.json".equals(fileName)) {
			try {
				ProjectResource resource = new ProjectResource(file.toURI());
				resource.load(file);
				if (resource.getProject() != null) {
					return true;
				}
			} catch (Exception e) {
				// ignore
			}
		}
		return false;
	}

	@Override
	public ProjectResource createResource(File file) {
		return new ProjectResource(file.toURI());
	}

	@Override
	protected WorkingCopy<ProjectDescription> createWorkingCopy(File file) {
		return new ProjectWorkingCopy(file);
	}

}