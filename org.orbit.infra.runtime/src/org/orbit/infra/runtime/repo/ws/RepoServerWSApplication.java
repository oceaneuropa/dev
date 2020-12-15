package org.orbit.infra.runtime.repo.ws;

import org.orbit.infra.runtime.repo.RepoServerService;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;
import org.origin.common.rest.server.AbstractJerseyWSApplication;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class RepoServerWSApplication extends AbstractJerseyWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public RepoServerWSApplication(final RepoServerService service, int feature) {
		super(service, feature);
		adapt(RepoServerService.class, service);

		registerService(RepoServerService.class, service);
		registerResources(RepoServerWSResource.class);
	}

	@Override
	public ServiceMetadata getMetadata() {
		ServiceMetadata metadata = super.getMetadata();
		RepoServerService service = getAdapter(RepoServerService.class);
		if (metadata instanceof ServiceMetadataImpl && service != null) {
			// put more properties to the metadata here
		}
		return metadata;
	}

}
