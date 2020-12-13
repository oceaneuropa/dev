package org.orbit.infra.model.repo.resource;

import java.net.URI;

import org.orbit.infra.model.repo.RepoConstants;
import org.origin.common.resource.Path;
import org.origin.common.resource.ResourceFactory;
import org.origin.common.resource.ResourceFactoryRegistry;
import org.origin.common.workingcopy.AbstractWorkingCopyFactory;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ReposResourceFactory extends AbstractWorkingCopyFactory<ReposResource> {

	public static final String FACTORY_NAME = "ReposResourceFactory";

	public static ReposResourceFactory INSTANCE = new ReposResourceFactory();

	public static void register() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (!(factory instanceof ReposResourceFactory)) {
			ResourceFactoryRegistry.INSTANCE.register(FACTORY_NAME, ReposResourceFactory.INSTANCE);
		}
	}

	public static void unregister() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (factory instanceof ReposResourceFactory) {
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
			if (RepoConstants.REPOS_FILE_NAME.equals(path.getLastSegment())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ReposResource createResource(URI uri) {
		return new ReposResource(uri);
	}

}
