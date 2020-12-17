package org.orbit.infra.runtime.subs.ws;

import org.orbit.infra.runtime.subs.SubsServerService;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;
import org.origin.common.rest.server.AbstractJerseyWSApplication;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubsServerWSApplication extends AbstractJerseyWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public SubsServerWSApplication(SubsServerService service, int feature) {
		super(service, feature);
		adapt(SubsServerService.class, service);

		registerService(SubsServerService.class, service);
		registerResources(SubServerWSResource.class);
	}

	@Override
	public ServiceMetadata getMetadata() {
		ServiceMetadata metadata = super.getMetadata();
		SubsServerService service = getAdapter(SubsServerService.class);
		if (metadata instanceof ServiceMetadataImpl && service != null) {
			// put more properties to the metadata here
		}
		return metadata;
	}

}
