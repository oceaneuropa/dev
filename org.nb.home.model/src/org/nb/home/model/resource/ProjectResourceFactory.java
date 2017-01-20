package org.nb.home.model.resource;

import java.io.File;
import java.net.URI;

import org.origin.common.resource.ResourceFactory;
import org.origin.common.resource.ResourceFactoryRegistry;
import org.origin.common.workingcopy.AbstractWorkingCopyFactory;
import org.origin.common.workingcopy.WorkingCopy;

public class ProjectResourceFactory extends AbstractWorkingCopyFactory<ProjectResource> {

	public static final String FACTORY_NAME = "ProjectResourceFactory";
	public static ProjectResourceFactory INSTANCE = new ProjectResourceFactory();

	/**
	 * Register resource factory.
	 * 
	 */
	public static void register() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (!(factory instanceof ProjectResourceFactory)) {
			ResourceFactoryRegistry.INSTANCE.register(FACTORY_NAME, ProjectResourceFactory.INSTANCE);
		}
	}

	/**
	 * Unregister resource factory.
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
	public boolean isSupported(URI uri) {
		File file = new File(uri);
		File parent = file.getParentFile();
		String fileName = file.getName();
		if (parent != null && parent.isDirectory() && "META-INF".equals(parent.getName()) && "project.json".equals(fileName)) {
			return true;
		}
		return false;
	}

	@Override
	public ProjectResource createResource(URI uri) {
		return new ProjectResource(uri);
	}

	@Override
	protected WorkingCopy createWorkingCopy(URI uri) {
		return new ProjectWorkingCopy(uri);
	}

}