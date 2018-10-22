package org.orbit.infra.runtime.configregistry.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;
import org.origin.common.rest.server.AbstractJerseyWSApplication;

public class ConfigRegistryWSApplication extends AbstractJerseyWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public ConfigRegistryWSApplication(final ConfigRegistryService service, int feature) {
		super(service, feature);
		adapt(ConfigRegistryService.class, service);

		AbstractBinder binder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(ConfigRegistryService.class);
			}
		};
		register(binder);
		register(ConfigRegistryWSResource.class);
	}

	@Override
	public ServiceMetadata getMetadata() {
		ServiceMetadata metadata = super.getMetadata();
		ConfigRegistryService service = getAdapter(ConfigRegistryService.class);
		if (metadata instanceof ServiceMetadataImpl && service != null) {
			// put more properties to the metadata here
		}
		return metadata;
	}

}
