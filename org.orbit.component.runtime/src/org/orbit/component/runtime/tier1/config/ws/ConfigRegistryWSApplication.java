package org.orbit.component.runtime.tier1.config.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;

public class ConfigRegistryWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public ConfigRegistryWSApplication(final ConfigRegistryService service, int feature) {
		super(service.getContextRoot(), feature);
		adapt(ConfigRegistryService.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(ConfigRegistryService.class);
			}
		});
		register(ConfigRegistryWSResource.class);
	}

}
