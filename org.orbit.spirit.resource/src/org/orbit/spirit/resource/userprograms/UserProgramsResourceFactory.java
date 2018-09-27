package org.orbit.spirit.resource.userprograms;

import java.net.URI;

import org.orbit.substance.model.dfs.Path;
import org.origin.common.resource.ResourceFactory;
import org.origin.common.resource.ResourceFactoryRegistry;
import org.origin.common.workingcopy.AbstractWorkingCopyFactory;
import org.origin.common.workingcopy.WorkingCopy;

public class UserProgramsResourceFactory extends AbstractWorkingCopyFactory<UserProgramsResource> {

	public static final String FACTORY_NAME = "UserProgramsResourceFactory";

	public static UserProgramsResourceFactory INSTANCE = new UserProgramsResourceFactory();

	public static void register() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (!(factory instanceof UserProgramsResourceFactory)) {
			ResourceFactoryRegistry.INSTANCE.register(FACTORY_NAME, UserProgramsResourceFactory.INSTANCE);
		}
	}

	public static void unregister() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (factory instanceof UserProgramsResourceFactory) {
			ResourceFactoryRegistry.INSTANCE.unregister(FACTORY_NAME);
		}
	}

	@Override
	public String getName() {
		return FACTORY_NAME;
	}

	@Override
	public boolean isSupported(URI uri) {
		String uriPath = uri.getPath();
		if (uriPath != null) {
			Path path = new Path(uriPath);
			if ("user_programs.json".equals(path.getLastSegment())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public UserProgramsResource createResource(URI uri) {
		return new UserProgramsResource(uri);
	}

	@Override
	protected WorkingCopy createWorkingCopy(URI uri) {
		return new UserProgramsWorkingCopy(uri);
	}

}
