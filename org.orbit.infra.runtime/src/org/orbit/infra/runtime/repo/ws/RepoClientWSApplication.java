package org.orbit.infra.runtime.repo.ws;

import org.orbit.infra.runtime.repo.RepoClientService;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;
import org.origin.common.rest.server.AbstractJerseyWSApplication;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class RepoClientWSApplication extends AbstractJerseyWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public RepoClientWSApplication(final RepoClientService service, int feature) {
		super(service, feature);
		adapt(RepoClientService.class, service);

		registerService(RepoClientService.class, service);
		registerResources(RepoClientWSResource.class);
	}

	@Override
	public ServiceMetadata getMetadata() {
		ServiceMetadata metadata = super.getMetadata();
		RepoClientService service = getAdapter(RepoClientService.class);
		if (metadata instanceof ServiceMetadataImpl && service != null) {
			// put more properties to the metadata here
		}
		return metadata;
	}

}
