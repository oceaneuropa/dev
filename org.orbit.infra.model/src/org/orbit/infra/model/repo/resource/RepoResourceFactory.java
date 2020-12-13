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
public class RepoResourceFactory extends AbstractWorkingCopyFactory<RepoResource> {

	public static final String FACTORY_NAME = "RepoResourceFactory";

	public static RepoResourceFactory INSTANCE = new RepoResourceFactory();

	public static void register() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (!(factory instanceof RepoResourceFactory)) {
			ResourceFactoryRegistry.INSTANCE.register(FACTORY_NAME, RepoResourceFactory.INSTANCE);
		}
	}

	public static void unregister() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (factory instanceof RepoResourceFactory) {
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
			if (RepoConstants.REPO_FILE_NAME.equals(path.getLastSegment())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public RepoResource createResource(URI uri) {
		return new RepoResource(uri);
	}

}
