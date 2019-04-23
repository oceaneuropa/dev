package org.orbit.component.runtime.tier1.config.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceV0;

public class ConfigRegistryWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public ConfigRegistryWSApplication(final ConfigRegistryServiceV0 service, int feature) {
		super(service, feature);
		// adapt(ConfigRegistryService.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(ConfigRegistryServiceV0.class);
			}
		});
		register(ConfigRegistryWSResource.class);
	}

}
